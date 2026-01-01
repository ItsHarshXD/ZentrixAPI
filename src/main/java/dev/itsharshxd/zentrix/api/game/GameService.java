package dev.itsharshxd.zentrix.api.game;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for game management and query operations.
 * <p>
 * This service provides read-only access to game state and information.
 * Use the events system to react to game state changes.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * GameService gameService = ZentrixProvider.get().getGameService();
 *
 * // Get all active games
 * Collection<ZentrixGame> games = gameService.getActiveGames();
 *
 * // Check if player is in a game
 * if (gameService.isInGame(player)) {
 *     ZentrixGame game = gameService.getPlayerGame(player).orElseThrow();
 *     // Do something with the game
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface GameService {

    /**
     * Gets all currently active games.
     * <p>
     * Active games include games in any state: WAITING, STARTING, PLAYING, ENDING, or RESTARTING.
     * </p>
     *
     * @return An unmodifiable collection of all active games (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixGame> getActiveGames();

    /**
     * Gets a game by its unique identifier.
     *
     * @param gameId The game's unique ID
     * @return Optional containing the game, or empty if not found
     */
    @NotNull
    Optional<ZentrixGame> getGame(@NotNull String gameId);

    /**
     * Gets the game a player is currently in.
     * <p>
     * This includes both active players and spectators.
     * </p>
     *
     * @param player The player to check
     * @return Optional containing the game, or empty if player is not in any game
     */
    @NotNull
    Optional<ZentrixGame> getPlayerGame(@NotNull Player player);

    /**
     * Gets the game a player is currently in by UUID.
     * <p>
     * This includes both active players and spectators.
     * </p>
     *
     * @param playerId The player's UUID
     * @return Optional containing the game, or empty if player is not in any game
     */
    @NotNull
    Optional<ZentrixGame> getPlayerGame(@NotNull UUID playerId);

    /**
     * Checks if a player is currently in any game.
     * <p>
     * This returns true for both active players and spectators.
     * </p>
     *
     * @param player The player to check
     * @return {@code true} if the player is in a game (playing or spectating)
     */
    boolean isInGame(@NotNull Player player);

    /**
     * Checks if a player is currently in any game by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is in a game (playing or spectating)
     */
    boolean isInGame(@NotNull UUID playerId);

    /**
     * Checks if a player is spectating a game.
     * <p>
     * Spectators are players who have been eliminated but are still
     * watching the game, or players who joined as spectators.
     * </p>
     *
     * @param player The player to check
     * @return {@code true} if the player is spectating a game
     */
    boolean isSpectating(@NotNull Player player);

    /**
     * Checks if a player is spectating a game by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is spectating a game
     */
    boolean isSpectating(@NotNull UUID playerId);

    /**
     * Checks if a player is actively playing (alive) in a game.
     * <p>
     * This returns {@code true} only for alive players, not spectators.
     * </p>
     *
     * @param player The player to check
     * @return {@code true} if the player is alive in a game
     */
    boolean isPlaying(@NotNull Player player);

    /**
     * Checks if a player is actively playing (alive) in a game by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is alive in a game
     */
    boolean isPlaying(@NotNull UUID playerId);

    /**
     * Gets the total number of active games.
     *
     * @return The count of active games
     */
    int getActiveGameCount();

    /**
     * Gets all games running on a specific arena.
     * <p>
     * Multiple game instances can run on the same arena template.
     * </p>
     *
     * @param arenaName The arena name
     * @return Collection of games for that arena (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixGame> getGamesForArena(@NotNull String arenaName);

    /**
     * Gets all games in a specific state.
     *
     * @param state The game state to filter by
     * @return Collection of games in that state (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixGame> getGamesByState(@NotNull ZentrixGame.GameState state);

    /**
     * Gets the total number of players across all active games.
     * <p>
     * This includes only alive players, not spectators.
     * </p>
     *
     * @return Total player count across all games
     */
    int getTotalPlayerCount();

    /**
     * Gets all available arena names.
     *
     * @return Collection of arena names (never null, may be empty)
     */
    @NotNull
    Collection<String> getAvailableArenas();
}
