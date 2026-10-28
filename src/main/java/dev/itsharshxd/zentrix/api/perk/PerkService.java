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
 * <p>A player belongs to the enabled tier with the highest priority whose permission they hold.
 * Every perk the tier does not set, and every perk of a player without a tier, follows the
 * server-wide default: {@code settings.yml} for host limits, party size and scenario settings,
 * and the {@code defaults} of {@code perks.yml} for the rest. Active boosts apply on top.
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
     * above their own whose value is higher, or that allows a toggle they are refused. What the
     * limit message names, so a shop can offer it.
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
}
