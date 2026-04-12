package dev.itsharshxd.zentrix.api.broadcast;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a read-only view of a broadcast configuration.
 * <p>
 * Broadcasts are periodic messages sent to players in specific game states.
 * They support different formats including chat messages, titles, and action bars.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * BroadcastService service = ZentrixAPI.get().getBroadcastService();
 *
 * Optional<ZentrixBroadcast> broadcast = service.getBroadcast("tips");
 * broadcast.ifPresent(b -> {
 *     System.out.println("Broadcast: " + b.getId());
 *     System.out.println("Type: " + b.getType());
 *     System.out.println("Interval: " + b.getInterval() + "s");
 *     System.out.println("Enabled: " + b.isEnabled());
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public interface ZentrixBroadcast {

    /**
     * Gets the unique identifier for this broadcast.
     *
     * @return The broadcast ID (never null)
     */
    @NotNull
    String getId();

    /**
     * Checks if this broadcast is enabled.
     *
     * @return {@code true} if the broadcast is enabled
     */
    boolean isEnabled();

    /**
     * Gets the interval between broadcasts in seconds.
     *
     * @return The interval in seconds
     */
    int getInterval();

    /**
     * Gets the game states in which this broadcast is shown.
     *
     * @return An unmodifiable set of game states (never null, may be empty)
     */
    @NotNull
    Set<GameState> getShowInStates();

    /**
     * Checks if this broadcast should be shown in the given state.
     *
     * @param state The game state to check
     * @return {@code true} if the broadcast is shown in this state
     */
    boolean showsInState(@NotNull GameState state);

    /**
     * Gets the type of this broadcast.
     *
     * @return The broadcast type (never null)
     */
    @NotNull
    BroadcastType getType();

    // ==========================================
    // CHAT Type Properties
    // ==========================================

    /**
     * Gets the messages for a CHAT type broadcast.
     * <p>
     * For CHAT broadcasts, messages are rotated or randomly selected.
     * </p>
     *
     * @return An unmodifiable list of messages (never null, may be empty for non-CHAT types)
     */
    @NotNull
    List<String> getMessages();

    // ==========================================
    // TITLE Type Properties
    // ==========================================

    /**
     * Gets the title text for a TITLE type broadcast.
     *
     * @return Optional containing the title, or empty if not a TITLE type
     */
    @NotNull
    Optional<String> getTitle();

    /**
     * Gets the subtitle text for a TITLE type broadcast.
     *
     * @return Optional containing the subtitle, or empty if not set
     */
    @NotNull
    Optional<String> getSubtitle();

    /**
     * Gets the fade-in time for TITLE broadcasts in ticks.
     *
     * @return The fade-in time (default 10)
     */
    int getFadeIn();

    /**
     * Gets the stay time for TITLE broadcasts in ticks.
     *
     * @return The stay time (default 70)
     */
    int getStay();

    /**
     * Gets the fade-out time for TITLE broadcasts in ticks.
     *
     * @return The fade-out time (default 20)
     */
    int getFadeOut();

    // ==========================================
    // ACTIONBAR Type Properties
    // ==========================================

    /**
     * Gets the action bar message for an ACTIONBAR type broadcast.
     *
     * @return Optional containing the action bar message, or empty if not an ACTIONBAR type
     */
    @NotNull
    Optional<String> getActionBar();

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Gets the addon ID that registered this broadcast.
     *
     * @return Optional containing the addon ID, or empty if not addon-registered
     */
    @NotNull
    Optional<String> getAddonId();

    /**
     * Checks if this broadcast was dynamically registered by an addon.
     *
     * @return {@code true} if dynamically registered
     */
    boolean isDynamicallyRegistered();

    /**
     * Gets custom metadata fields for this broadcast.
     *
     * @return An unmodifiable map of custom fields (never null, may be empty)
     */
    @NotNull
    Map<String, Object> getCustomFields();
}
