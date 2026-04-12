package dev.itsharshxd.zentrix.api.gametype;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a read-only view of a game type configuration.
 * <p>
 * Game types define different modes of play (e.g., Solos, Duos, Squads)
 * with their own team sizes, player limits, and scoreboards.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * GameTypeService service = ZentrixAPI.get().getGameTypeService();
 *
 * Optional<ZentrixGameType> gameType = service.getGameType("trios");
 * gameType.ifPresent(gt -> {
 *     System.out.println("Game Type: " + gt.getName());
 *     System.out.println("Team Size: " + gt.getTeamSize());
 *     System.out.println("Players: " + gt.getMinimumPlayers() + "-" + gt.getMaximumPlayers());
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public interface ZentrixGameType {

    /**
     * Gets the name of this game type.
     * <p>
     * This is the unique identifier used in configuration and selection.
     * </p>
     *
     * @return The game type name (never null)
     */
    @NotNull
    String getName();

    /**
     * Gets the team size for this game type.
     * <p>
     * For Solos, this is 1. For Duos, 2. For Squads, typically 4.
     * </p>
     *
     * @return The team size
     */
    int getTeamSize();

    /**
     * Gets the minimum number of players required to start a game.
     *
     * @return The minimum player count
     */
    int getMinimumPlayers();

    /**
     * Gets the maximum number of players allowed in a game.
     *
     * @return The maximum player count
     */
    int getMaximumPlayers();

    /**
     * Gets the start time countdown duration in seconds.
     * <p>
     * This is how long players wait after minimum players are reached
     * before the game starts.
     * </p>
     *
     * @return The start time in seconds
     */
    int getStartTime();

    // ==========================================
    // Scoreboard Information
    // ==========================================

    /**
     * Checks if this game type has any scoreboards configured.
     *
     * @return {@code true} if at least one state has a scoreboard
     */
    boolean hasScoreboards();

    /**
     * Checks if a specific game state has a scoreboard configured.
     *
     * @param state The game state to check
     * @return {@code true} if a scoreboard is configured for that state
     */
    boolean hasScoreboardForState(@NotNull GameTypeState state);

    /**
     * Gets the scoreboard title for a specific game state.
     *
     * @param state The game state
     * @return Optional containing the title, or empty if no scoreboard for that state
     */
    @NotNull
    Optional<String> getScoreboardTitle(@NotNull GameTypeState state);

    /**
     * Gets the scoreboard lines for a specific game state.
     *
     * @param state The game state
     * @return An unmodifiable list of lines (never null, may be empty)
     */
    @NotNull
    List<String> getScoreboardLines(@NotNull GameTypeState state);

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Gets the addon ID that registered this game type.
     *
     * @return Optional containing the addon ID, or empty if not addon-registered
     */
    @NotNull
    Optional<String> getAddonId();

    /**
     * Checks if this game type was dynamically registered by an addon.
     * <p>
     * Built-in game types (from configuration) return {@code false}.
     * </p>
     *
     * @return {@code true} if dynamically registered
     */
    boolean isDynamicallyRegistered();

    /**
     * Checks if this is a built-in game type from the configuration.
     *
     * @return {@code true} if this is a built-in type
     */
    default boolean isBuiltIn() {
        return !isDynamicallyRegistered();
    }

    /**
     * Gets custom metadata fields for this game type.
     *
     * @return An unmodifiable map of custom fields (never null, may be empty)
     */
    @NotNull
    Map<String, Object> getCustomFields();

    /**
     * Enum representing game states for scoreboard selection.
     */
    enum GameTypeState {
        /**
         * The waiting state while players are joining.
         */
        WAITING,

        /**
         * The starting state during countdown.
         */
        STARTING,

        /**
         * The playing state during active gameplay.
         */
        PLAYING,

        /**
         * The win state when a winner has been determined.
         */
        WIN
    }
}
