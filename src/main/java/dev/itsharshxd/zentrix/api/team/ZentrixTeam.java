package dev.itsharshxd.zentrix.api.team;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents a read-only view of a team in a Zentrix game.
 * <p>
 * Teams are created when players join a game and are used to group
 * players together. In solo games, each player is their own team.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ZentrixTeam team = ZentrixProvider.get()
 *     .getTeamService()
 *     .getPlayerTeam(player)
 *     .orElse(null);
 *
 * if (team != null) {
 *     String name = team.getDisplayName();
 *     String color = team.getColor();
 *     int aliveCount = team.getAliveMemberCount();
 *     boolean eliminated = team.isEliminated();
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ZentrixTeam {

    /**
     * Gets the team's unique identifier.
     * <p>
     * Format: "team-{number}" (e.g., "team-1", "team-2")
     * </p>
     *
     * @return The team ID (never null)
     */
    @NotNull
    String getTeamId();

    /**
     * Gets the team number (1-indexed).
     * <p>
     * This is a sequential number assigned when the team is created.
     * </p>
     *
     * @return The team number (e.g., 1, 2, 3, ...)
     */
    int getTeamNumber();

    /**
     * Gets the team's display name with color codes.
     * <p>
     * This is the formatted name shown in chat and GUIs.
     * Supports MiniMessage format and legacy color codes.
     * </p>
     *
     * @return The display name (never null)
     */
    @NotNull
    String getDisplayName();

    /**
     * Gets the team's color code.
     * <p>
     * This can be a hex color (e.g., "&#FF5555") or legacy color code.
     * </p>
     *
     * @return The color code (never null)
     */
    @NotNull
    String getColor();

    /**
     * Gets the team's symbol.
     * <p>
     * The symbol is a short text/character that represents the team,
     * often used in scoreboards and nametags.
     * </p>
     *
     * @return The team symbol (never null)
     */
    @NotNull
    String getSymbol();

    /**
     * Gets the chat format template for this team.
     * <p>
     * Placeholders:
     * <ul>
     *   <li>{@code {color}} - Team color</li>
     *   <li>{@code {team}} - Team name</li>
     *   <li>{@code {player}} - Player name</li>
     *   <li>{@code {message}} - Chat message</li>
     * </ul>
     * </p>
     *
     * @return The chat format template (never null)
     */
    @NotNull
    String getChatFormat();

    /**
     * Gets the nametag format template for this team.
     * <p>
     * Placeholders:
     * <ul>
     *   <li>{@code {color}} - Team color</li>
     *   <li>{@code {player}} - Player name</li>
     * </ul>
     * </p>
     *
     * @return The nametag format template (never null)
     */
    @NotNull
    String getNametagFormat();

    /**
     * Checks if friendly fire is enabled for this team.
     * <p>
     * When enabled, teammates can damage each other.
     * </p>
     *
     * @return {@code true} if teammates can damage each other
     */
    boolean isFriendlyFireEnabled();

    /**
     * Gets all member UUIDs of this team.
     * <p>
     * This includes both alive and eliminated members who were
     * part of this team during the game.
     * </p>
     *
     * @return An unmodifiable collection of member UUIDs (never null)
     */
    @NotNull
    Collection<UUID> getMemberIds();

    /**
     * Gets the current number of members in this team.
     * <p>
     * This is the total count, including eliminated members.
     * </p>
     *
     * @return The total member count
     */
    int getMemberCount();

    /**
     * Gets the number of alive members in this team.
     * <p>
     * Alive members are those who have not been eliminated.
     * </p>
     *
     * @return The count of alive members
     */
    int getAliveMemberCount();

    /**
     * Checks if this team is eliminated.
     * <p>
     * A team is eliminated when all its members have been killed or left the game.
     * </p>
     *
     * @return {@code true} if the team has no alive members
     */
    boolean isEliminated();

    /**
     * Checks if the team is empty (no members at all).
     *
     * @return {@code true} if the team has no members
     */
    boolean isEmpty();

    /**
     * Checks if a specific player is a member of this team.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is on this team
     */
    boolean hasMember(@NotNull UUID playerId);

    /**
     * Checks if the team is full based on the game's team size limit.
     *
     * @param maxSize The maximum team size for the game type
     * @return {@code true} if the team has reached the maximum size
     */
    boolean isFull(int maxSize);
}
