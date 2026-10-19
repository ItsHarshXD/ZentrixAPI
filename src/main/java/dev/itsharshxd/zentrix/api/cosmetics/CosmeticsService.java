package dev.itsharshxd.zentrix.api.cosmetics;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
}
