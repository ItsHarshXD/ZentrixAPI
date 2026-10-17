package dev.itsharshxd.zentrix.api.game;

import dev.itsharshxd.zentrix.api.phase.GamePhase;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import dev.itsharshxd.zentrix.api.team.CompetingSide;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Read-only view of a Zentrix Battle Royale game instance.
 * <p>
 * It exposes game state, players, teams, and other game information. Addons
 * cannot modify game state through this interface; use events and services.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * ZentrixGame game = ZentrixProvider.get().getGameService()
 *     .getPlayerGame(player).orElse(null);
 *
 * if (game != null) {
 *     // Read game information
 *     String arena = game.getArenaName();
 *     int playerCount = game.getPlayerCount();
 *     GameState state = game.getState();
 *
 *     // Check the phase
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
     * This legacy ID combines the copied arena name and object hash. Prefer
     * {@link #getRuntimeId()} for the canonical copied {@code game-*} ID.
     * </p>
     *
     * @return The unique game ID (never null)
     */
    @NotNull
    String getGameId();

    /** Canonical copied runtime identifier (normally {@code game-*}). */
    @NotNull
    default String getRuntimeId() {
        return getArenaName();
    }

    /** Source/template arena used to create this runtime game, when known. */
    @NotNull
    default Optional<String> getSourceArenaName() {
        return Optional.empty();
    }

    /**
     * Gets the arena name this game is running on.
     * <p>
     * This legacy value is the copied runtime arena name. Use
     * {@link #getSourceArenaName()} for the source/template arena.
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
     * <p>
     * Legacy semantics, kept unchanged: in a match played without teams this counts the players
     * still playing, as each is their own side, even though {@link #getAliveTeams()} is empty
     * there. It is therefore the number of alive sides, not of alive teams. Use
     * {@link #getAliveSideCount()} for sides and {@code getAliveTeams().size()} for teams.
     * </p>
     *
     * @return The count of alive sides
     * @deprecated since 1.8.0; ambiguous in solo matches. Use {@link #getAliveSideCount()}.
     */
    @Deprecated(since = "1.8.0")
    int getAliveTeamCount();

    /**
     * Whether this match is played in teams.
     * <p>
     * Settled when the match starts and fixed for the rest of it; before that it follows the
     * current configuration of the game type and of {@code arena-management.solo-teams}.
     * </p>
     *
     * @return {@code true} when players compete as teams
     * @since 1.8.0
     */
    default boolean isTeamMode() {
        return getTeamSize() > 1;
    }

    /**
     * Whether {@link #isTeamMode()} is fixed for this match, which it is from the start on.
     *
     * @return {@code true} once the match's team mode can no longer change
     * @since 1.8.0
     */
    default boolean isTeamModeLocked() {
        return isPlaying();
    }

    /**
     * Gets every side competing in this match: its teams in team mode, otherwise its players.
     * <p>
     * In team mode every team of the match is included, eliminated ones too. Without teams the
     * sides are the players still playing, followed by the players this match eliminated who are
     * still watching it; a player who has left is no longer listed. The list is a snapshot.
     * </p>
     *
     * @return The sides (never null, may be empty)
     * @since 1.8.0
     */
    @NotNull
    default List<CompetingSide> getCompetingSides() {
        return List.of();
    }

    /**
     * Gets the number of sides still competing: alive teams in team mode, otherwise players still
     * playing. This is the count win conditions use.
     *
     * @return The count of alive sides
     * @since 1.8.0
     */
    @SuppressWarnings("deprecation")
    default int getAliveSideCount() {
        return getAliveTeamCount();
    }

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
