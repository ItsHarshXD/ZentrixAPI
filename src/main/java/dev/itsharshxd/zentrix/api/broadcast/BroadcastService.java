package dev.itsharshxd.zentrix.api.broadcast;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Manages broadcasts within Zentrix.
 * <p>
 * Broadcasts are periodic messages sent to players in specific game states.
 * The service registers, queries, and controls them.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * BroadcastService broadcastService = ZentrixAPI.get().getBroadcastService();
 *
 * // Register a broadcast
 * BroadcastBuilder tips = new BroadcastBuilder()
 *     .id("gameplay-tips")
 *     .type(BroadcastType.CHAT)
 *     .interval(180)
 *     .showIn(GameState.PLAYING)
 *     .messages(
 *         "&#FFD700[TIP] Use sneak to reduce knockback!",
 *         "&#FFD700[TIP] Golden apples give absorption!"
 *     )
 *     .addonId(getAddonId());
 *
 * broadcastService.registerBroadcast(tips);
 *
 * // List broadcasts
 * Collection<ZentrixBroadcast> playingBroadcasts =
 *     broadcastService.getBroadcastsForState(GameState.PLAYING);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public interface BroadcastService {

    // ==========================================
    // Registration
    // ==========================================

    /**
     * Registers a custom broadcast from a builder.
     * <p>
     * The registration is kept in memory only.
     * </p>
     *
     * @param builder The broadcast builder with configuration
     * @return {@code true} if registration was successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    boolean registerBroadcast(@NotNull BroadcastBuilder builder);

    /**
     * Registers a custom broadcast asynchronously with persistence.
     * <p>
     * The broadcast is registered and saved to {@code broadcasts.yml}.
     * </p>
     *
     * @param builder The broadcast builder with configuration
     * @return A future that completes with {@code true} if successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    @NotNull
    CompletableFuture<Boolean> registerBroadcastAsync(@NotNull BroadcastBuilder builder);

    /**
     * Unregisters a broadcast by ID.
     * <p>
     * The removal affects memory only.
     * </p>
     *
     * @param broadcastId The ID of the broadcast to unregister
     * @return {@code true} if the broadcast was found and removed
     */
    boolean unregisterBroadcast(@NotNull String broadcastId);

    /**
     * Updates an existing broadcast with new configuration.
     * <p>
     * The broadcast must already exist.
     * </p>
     *
     * @param builder The broadcast builder with updated configuration
     * @return {@code true} if the broadcast was found and updated
     */
    boolean updateBroadcast(@NotNull BroadcastBuilder builder);

    // ==========================================
    // Queries
    // ==========================================

    /**
     * Gets a broadcast by its ID.
     *
     * @param broadcastId The broadcast ID
     * @return Optional containing the broadcast, or empty if not found
     */
    @NotNull
    Optional<ZentrixBroadcast> getBroadcast(@NotNull String broadcastId);

    /**
     * Gets all registered broadcasts.
     *
     * @return An unmodifiable collection of all broadcasts (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixBroadcast> getAllBroadcasts();

    /**
     * Gets all broadcasts that should be shown in a specific game state.
     *
     * @param state The game state to filter by
     * @return Collection of broadcasts for that state (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixBroadcast> getBroadcastsForState(@NotNull GameState state);

    /**
     * Gets all broadcasts registered by a specific addon.
     *
     * @param addonId The addon identifier
     * @return Collection of broadcasts registered by the addon (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixBroadcast> getBroadcastsByAddon(@NotNull String addonId);

    /**
     * Checks if a broadcast exists with the given ID.
     *
     * @param broadcastId The broadcast ID to check
     * @return {@code true} if the broadcast exists
     */
    boolean broadcastExists(@NotNull String broadcastId);

    // ==========================================
    // Control
    // ==========================================

    /**
     * Enables a broadcast.
     *
     * @param broadcastId The broadcast ID to enable
     * @return {@code true} if the broadcast was found and enabled
     */
    boolean enableBroadcast(@NotNull String broadcastId);

    /**
     * Disables a broadcast.
     *
     * @param broadcastId The broadcast ID to disable
     * @return {@code true} if the broadcast was found and disabled
     */
    boolean disableBroadcast(@NotNull String broadcastId);

    /**
     * Triggers a broadcast immediately for the given game state.
     * <p>
     * This bypasses the interval timer and sends the broadcast immediately.
     * </p>
     *
     * @param broadcastId The broadcast ID to trigger
     * @param state       The game state context for the broadcast
     * @return {@code true} if the broadcast was found and triggered
     */
    boolean triggerBroadcastNow(@NotNull String broadcastId, @NotNull GameState state);

    /**
     * Resets a broadcast's timer.
     * <p>
     * The next broadcast will occur after the full interval.
     * </p>
     *
     * @param broadcastId The broadcast ID
     */
    void resetBroadcastTimer(@NotNull String broadcastId);

    // ==========================================
    // Convenience methods
    // ==========================================

    /**
     * Creates a {@link BroadcastBuilder}.
     *
     * @return A new BroadcastBuilder
     */
    @NotNull
    default BroadcastBuilder createBroadcastBuilder() {
        return new BroadcastBuilder();
    }
}
