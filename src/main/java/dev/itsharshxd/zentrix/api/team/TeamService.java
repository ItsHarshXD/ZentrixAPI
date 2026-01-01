package dev.itsharshxd.zentrix.api.team;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service for team-related queries within Zentrix games.
 * <p>
 * This service provides access to team information, membership,
 * and team status queries.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * TeamService teamService = ZentrixProvider.get().getTeamService();
 *
 * // Check if two players are teammates
 * if (teamService.areTeammates(player1, player2)) {
 *     // Don't allow friendly fire, etc.
 * }
 *
 * // Get a player's team
 * Optional<ZentrixTeam> team = teamService.getPlayerTeam(player);
 * team.ifPresent(t -> {
 *     String teamName = t.getDisplayName();
 *     int aliveMembers = t.getAliveMemberCount();
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface TeamService {

    /**
     * Gets all teams in a specific game.
     *
     * @param game The game
     * @return An unmodifiable collection of teams (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixTeam> getTeams(@NotNull ZentrixGame game);

    /**
     * Gets a team by its ID within a game.
     *
     * @param game   The game
     * @param teamId The team ID (e.g., "team-1")
     * @return Optional containing the team, or empty if not found
     */
    @NotNull
    Optional<ZentrixTeam> getTeam(@NotNull ZentrixGame game, @NotNull String teamId);

    /**
     * Gets the team a player belongs to.
     *
     * @param player The player
     * @return Optional containing the team, or empty if not in a team
     */
    @NotNull
    Optional<ZentrixTeam> getPlayerTeam(@NotNull Player player);

    /**
     * Gets the team a player belongs to by UUID.
     *
     * @param playerId The player's UUID
     * @return Optional containing the team, or empty if not in a team
     */
    @NotNull
    Optional<ZentrixTeam> getPlayerTeam(@NotNull UUID playerId);

    /**
     * Gets the team a player belongs to within a specific game.
     *
     * @param game     The game
     * @param playerId The player's UUID
     * @return Optional containing the team, or empty if not found
     */
    @NotNull
    Optional<ZentrixTeam> getPlayerTeam(@NotNull ZentrixGame game, @NotNull UUID playerId);

    /**
     * Checks if two players are on the same team.
     *
     * @param player1 First player
     * @param player2 Second player
     * @return {@code true} if both players are on the same team
     */
    boolean areTeammates(@NotNull Player player1, @NotNull Player player2);

    /**
     * Checks if two players are on the same team by UUID.
     *
     * @param playerId1 First player's UUID
     * @param playerId2 Second player's UUID
     * @return {@code true} if both players are on the same team
     */
    boolean areTeammates(@NotNull UUID playerId1, @NotNull UUID playerId2);

    /**
     * Gets all alive teams in a game.
     * <p>
     * A team is considered alive if it has at least one member
     * who is still playing (not eliminated).
     * </p>
     *
     * @param game The game
     * @return An unmodifiable collection of alive teams (never null)
     */
    @NotNull
    Collection<ZentrixTeam> getAliveTeams(@NotNull ZentrixGame game);

    /**
     * Gets the number of alive teams in a game.
     *
     * @param game The game
     * @return The count of teams with at least one alive member
     */
    int getAliveTeamCount(@NotNull ZentrixGame game);

    /**
     * Checks if a team is eliminated.
     * <p>
     * A team is eliminated when all its members have been killed or left.
     * </p>
     *
     * @param game The game
     * @param team The team to check
     * @return {@code true} if the team has no alive members
     */
    boolean isTeamEliminated(@NotNull ZentrixGame game, @NotNull ZentrixTeam team);

    /**
     * Gets the teammates of a player (excluding the player themselves).
     *
     * @param player The player
     * @return Set of teammate UUIDs (never null, may be empty)
     */
    @NotNull
    Set<UUID> getTeammates(@NotNull Player player);

    /**
     * Gets the teammates of a player by UUID.
     *
     * @param playerId The player's UUID
     * @return Set of teammate UUIDs (never null, may be empty)
     */
    @NotNull
    Set<UUID> getTeammates(@NotNull UUID playerId);

    /**
     * Gets alive teammates of a player (still in the game).
     *
     * @param player The player
     * @return Set of alive teammate UUIDs (never null, may be empty)
     */
    @NotNull
    Set<UUID> getAliveTeammates(@NotNull Player player);

    /**
     * Gets alive teammates of a player by UUID.
     *
     * @param playerId The player's UUID
     * @return Set of alive teammate UUIDs (never null, may be empty)
     */
    @NotNull
    Set<UUID> getAliveTeammates(@NotNull UUID playerId);

    /**
     * Gets the winning team for a game.
     * <p>
     * Returns the team if only one team remains alive.
     * </p>
     *
     * @param game The game
     * @return Optional containing the winning team, or empty if no winner yet
     */
    @NotNull
    Optional<ZentrixTeam> getWinningTeam(@NotNull ZentrixGame game);

    /**
     * Gets all ZentrixPlayer members of a team.
     *
     * @param team The team
     * @return An unmodifiable collection of team members (never null)
     */
    @NotNull
    Collection<ZentrixPlayer> getTeamMembers(@NotNull ZentrixTeam team);

    /**
     * Gets alive ZentrixPlayer members of a team.
     *
     * @param game The game
     * @param team The team
     * @return An unmodifiable collection of alive team members (never null)
     */
    @NotNull
    Collection<ZentrixPlayer> getAliveTeamMembers(@NotNull ZentrixGame game, @NotNull ZentrixTeam team);
}
