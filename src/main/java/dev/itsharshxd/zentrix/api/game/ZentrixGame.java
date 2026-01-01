package dev.itsharshxd.zentrix.api.game;

import dev.itsharshxd.zentrix.api.phase.GamePhase;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a read-only view of a Zentrix Battle Royale game instance.
 * <p>
 * This interface provides access to game state, players, teams, and other
 * game-related information. Addons cannot directly modify game state through
 * this interface - use events and services instead.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ZentrixGame game = ZentrixProvider.get().getGameService()
 *     .getPlayerGame(player).orElse(null);
 *
 * if (game != null) {
 *     // Get game information
 *     String arena = game.getArenaName();
 *     int playerCount = game.getPlayerCount();
 *     GameState state = game.getState();
 *
 *     // Check phase
 *     game.getCurrentPhase().ifPresent(phase -> {
 *         String phaseName = phase.getName();
 *         int timeLeft = phase.getTimeRemaining();
 *     });
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ZentrixGame {

    /**
     * Gets the unique identifier for this game instance.
     * <p>
     * This ID is unique across all active games and is typically
     * a combination of arena name and instance number.
     * </p>
     *
     * @return The unique game ID (never null)
     */
    @NotNull
    String getGameId();

    /**
     * Gets the arena name this game is running on.
     * <p>
     * Multiple game instances can run on copies of the same arena.
     * </p>
     *
     * @return The arena name (never null)
     */
    @NotNull
    String getArenaName();

    /**
     * Gets the current state of this game.
     *
     * @return The current game state (never null)
     */
    @NotNull
    GameState getState();

    /**
     * Checks if the game is currently in the playing state.
     *
     * @return {@code true} if the game state is {@link GameState#PLAYING}
     */
    default boolean isPlaying() {
        return getState() == GameState.PLAYING;
    }

    /**
     * Checks if the game is waiting for players.
     *
     * @return {@code true} if the game state is {@link GameState#WAITING}
     */
    default boolean isWaiting() {
        return getState() == GameState.WAITING;
    }

    /**
     * Gets all players currently alive in this game.
     * <p>
     * This does not include spectators or eliminated players.
     * </p>
     *
     * @return An unmodifiable collection of alive players (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixPlayer> getPlayers();

    /**
     * Gets all spectators watching this game.
     * <p>
     * Spectators include eliminated players who stayed to watch
     * and players who joined specifically to spectate.
     * </p>
     *
     * @return An unmodifiable collection of spectators (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixPlayer> getSpectators();

    /**
     * Gets all participants in this game (players and spectators).
     *
     * @return An unmodifiable collection of all participants (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixPlayer> getAllParticipants();

    /**
     * Gets a player by their UUID.
     *
     * @param playerId The player's UUID
     * @return Optional containing the player, or empty if not found
     */
    @NotNull
    Optional<ZentrixPlayer> getPlayer(@NotNull UUID playerId);

    /**
     * Checks if a player is in this game (playing or spectating).
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is in this game
     */
    boolean hasPlayer(@NotNull UUID playerId);

    /**
     * Gets the number of alive players in this game.
     *
     * @return The count of alive players
     */
    int getPlayerCount();

    /**
     * Gets the number of spectators in this game.
     *
     * @return The count of spectators
     */
    int getSpectatorCount();

    /**
     * Gets all teams in this game.
     *
     * @return An unmodifiable collection of teams (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixTeam> getTeams();

    /**
     * Gets teams that still have alive members.
     *
     * @return An unmodifiable collection of alive teams (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixTeam> getAliveTeams();

    /**
     * Gets a team by its ID.
     *
     * @param teamId The team ID (e.g., "team-1")
     * @return Optional containing the team, or empty if not found
     */
    @NotNull
    Optional<ZentrixTeam> getTeam(@NotNull String teamId);

    /**
     * Gets the number of alive teams.
     *
     * @return The count of teams with at least one alive member
     */
    int getAliveTeamCount();

    /**
     * Gets the current game phase, if the game is playing.
     * <p>
     * Returns empty if the game hasn't started or has ended.
     * </p>
     *
     * @return Optional containing the current phase, or empty if not in a phase
     */
    @NotNull
    Optional<GamePhase> getCurrentPhase();

    /**
     * Gets the game world.
     * <p>
     * May return null if the world hasn't been created yet
     * (e.g., during WAITING state before arena copy).
     * </p>
     *
     * @return The game world, or null if not yet available
     */
    @Nullable
    World getWorld();

    /**
     * Gets the game type name (e.g., "solo", "duo", "squad").
     *
     * @return The game type name (never null)
     */
    @NotNull
    String getGameTypeName();

    /**
     * Gets the maximum number of players for this game type.
     *
     * @return The maximum player capacity
     */
    int getMaxPlayers();

    /**
     * Gets the minimum number of players required to start.
     *
     * @return The minimum player count
     */
    int getMinPlayers();

    /**
     * Gets the team size for this game type.
     * <p>
     * Returns 1 for solo games.
     * </p>
     *
     * @return The team size
     */
    int getTeamSize();

    /**
     * Gets the time remaining in the current phase, in seconds.
     * <p>
     * Returns 0 if no phase is active.
     * </p>
     *
     * @return Seconds remaining in current phase
     */
    int getPhaseTimeRemaining();

    /**
     * Gets the total game duration so far, in seconds.
     * <p>
     * Starts counting from when the game enters PLAYING state.
     * </p>
     *
     * @return Game duration in seconds, or 0 if game hasn't started
     */
    long getGameDuration();

    /**
     * Gets the current world border size.
     * <p>
     * Returns 0 if the world is not available.
     * </p>
     *
     * @return The world border diameter in blocks
     */
    double getWorldBorderSize();

    /**
     * Sends a message to all players and spectators in this game.
     * <p>
     * Supports MiniMessage format and legacy color codes.
     * </p>
     *
     * @param message The message to send
     */
    void broadcast(@NotNull String message);

    /**
     * Sends a localized message to all players and spectators.
     * <p>
     * Uses Zentrix's locale system with placeholder support.
     * </p>
     *
     * @param key          The locale key
     * @param placeholders Placeholder pairs (key, value, key, value, ...)
     */
    void broadcastLocalized(@NotNull String key, @NotNull Object... placeholders);

    /**
     * Represents the possible states of a Zentrix game.
     */
    enum GameState {
        /**
         * Game is waiting for players to join.
         * Players can join during this state.
         */
        WAITING,

        /**
         * Game has enough players and countdown has started.
         * Players can still join during this state.
         */
        STARTING,

        /**
         * Game is actively being played.
         * Players cannot join (only spectate).
         */
        PLAYING,

        /**
         * Game has ended and winner is being announced.
         * Transitioning to cleanup.
         */
        ENDING,

        /**
         * Game is restarting/cleaning up.
         * Arena is being reset.
         */
        RESTARTING
    }
}
