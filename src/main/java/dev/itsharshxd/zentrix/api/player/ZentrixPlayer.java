package dev.itsharshxd.zentrix.api.player;

import dev.itsharshxd.zentrix.api.classes.PlayerClass;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a read-only view of a player participating in a Zentrix game.
 * <p>
 * This interface provides access to player information including their
 * identity, game statistics, team membership, and selected class.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ZentrixPlayer zentrixPlayer = ZentrixProvider.get()
 *     .getPlayerService()
 *     .getPlayer(player)
 *     .orElse(null);
 *
 * if (zentrixPlayer != null) {
 *     int kills = zentrixPlayer.getGameKills();
 *     String teamId = zentrixPlayer.getTeamId().orElse("none");
 *     boolean alive = zentrixPlayer.isAlive();
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ZentrixPlayer {

    /**
     * Gets the player's unique identifier (UUID).
     *
     * @return The player's UUID (never null)
     */
    @NotNull
    UUID getUniqueId();

    /**
     * Gets the player's name.
     *
     * @return The player's name (never null)
     */
    @NotNull
    String getName();

    /**
     * Gets the underlying Bukkit player instance if online.
     * <p>
     * The player may be offline if they disconnected during the game.
     * </p>
     *
     * @return Optional containing the Bukkit player, or empty if offline
     */
    @NotNull
    Optional<Player> getBukkitPlayer();

    /**
     * Checks if the player is currently online.
     *
     * @return {@code true} if the player is online
     */
    boolean isOnline();

    /**
     * Gets the player's current kill count in this game.
     *
     * @return The number of kills this game
     */
    int getGameKills();

    /**
     * Gets the player's team.
     *
     * @return Optional containing the team, or empty if not assigned
     */
    @NotNull
    Optional<ZentrixTeam> getTeam();

    /**
     * Gets the player's team ID.
     * <p>
     * Format: "team-{number}" (e.g., "team-1", "team-2")
     * </p>
     *
     * @return Optional containing the team ID, or empty if not assigned
     */
    @NotNull
    Optional<String> getTeamId();

    /**
     * Checks if the player has been assigned to a team.
     *
     * @return {@code true} if the player is on a team
     */
    boolean hasTeam();

    /**
     * Gets the player's selected class.
     *
     * @return Optional containing the class, or empty if none selected
     */
    @NotNull
    Optional<PlayerClass> getSelectedClass();

    /**
     * Checks if the player has selected a class.
     *
     * @return {@code true} if a class is selected
     */
    boolean hasSelectedClass();

    /**
     * Checks if the player is alive (not eliminated/spectating).
     *
     * @return {@code true} if the player is alive in the game
     */
    boolean isAlive();

    /**
     * Checks if the player is spectating.
     *
     * @return {@code true} if the player is spectating
     */
    boolean isSpectating();

    // ==========================================
    // Per-Game Statistics
    // ==========================================

    /**
     * Gets the total damage dealt by this player in the current game.
     *
     * @return Damage dealt, or 0 if stats not tracked
     */
    double getDamageDealt();

    /**
     * Gets the total damage taken by this player in the current game.
     *
     * @return Damage taken, or 0 if stats not tracked
     */
    double getDamageTaken();

    /**
     * Gets the player's current kill streak.
     * <p>
     * Kill streak resets on death.
     * </p>
     *
     * @return Current kill streak, or 0 if none
     */
    int getKillStreak();

    /**
     * Gets the highest kill streak achieved in this game.
     *
     * @return Highest kill streak, or 0 if none
     */
    int getHighestKillStreak();

    /**
     * Gets the player's survival time in seconds.
     * <p>
     * Measured from game start (not waiting lobby join).
     * </p>
     *
     * @return Survival time in seconds
     */
    long getSurvivalTimeSeconds();

    /**
     * Checks if per-game statistics are available.
     * <p>
     * Stats may not be available if the player just joined or
     * the game hasn't started yet.
     * </p>
     *
     * @return {@code true} if stats are available
     */
    boolean hasGameStats();
}
