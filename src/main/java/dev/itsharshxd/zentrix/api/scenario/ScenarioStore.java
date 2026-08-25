package dev.itsharshxd.zentrix.api.scenario;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A scenario's scratch space for one match.
 *
 * <p>Everything stored here is private to a single scenario in a single game and is discarded when
 * that match ends, which is what lets the same scenario run in several matches at once without any
 * of them seeing each other's state.
 *
 * <p>The store is thread-safe.
 *
 * @since 1.7.0
 */
public interface ScenarioStore {

    /** The stored value, empty when absent or of a different type. */
    @NotNull
    <T> Optional<T> get(@NotNull String key, @NotNull Class<T> type);

    /** The stored value, or {@code fallback} when absent or of a different type. */
    <T> T getOrDefault(@NotNull String key, @NotNull Class<T> type, T fallback);

    /** The stored value, creating and storing one through {@code factory} when absent. */
    @NotNull
    <T> T computeIfAbsent(@NotNull String key, @NotNull Class<T> type, @NotNull Supplier<T> factory);

    /** Stores a value; a null value removes the key. */
    void set(@NotNull String key, @Nullable Object value);

    /** Removes a key and reports whether anything was stored under it. */
    boolean remove(@NotNull String key);

    boolean contains(@NotNull String key);

    @NotNull
    Set<String> keys();

    /** Removes everything. Cleanup does this automatically. */
    void clear();

    // ==========================================
    // Per-player convenience
    // ==========================================

    /** Reads a value stored against one participant. */
    @NotNull
    <T> Optional<T> getPlayer(@NotNull UUID playerId, @NotNull String key, @NotNull Class<T> type);

    /** Stores a value against one participant; a null value removes it. */
    void setPlayer(@NotNull UUID playerId, @NotNull String key, @Nullable Object value);

    /**
     * Drops everything stored against one participant.
     *
     * <p>Useful when a player leaves mid-match; the store does not do this on its own, because a
     * scenario may want a disconnecting player's state to survive a reconnect.
     */
    void clearPlayer(@NotNull UUID playerId);
}
