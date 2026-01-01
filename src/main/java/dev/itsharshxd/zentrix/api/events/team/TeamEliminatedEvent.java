package dev.itsharshxd.zentrix.api.events.team;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Called when a team is eliminated from a Zentrix game.
 * <p>
 * This event is fired when all members of a team have been eliminated
 * (killed or left the game). At this point:
 * <ul>
 *   <li>All team members have been eliminated</li>
 *   <li>The team is marked as eliminated</li>
 *   <li>Remaining teams count has been updated</li>
 *   <li>Win condition may be checked next</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>NOT cancellable</b>.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onTeamEliminated(TeamEliminatedEvent event) {
 *     ZentrixTeam team = event.getTeam();
 *     ZentrixGame game = event.getGame();
 *
 *     // Announce team elimination
 *     game.broadcast("&c" + team.getDisplayName() + " has been eliminated!");
 *
 *     // Check remaining teams
 *     int remaining = event.getRemainingTeamCount();
 *     game.broadcast("&7" + remaining + " teams remain!");
 *
 *     // Check if this triggers a win
 *     if (event.triggersWin()) {
 *         event.getWinningTeam().ifPresent(winner -> {
 *             game.broadcast("&6" + winner.getDisplayName() + " wins!");
 *         });
 *     }
 *
 *     // Get the player who caused the final elimination
 *     event.getFinalKiller().ifPresent(killer -> {
 *         getLogger().info(killer.getName() + " got the final kill on " +
 *                          team.getDisplayName());
 *     });
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see dev.itsharshxd.zentrix.api.events.player.PlayerDeathGameEvent
 * @see dev.itsharshxd.zentrix.api.events.game.GameEndEvent
 */
public class TeamEliminatedEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixTeam team;
    private final ZentrixPlayer lastMemberEliminated;
    private final ZentrixPlayer finalKiller;
    private final Collection<ZentrixPlayer> teamMembers;
    private final int placement;
    private final int remainingTeamCount;

    /**
     * Constructs a new TeamEliminatedEvent.
     *
     * @param game                  The game where the elimination occurred
     * @param team                  The team that was eliminated
     * @param lastMemberEliminated  The last member of the team to be eliminated
     * @param finalKiller           The player who killed the last team member (null if environmental)
     * @param teamMembers           All members who were on this team
     * @param placement             The team's final placement (e.g., 5th place = 5)
     * @param remainingTeamCount    The number of teams still alive after this elimination
     */
    public TeamEliminatedEvent(@NotNull ZentrixGame game,
                               @NotNull ZentrixTeam team,
                               @NotNull ZentrixPlayer lastMemberEliminated,
                               @Nullable ZentrixPlayer finalKiller,
                               @NotNull Collection<ZentrixPlayer> teamMembers,
                               int placement,
                               int remainingTeamCount) {
        super(game);
        this.team = team;
        this.lastMemberEliminated = lastMemberEliminated;
        this.finalKiller = finalKiller;
        this.teamMembers = Collections.unmodifiableCollection(teamMembers);
        this.placement = placement;
        this.remainingTeamCount = remainingTeamCount;
    }

    /**
     * Gets the team that was eliminated.
     *
     * @return The eliminated team (never null)
     */
    @NotNull
    public ZentrixTeam getTeam() {
        return team;
    }

    /**
     * Gets the team's display name.
     * <p>
     * Convenience method equivalent to {@code getTeam().getDisplayName()}.
     * </p>
     *
     * @return The team's display name (never null)
     */
    @NotNull
    public String getTeamDisplayName() {
        return team.getDisplayName();
    }

    /**
     * Gets the team's ID.
     * <p>
     * Convenience method equivalent to {@code getTeam().getTeamId()}.
     * </p>
     *
     * @return The team ID (never null)
     */
    @NotNull
    public String getTeamId() {
        return team.getTeamId();
    }

    /**
     * Gets the team's number.
     * <p>
     * Convenience method equivalent to {@code getTeam().getTeamNumber()}.
     * </p>
     *
     * @return The team number
     */
    public int getTeamNumber() {
        return team.getTeamNumber();
    }

    /**
     * Gets the last member of the team to be eliminated.
     * <p>
     * This is the player whose death triggered the team elimination.
     * </p>
     *
     * @return The last eliminated member (never null)
     */
    @NotNull
    public ZentrixPlayer getLastMemberEliminated() {
        return lastMemberEliminated;
    }

    /**
     * Gets the player who killed the last team member.
     * <p>
     * Returns empty if the last member died to environmental damage.
     * </p>
     *
     * @return Optional containing the killer, or empty if environmental death
     */
    @NotNull
    public Optional<ZentrixPlayer> getFinalKiller() {
        return Optional.ofNullable(finalKiller);
    }

    /**
     * Checks if there was a killer for the final team member.
     *
     * @return {@code true} if the last member was killed by another player
     */
    public boolean hasFinalKiller() {
        return finalKiller != null;
    }

    /**
     * Gets all members who were on this team.
     * <p>
     * This includes all players who were assigned to this team during the game,
     * regardless of when they were eliminated.
     * </p>
     *
     * @return An unmodifiable collection of team members (never null)
     */
    @NotNull
    public Collection<ZentrixPlayer> getTeamMembers() {
        return teamMembers;
    }

    /**
     * Gets the number of members that were on this team.
     *
     * @return The team member count
     */
    public int getTeamMemberCount() {
        return teamMembers.size();
    }

    /**
     * Gets the team's final placement.
     * <p>
     * This is calculated based on when the team was eliminated relative to others.
     * A placement of 1 means the team won, 2 means second place, etc.
     * </p>
     *
     * @return The placement (1-indexed, higher is worse)
     */
    public int getPlacement() {
        return placement;
    }

    /**
     * Gets the placement as an ordinal string.
     * <p>
     * Examples: "1st", "2nd", "3rd", "4th", etc.
     * </p>
     *
     * @return The ordinal placement string (never null)
     */
    @NotNull
    public String getPlacementOrdinal() {
        if (placement % 100 >= 11 && placement % 100 <= 13) {
            return placement + "th";
        }
        return switch (placement % 10) {
            case 1 -> placement + "st";
            case 2 -> placement + "nd";
            case 3 -> placement + "rd";
            default -> placement + "th";
        };
    }

    /**
     * Gets the number of teams still alive after this elimination.
     *
     * @return Remaining team count
     */
    public int getRemainingTeamCount() {
        return remainingTeamCount;
    }

    /**
     * Checks if this elimination triggers a win condition.
     * <p>
     * Returns true if only one team remains after this elimination.
     * </p>
     *
     * @return {@code true} if this elimination results in a winner
     */
    public boolean triggersWin() {
        return remainingTeamCount == 1;
    }

    /**
     * Gets the winning team if this elimination triggers a win.
     * <p>
     * Returns empty if there is no winner yet (more than one team remains).
     * </p>
     *
     * @return Optional containing the winning team, or empty if no winner yet
     */
    @NotNull
    public Optional<ZentrixTeam> getWinningTeam() {
        if (triggersWin()) {
            return getGame().getAliveTeams().stream().findFirst();
        }
        return Optional.empty();
    }

    /**
     * Gets the total kills achieved by this team during the game.
     *
     * @return Total team kills
     */
    public int getTotalTeamKills() {
        return teamMembers.stream()
                .mapToInt(ZentrixPlayer::getGameKills)
                .sum();
    }

    /**
     * Gets the member with the most kills on this team.
     *
     * @return Optional containing the top killer, or empty if no kills
     */
    @NotNull
    public Optional<ZentrixPlayer> getTopKiller() {
        return teamMembers.stream()
                .filter(p -> p.getGameKills() > 0)
                .max((p1, p2) -> Integer.compare(p1.getGameKills(), p2.getGameKills()));
    }

    /**
     * Checks if this was the last team to be eliminated before the winner.
     * <p>
     * Returns true if this team got second place (placement == 2).
     * </p>
     *
     * @return {@code true} if this team got second place
     */
    public boolean wasRunnerUp() {
        return placement == 2;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the static handler list for this event type.
     *
     * @return The handler list
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
