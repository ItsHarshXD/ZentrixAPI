package dev.itsharshxd.zentrix.api.player;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides player queries for Zentrix games.
 * <p>
 * Use it to inspect player state, statistics, and team membership.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * PlayerService playerService = ZentrixProvider.get().getPlayerService();
 *
 * // Get the player's game representation
 * Optional<ZentrixPlayer> zentrixPlayer = playerService.getPlayer(player);
 *
 * zentrixPlayer.ifPresent(zp -> {
 *     int kills = zp.getGameKills();
 *     boolean alive = zp.isAlive();
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface PlayerService {

    /**
     * Gets a player's Zentrix representation if they are in a game.
     *
     * @param player The Bukkit player
     * @return Optional containing the ZentrixPlayer, or empty if not in a game
     */
    @NotNull
    Optional<ZentrixPlayer> getPlayer(@NotNull Player player);

    /**
     * Gets a player's Zentrix representation by UUID if they are in a game.
     *
     * @param playerId The player's UUID
     * @return Optional containing the ZentrixPlayer, or empty if not in a game
     */
    @NotNull
    Optional<ZentrixPlayer> getPlayer(@NotNull UUID playerId);

    /**
     * Gets a player within a specific game.
     *
     * @param game     The game to search in
     * @param playerId The player's UUID
     * @return Optional containing the ZentrixPlayer, or empty if not found
     */
    @NotNull
    Optional<ZentrixPlayer> getPlayer(@NotNull ZentrixGame game, @NotNull UUID playerId);

    /**
     * Gets all players currently in any game (alive only).
     *
     * @return An unmodifiable collection of all players in games (never null)
     */
    @NotNull
    Collection<ZentrixPlayer> getAllPlayers();

    /**
     * Gets all spectators currently watching any game.
     *
     * @return An unmodifiable collection of all spectators (never null)
     */
    @NotNull
    Collection<ZentrixPlayer> getAllSpectators();

    /**
     * Checks if a player is currently alive in any game.
     *
     * @param player The player to check
     * @return {@code true} if the player is alive in a game
     */
    boolean isAlive(@NotNull Player player);

    /**
     * Checks if a player is currently alive in any game by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is alive in a game
     */
    boolean isAlive(@NotNull UUID playerId);

    /**
     * Checks if a player is spectating any game.
     *
     * @param player The player to check
     * @return {@code true} if the player is spectating a game
     */
    boolean isSpectating(@NotNull Player player);

    /**
     * Checks if a player is spectating any game by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is spectating a game
     */
    boolean isSpectating(@NotNull UUID playerId);

    /**
     * Gets the number of kills a player has in their current game.
     * <p>
     * Returns 0 if the player is not in a game.
     * </p>
     *
     * @param player The player
     * @return The kill count, or 0 if not in a game
     */
    int getGameKills(@NotNull Player player);

    /**
     * Gets the number of kills a player has in their current game by UUID.
     *
     * @param playerId The player's UUID
     * @return The kill count, or 0 if not in a game
     */
    int getGameKills(@NotNull UUID playerId);

    /**
     * Gets all players in a specific game.
     *
     * @param game The game
     * @return An unmodifiable collection of players (never null)
     */
    @NotNull
    Collection<ZentrixPlayer> getPlayersInGame(@NotNull ZentrixGame game);

    /**
     * Gets all spectators in a specific game.
     *
     * @param game The game
     * @return An unmodifiable collection of spectators (never null)
     */
    @NotNull
    Collection<ZentrixPlayer> getSpectatorsInGame(@NotNull ZentrixGame game);

    /**
     * Checks if two players are in the same game.
     *
     * @param player1 First player
     * @param player2 Second player
     * @return {@code true} if both players are in the same game
     */
    boolean areInSameGame(@NotNull Player player1, @NotNull Player player2);

    /**
     * Checks if two players are in the same game by UUID.
     *
     * @param playerId1 First player's UUID
     * @param playerId2 Second player's UUID
     * @return {@code true} if both players are in the same game
     */
    boolean areInSameGame(@NotNull UUID playerId1, @NotNull UUID playerId2);

    /**
     * Where a player stands, online or not.
     * <p>
     * Unlike {@link #getPlayer(UUID)}, this also answers for a player who left a running match
     * and can still return to it ({@link ParticipantState#DISCONNECTED_RETURNABLE}), and tells an
     * eliminated participant apart from a spectator who never played.
     * </p>
     *
     * @param playerId The player's UUID
     * @return The player's state; {@link ParticipantState#NOT_PARTICIPATING} outside any match
     * @since 1.8.0
     */
    @NotNull
    default ParticipantState getParticipantState(@NotNull UUID playerId) {
        return getPlayer(playerId).map(ZentrixPlayer::getParticipantState)
                .orElse(ParticipantState.NOT_PARTICIPATING);
    }

    /**
     * The living player whose point of view a spectator currently has, if any.
     *
     * @param spectator The spectator
     * @return The viewed player; empty in free flight or for a player who is not spectating
     * @since 1.8.0
     */
    @NotNull
    default Optional<Player> getSpectatorCameraTarget(@NotNull Player spectator) {
        throw new UnsupportedOperationException("Spectator cameras require Zentrix API 1.8.0");
    }

    /**
     * Puts a spectator into a living player's point of view, as clicking that player would.
     * <p>
     * Only living players of the match the spectator watches can be viewed. A
     * {@link dev.itsharshxd.zentrix.api.events.spectator.SpectatorCameraEvent} listener may still
     * refuse. Main thread only.
     * </p>
     *
     * @param spectator The spectator
     * @param target    The living player to view
     * @return {@code true} when the spectator now views the target
     * @throws IllegalStateException off the main thread
     * @since 1.8.0
     */
    default boolean setSpectatorCameraTarget(@NotNull Player spectator, @NotNull Player target) {
        throw new UnsupportedOperationException("Spectator cameras require Zentrix API 1.8.0");
    }

    /**
     * Returns a spectator from another player's point of view to free flight, as sneaking would.
     * Main thread only.
     *
     * @param spectator The spectator
     * @return {@code true} when they had been viewing a player
     * @throws IllegalStateException off the main thread
     * @since 1.8.0
     */
    default boolean clearSpectatorCameraTarget(@NotNull Player spectator) {
        throw new UnsupportedOperationException("Spectator cameras require Zentrix API 1.8.0");
    }
}
