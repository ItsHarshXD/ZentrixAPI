package dev.itsharshxd.zentrix.api.cosmetics;

import dev.itsharshxd.zentrix.api.addon.Registration;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The cosmetics each player owns, per category, and the one they chose in each.
 *
 * <p>Ownership is stored with the rest of the player's data, so a database shared by several
 * servers shares it too. Every change works for offline players, applies to the data as stored
 * now - which another server may have changed - and completes on the main thread. Online players'
 * cosmetics are also cached, read as they join; {@link #getOwnership} answers from that cache.
 *
 * <p>Changes complete exceptionally while storage is unavailable, including while Zentrix runs on
 * its in-memory stand-in for a database it could not reach; check {@link #isAvailable()} first. A
 * change that altered anything fires
 * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticOwnershipChangeEvent}.
 *
 * <pre>{@code
 * CosmeticsService cosmetics = ZentrixAPI.get().getCosmeticsService();
 * cosmetics.grant(playerId, CosmeticCategory.GLIDERS, List.of("phoenix"))
 *     .thenAccept(change -> {
 *         if (change.changed()) {
 *             // unlocked
 *         }
 *     });
 * }</pre>
 *
 * <h2>Every type, and your own menu (since 1.11.0)</h2>
 *
 * <p>Methods taking a {@link CosmeticTypeKey} work alike for Zentrix's own categories and for
 * types addons {@linkplain #registerType register}. Together they hold everything
 * {@code /cosmetics} does, so an addon can build its own cosmetics menu, mixing in cosmetics of
 * other plugins if it likes:
 * <ul>
 *   <li>what there is: {@link #getTypes()}, {@link #getCosmetics(CosmeticTypeKey)};</li>
 *   <li>where a player stands: {@link #getStates(Player, CosmeticTypeKey)},
 *       {@link #getEquipped(Player, CosmeticTypeKey)};</li>
 *   <li>what a player does: {@link #equip}, {@link #unequip}, {@link #purchase},
 *       {@link #startPreview}, {@link #stopPreview}, each as the menu does it, with the same
 *       checks and events;</li>
 *   <li>what an addon does for a player: {@link #grant(UUID, CosmeticTypeKey, Collection) grant},
 *       {@link #grantTimed(UUID, CosmeticTypeKey, Collection, Duration) grantTimed},
 *       {@link #revoke(UUID, CosmeticTypeKey, Collection) revoke},
 *       {@link #select(UUID, CosmeticTypeKey, String) select}.</li>
 * </ul>
 *
 * <pre>{@code
 * for (CosmeticType type : cosmetics.getTypes()) {
 *     if (!type.available()) continue;
 *     for (CosmeticState state : cosmetics.getStates(player, type.key())) {
 *         // draw state.cosmetic(), marked by state.equipped(), state.canUse(), state.purchasable()
 *     }
 * }
 * // on a click:
 * cosmetics.equip(player, type.key(), cosmeticId).thenAccept(result -> {
 *     if (result.status() == CosmeticResult.Status.LOCKED) {
 *         // offer cosmetics.purchase(player, type.key(), cosmeticId)
 *     }
 * });
 * }</pre>
 *
 * <p>Methods that read or act for an online {@link Player} must be called on the main thread.
 *
 * @since 1.8.0
 */
public interface CosmeticsService {

    /** Whether cosmetics can be read and changed now. */
    boolean isAvailable();

    /** Whether an online player's cosmetics have been read since they joined. */
    boolean isLoaded(@NotNull UUID playerId);

    /**
     * An online player's cosmetics of one category, as last read. Empty ownership for offline
     * players and before the first read; use {@link #refresh(UUID)} to read them from storage.
     */
    @NotNull
    CosmeticOwnership getOwnership(@NotNull UUID playerId, @NotNull CosmeticCategory category);

    /**
     * Reads a player's cosmetics as stored now, online or offline, refreshing the cache of an
     * online player.
     *
     * @return completes on the main thread with every category
     */
    @NotNull
    CompletableFuture<Map<CosmeticCategory, CosmeticOwnership>> refresh(@NotNull UUID playerId);

    /**
     * Grants cosmetics permanently. Ones already owned permanently are left as they are; ones
     * owned for a limited time become permanent.
     */
    @NotNull
    CompletableFuture<CosmeticChange> grant(
            @NotNull UUID playerId, @NotNull CosmeticCategory category, @NotNull Collection<String> ids);

    /**
     * Grants cosmetics for a limited time, such as a glider for seven days.
     *
     * <p>A cosmetic the player does not own is granted until {@code duration} from now. One they
     * already own for a limited time keeps its remaining time with {@code duration} added. One
     * they own permanently is left as it is: a timed grant never shortens a permanent one.
     *
     * <p>When the time runs out the cosmetic is taken away on its own, even while the player is
     * offline or the server is restarting, and on every server sharing the database. A selection
     * of it falls back to the server's default, and
     * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticOwnershipChangeEvent} fires with
     * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticOwnershipChangeEvent.Cause#EXPIRED}.
     *
     * @param duration how long to grant them for; must be positive
     * @return completes on the main thread with the change
     * @throws IllegalArgumentException when the duration is zero or negative
     * @since 1.9.0
     */
    @NotNull
    default CompletableFuture<CosmeticChange> grantTimed(
            @NotNull UUID playerId, @NotNull CosmeticCategory category, @NotNull Collection<String> ids,
            @NotNull Duration duration) {
        return CompletableFuture.failedFuture(
                new UnsupportedOperationException("Timed cosmetics require Zentrix API 1.9.0"));
    }

    /** Takes cosmetics away. Taking the chosen one clears the choice, so the default is used again. */
    @NotNull
    CompletableFuture<CosmeticChange> revoke(
            @NotNull UUID playerId, @NotNull CosmeticCategory category, @NotNull Collection<String> ids);

    /** Takes every cosmetic of a category away, deleted ones included, and clears the choice. */
    @NotNull
    CompletableFuture<CosmeticChange> revokeAll(@NotNull UUID playerId, @NotNull CosmeticCategory category);

    /**
     * Chooses a player's cosmetic: one they own as stored now, or null for the server's default.
     * Choosing one they do not own changes nothing. Unlike a player's own choice in the cosmetics
     * menu, this does not fire {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticSelectEvent}.
     */
    @NotNull
    CompletableFuture<CosmeticChange> select(
            @NotNull UUID playerId, @NotNull CosmeticCategory category, @Nullable String id);

    /** The IDs of every cosmetic that currently exists in a category, in the server's order. */
    @NotNull
    List<String> getCosmeticIds(@NotNull CosmeticCategory category);

    /** The cosmetic used for players who chose none, when the category has one. */
    @NotNull
    Optional<String> getDefaultId(@NotNull CosmeticCategory category);

    // ------------------------------------------------------------------
    // Types and cosmetics (since 1.11.0)
    // ------------------------------------------------------------------

    /**
     * Every cosmetic type: Zentrix's own categories in the cosmetics menu's order, then the types
     * addons registered, in the order they did. Built-in types missing from this edition are
     * listed with {@link CosmeticType#available()} false.
     *
     * @since 1.11.0
     */
    @NotNull
    List<CosmeticType> getTypes();

    /**
     * The type a key names, while it exists.
     *
     * @since 1.11.0
     */
    @NotNull
    Optional<CosmeticType> getType(@NotNull CosmeticTypeKey type);

    /**
     * Every cosmetic of a type, in the server's order, with what menus show of them. Empty for a
     * type that does not exist or is not available.
     *
     * @since 1.11.0
     */
    @NotNull
    List<Cosmetic> getCosmetics(@NotNull CosmeticTypeKey type);

    /**
     * One cosmetic of a type, while it exists.
     *
     * @since 1.11.0
     */
    @NotNull
    Optional<Cosmetic> getCosmetic(@NotNull CosmeticTypeKey type, @NotNull String id);

    // ------------------------------------------------------------------
    // Where a player stands (since 1.11.0)
    // ------------------------------------------------------------------

    /**
     * An online player's cosmetics of one type, as last read, as
     * {@link #getOwnership(UUID, CosmeticCategory)} answers for a built-in one.
     *
     * @since 1.11.0
     */
    @NotNull
    CosmeticOwnership getOwnership(@NotNull UUID playerId, @NotNull CosmeticTypeKey type);

    /**
     * Where an online player stands with one cosmetic, as last read.
     *
     * @return empty when the type or cosmetic does not exist
     * @since 1.11.0
     */
    @NotNull
    Optional<CosmeticState> getState(@NotNull Player player, @NotNull CosmeticTypeKey type, @NotNull String id);

    /**
     * Where an online player stands with every cosmetic of a type, in the server's order, as last
     * read. Every cosmetic is listed, also those the player can neither use nor buy; Zentrix's own
     * menu hides those for most types.
     *
     * @since 1.11.0
     */
    @NotNull
    List<CosmeticState> getStates(@NotNull Player player, @NotNull CosmeticTypeKey type);

    /**
     * The cosmetic in effect for an online player: the one they chose while they can still use it,
     * otherwise the type's default.
     *
     * @return empty when they chose none and the type has no default
     * @since 1.11.0
     */
    @NotNull
    Optional<Cosmetic> getEquipped(@NotNull Player player, @NotNull CosmeticTypeKey type);

    // ------------------------------------------------------------------
    // Changes for any player (since 1.11.0)
    // ------------------------------------------------------------------

    /**
     * Unlocks cosmetics of any type permanently, as {@link #grant(UUID, CosmeticCategory, Collection)}
     * does. A {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticUnlockEvent} listener may
     * cancel it, leaving the change empty; the 1.8.0 overload fires that event too.
     *
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticChange> grant(
            @NotNull UUID playerId, @NotNull CosmeticTypeKey type, @NotNull Collection<String> ids);

    /**
     * Unlocks cosmetics of any type for a limited time, as
     * {@link #grantTimed(UUID, CosmeticCategory, Collection, Duration)} does, firing
     * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticUnlockEvent} first.
     *
     * @throws IllegalArgumentException when the duration is zero or negative
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticChange> grantTimed(
            @NotNull UUID playerId, @NotNull CosmeticTypeKey type, @NotNull Collection<String> ids,
            @NotNull Duration duration);

    /**
     * Takes cosmetics of any type away, as {@link #revoke(UUID, CosmeticCategory, Collection)} does.
     *
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticChange> revoke(
            @NotNull UUID playerId, @NotNull CosmeticTypeKey type, @NotNull Collection<String> ids);

    /**
     * Takes every cosmetic of any type away and clears the choice, as
     * {@link #revokeAll(UUID, CosmeticCategory)} does.
     *
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticChange> revokeAll(@NotNull UUID playerId, @NotNull CosmeticTypeKey type);

    /**
     * Stores a player's choice in any type without the checks and events of a player's own choice,
     * as {@link #select(UUID, CosmeticCategory, String)} does: one they own as stored now, or null
     * for the default. For a class, or an addon's cosmetic that is unlocked by default, ownership is
     * not needed.
     *
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticChange> select(
            @NotNull UUID playerId, @NotNull CosmeticTypeKey type, @Nullable String id);

    // ------------------------------------------------------------------
    // A player's own actions (since 1.11.0)
    // ------------------------------------------------------------------

    /**
     * Equips a cosmetic as the player's own choice, exactly as a left-click in {@code /cosmetics}
     * does: the player must be able to use it, and
     * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticSelectEvent} (built-in categories)
     * and {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticEquipEvent} may refuse it.
     * Main thread.
     *
     * @return completes on the main thread: {@code SUCCESS}, {@code UNCHANGED} when it is already
     *         equipped, {@code UNKNOWN}, {@code LOCKED}, {@code CANCELLED}, or {@code UNAVAILABLE}
     *         and {@code FAILED} when it could not be saved
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticResult> equip(@NotNull Player player, @NotNull CosmeticTypeKey type, @NotNull String id);

    /**
     * Clears the player's choice in a type, so its default is equipped, as choosing "Server
     * Default" in {@code /cosmetics} does, with the same events as {@link #equip}. Main thread.
     *
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticResult> unequip(@NotNull Player player, @NotNull CosmeticTypeKey type);

    /**
     * Buys a cosmetic for the player with Zentrix's currency and unlocks it permanently, as
     * {@code /cosmetics} does for an addon's cosmetic with a price. Only cosmetics that are for
     * sale and the player cannot use yet can be bought.
     * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticUnlockEvent} is fired before the
     * payment, which then goes through the currency's usual
     * {@link dev.itsharshxd.zentrix.api.events.currency.CurrencyChangeEvent}. Should storing the
     * cosmetic fail after the payment, the payment is refunded. Main thread.
     *
     * @return completes on the main thread: {@code SUCCESS}, {@code UNKNOWN},
     *         {@code ALREADY_UNLOCKED}, {@code NOT_FOR_SALE}, {@code INSUFFICIENT_FUNDS},
     *         {@code CANCELLED}, {@code UNAVAILABLE} or {@code FAILED}
     * @since 1.11.0
     */
    @NotNull
    CompletableFuture<CosmeticResult> purchase(
            @NotNull Player player, @NotNull CosmeticTypeKey type, @NotNull String id);

    /**
     * Previews a cosmetic to the player alone, as a right-click in {@code /cosmetics} does: a
     * victory effect plays, a glider shows on their back, a cage design around them, and an
     * addon's type shows what its handler shows. Ends any preview already showing.
     * {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticPreviewEvent} may refuse it. A
     * preview does not need the player to own the cosmetic. Classes have no previews. Main thread.
     *
     * @return whether the preview shows
     * @since 1.11.0
     */
    boolean startPreview(@NotNull Player player, @NotNull CosmeticTypeKey type, @NotNull String id);

    /**
     * Ends the player's cosmetic preview, of whatever type, if one shows. Main thread.
     *
     * @since 1.11.0
     */
    void stopPreview(@NotNull Player player);

    // ------------------------------------------------------------------
    // Custom types (since 1.11.0)
    // ------------------------------------------------------------------

    /**
     * Adds a cosmetic type of the addon's own, under {@code CosmeticTypeKey.of(owner,
     * definition.getKey())}. It works as Zentrix's own categories do:
     * <ul>
     *   <li>{@code /cosmetics} shows a tile for it, opening the cosmetics the player can use or
     *       buy; left-click equips or buys, right-click previews;</li>
     *   <li>ownership and choices are stored with the player's data, in every storage backend,
     *       and kept while the type is not registered;</li>
     *   <li>a cosmetic's permission unlocks it, and one with a price can be bought;</li>
     *   <li>the handler hears each player's equipped cosmetic at the moments the definition
     *       lists, and shows previews.</li>
     * </ul>
     *
     * <p>The registration ends when the owner is disabled; ending it stops previews of the type,
     * calls {@link CosmeticHandler#onUnregister()} and takes the tile out of {@code /cosmetics}.
     * Not part of the Free edition, which refuses it. Main thread.
     *
     * @throws IllegalArgumentException when the owner is not enabled or the key is already
     *                                  registered
     * @throws UnsupportedOperationException in the Free edition
     * @since 1.11.0
     */
    @NotNull
    Registration registerType(@NotNull Plugin owner, @NotNull CosmeticTypeDefinition definition,
                              @NotNull CosmeticHandler handler);
}
