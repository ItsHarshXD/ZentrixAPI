package dev.itsharshxd.zentrix.api.perk;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Rank-based limits from {@code perks.yml}: named tiers tied to permissions, and temporary
 * boosts given to single players.
 *
 * <p>A player holds every enabled tier whose permission they have, and is shown as belonging to
 * the highest-priority one. The {@linkplain #getResolutionMode() resolution mode} decides where
 * each perk comes from: that tier, or the best value among all the tiers they hold. A tier's value
 * comes from the tier itself, then up its parent chain. Every perk no tier gives, and every perk of
 * a player without a tier, follows the server-wide default: {@code settings.yml} for host limits,
 * party size and scenario settings, the {@code defaults} of {@code perks.yml} for the rest, and an
 * addon's own default for its perks. Active personal and server-wide boosts apply on top.
 *
 * <p>Addons can {@linkplain #registerPerk register perks} of their own, which resolve exactly as
 * the built-in ones do. A number value of {@link PerkDefinition#UNLIMITED} in {@code perks.yml}
 * means unlimited for perks that support it; {@link #getNumber} answers {@link Integer#MAX_VALUE}
 * for it, so limits compare as usual.
 *
 * <p>Zentrix asks for a perk when the player acts - creating a game, inviting someone - so a rank
 * that expires takes nothing away the player already has. When a perk stops a player,
 * {@link dev.itsharshxd.zentrix.api.events.perk.PerkLimitReachedEvent} fires.
 *
 * <p>Reads answer at once, on the main thread, from what Zentrix holds for online players. Boost
 * changes are stored with the player's data, work for offline players and complete on the main
 * thread; they complete exceptionally while storage is unavailable.
 *
 * <pre>{@code
 * PerkService perks = ZentrixAPI.get().getPerkService();
 * int slots = perks.getNumber(player, Perk.CUSTOM_GAMES_MAX_HOSTED);
 * perks.grantBoost(player.getUniqueId(), Perk.CUSTOM_GAMES_MAX_HOSTED, 1, Duration.ofHours(24), "shop");
 * }</pre>
 *
 * @since 1.9.0
 */
public interface PerkService {

    /** The enabled tiers, highest priority first. Empty when {@code perks.yml} enables none. */
    @NotNull
    List<PerkTier> getTiers();

    /** An enabled tier by its {@code perks.yml} key, ignoring case. */
    @NotNull
    Optional<PerkTier> getTier(@NotNull String id);

    /** The tier a player belongs to now, or empty when they hold no tier's permission. */
    @NotNull
    Optional<PerkTier> getTier(@NotNull Player player);

    /**
     * A {@link Perk.Type#NUMBER} perk as it applies to a player now: their tier's value or the
     * server-wide default, plus their active boosts.
     *
     * @throws IllegalArgumentException for a toggle perk
     */
    int getNumber(@NotNull Player player, @NotNull Perk perk);

    /**
     * Whether a {@link Perk.Type#TOGGLE} perk is allowed for a player now: by their tier, the
     * server-wide default, or an active boost.
     *
     * @throws IllegalArgumentException for a number perk
     */
    boolean isAllowed(@NotNull Player player, @NotNull Perk perk);

    /** The server-wide default of a {@link Perk.Type#NUMBER} perk, for players without a tier. */
    int getDefaultNumber(@NotNull Perk perk);

    /** The server-wide default of a {@link Perk.Type#TOGGLE} perk, for players without a tier. */
    boolean isAllowedByDefault(@NotNull Perk perk);

    /**
     * The tier a player would have to reach for more of a perk: the lowest-priority enabled tier
     * above their own whose value is higher, or that allows a toggle they are refused. Under
     * {@link PerkResolutionMode#BEST_VALUE}, the lowest-priority enabled tier they do not hold that
     * would beat what they get now. What the limit message names, so a shop can offer it.
     *
     * @return empty when no tier above theirs would give them more
     */
    @NotNull
    Optional<PerkTier> getNextTier(@NotNull Player player, @NotNull Perk perk);

    /** A player's active boosts, as last read. Empty for offline players; see {@link #refreshBoosts}. */
    @NotNull
    List<PerkBoost> getBoosts(@NotNull UUID playerId);

    /** Reads a player's boosts as stored now, online or offline, which another server may have changed. */
    @NotNull
    CompletableFuture<List<PerkBoost>> refreshBoosts(@NotNull UUID playerId);

    /**
     * Gives a player a temporary boost, online or offline. It ends on its own after
     * {@code duration}.
     *
     * @param amount   added to a number perk; must be positive. Ignored by a toggle perk, which the
     *                 boost allows
     * @param duration how long it lasts; must be positive
     * @param source   who or what gave it, kept with the boost
     * @return completes on the main thread with the boost as stored
     * @throws IllegalArgumentException when the amount or duration is not positive
     */
    @NotNull
    CompletableFuture<PerkBoost> grantBoost(@NotNull UUID playerId, @NotNull Perk perk, int amount,
                                            @NotNull Duration duration, @NotNull String source);

    /**
     * Ends one of a player's boosts early.
     *
     * @return completes with whether the boost existed
     */
    @NotNull
    CompletableFuture<Boolean> removeBoost(@NotNull UUID playerId, @NotNull String boostId);

    /**
     * Ends a player's boosts early: every boost, or every boost of one perk.
     *
     * @param perk the perk whose boosts end, or null for all of them
     * @return completes with how many boosts ended
     */
    @NotNull
    CompletableFuture<Integer> clearBoosts(@NotNull UUID playerId, @Nullable Perk perk);

    // ------------------------------------------------------------------
    // Addon perks
    // ------------------------------------------------------------------

    /**
     * Registers an addon perk. From then on tiers in {@code perks.yml} can set it under its key,
     * boosts can raise it, and {@code /perks}, the tier editor and placeholders
     * ({@code %zentrix_perk_<key>%}, dots and dashes as underscores) show it, exactly as they do
     * built-in perks.
     *
     * <p>Values and boosts of a perk that is not registered are kept as they are, so an addon
     * that loads late, or is removed for a while, finds them again. The perk is unregistered when
     * its owner disables. Main thread, such as from the addon's {@code onEnable}.
     *
     * @param owner      the addon registering it
     * @param definition the perk; build one with {@link PerkDefinition#number} or
     *                   {@link PerkDefinition#toggle}
     * @throws IllegalArgumentException when the key is a built-in perk's, or another addon's
     * @throws IllegalStateException    off the main thread
     * @since 1.16.0
     */
    void registerPerk(@NotNull org.bukkit.plugin.Plugin owner, @NotNull PerkDefinition definition);

    /**
     * Unregisters an addon perk. Its values in {@code perks.yml} and its stored boosts are kept.
     *
     * @return whether it was registered
     * @since 1.16.0
     */
    boolean unregisterPerk(@NotNull String key);

    /**
     * Every perk, built-in perks first in their order, then registered addon perks.
     *
     * @since 1.16.0
     */
    @NotNull
    List<PerkDefinition> getPerks();

    /**
     * A built-in or registered addon perk by its exact key.
     *
     * @since 1.16.0
     */
    @NotNull
    Optional<PerkDefinition> getPerk(@NotNull String key);

    /**
     * A number perk, built-in or addon, as it applies to a player now. {@link Integer#MAX_VALUE}
     * when unlimited.
     *
     * @throws IllegalArgumentException for a toggle perk or a key nothing registered
     * @since 1.16.0
     */
    int getNumber(@NotNull Player player, @NotNull String perkKey);

    /**
     * Whether a toggle perk, built-in or addon, is allowed for a player now.
     *
     * @throws IllegalArgumentException for a number perk or a key nothing registered
     * @since 1.16.0
     */
    boolean isAllowed(@NotNull Player player, @NotNull String perkKey);

    /**
     * The tier a player would have to reach for more of a perk, built-in or addon, as
     * {@link #getNextTier(Player, Perk)} answers it.
     *
     * @since 1.16.0
     */
    @NotNull
    Optional<PerkTier> getNextTier(@NotNull Player player, @NotNull String perkKey);

    /**
     * How a player holding several tiers gets each perk, from {@code perks.yml}.
     *
     * @since 1.16.0
     */
    @NotNull
    PerkResolutionMode getResolutionMode();

    /**
     * A player's active boosts of every perk, addon perks included, as last read. Boosts of addon
     * perks have a null {@link PerkBoost#perk()}; {@link #getBoosts} leaves them out.
     *
     * @since 1.16.0
     */
    @NotNull
    List<PerkBoost> getAllBoosts(@NotNull UUID playerId);

    /**
     * Gives a player a temporary boost of any perk, built-in or addon, by its key.
     *
     * @throws IllegalArgumentException when the amount or duration is not positive, or the key
     *                                  names no perk
     * @since 1.16.0
     */
    @NotNull
    CompletableFuture<PerkBoost> grantBoost(@NotNull UUID playerId, @NotNull String perkKey, int amount,
                                            @NotNull Duration duration, @NotNull String source);

    // ------------------------------------------------------------------
    // Server-wide boosts
    // ------------------------------------------------------------------

    /**
     * The server-wide boosts in force, as last read. They apply to every player, on top of their
     * own boosts, and are shared by every server on the same database.
     *
     * @since 1.16.0
     */
    @NotNull
    List<ServerBoost> getServerBoosts();

    /**
     * Starts a server-wide boost, announced to players and ending on its own after
     * {@code duration}.
     *
     * @param amount added to a number perk; must be positive. Ignored by a toggle perk
     * @return completes on the main thread with the boost as stored
     * @throws IllegalArgumentException when the amount or duration is not positive, or the key
     *                                  names no perk
     * @since 1.16.0
     */
    @NotNull
    CompletableFuture<ServerBoost> startServerBoost(@NotNull String perkKey, int amount,
                                                    @NotNull Duration duration, @NotNull String source);

    /**
     * Ends one server-wide boost early.
     *
     * @return completes with whether it existed
     * @since 1.16.0
     */
    @NotNull
    CompletableFuture<Boolean> endServerBoost(@NotNull String boostId);
}
