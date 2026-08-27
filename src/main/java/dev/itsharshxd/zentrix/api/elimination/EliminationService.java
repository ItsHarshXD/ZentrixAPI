package dev.itsharshxd.zentrix.api.elimination;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Collection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Ending a player's or a team's match without a death.
 *
 * <p>An alternative win condition, an objective, a countdown or a wager all need the same thing: put
 * somebody out of the match exactly the way dying would, minus the rewards nobody earned. Doing that
 * by hand means reproducing Zentrix's whole death pipeline — team removal, statistics, spectator
 * conversion, team-elimination detection, the addon events, the win-condition check — and getting
 * one step wrong leaves a match that can never end. This service is that pipeline, exposed.
 *
 * <p>What is awarded is entirely up to {@link EliminationOptions}; removal, team bookkeeping and
 * the addon events are not optional and always happen. The eliminated players become spectators of
 * the match they were in, so they keep watching rather than being thrown back to the lobby.
 *
 * <p>The win condition is the one part of the pipeline a caller may decline, with
 * {@link EliminationOptions#checkWinCondition()}. Left on, as every preset leaves it, a request
 * checks once at the end and a batch that wipes out the field ends the match inside the call.
 *
 * <h2>Example: an objective decides the match</h2>
 * <pre>{@code
 * EliminationService eliminations = ZentrixAPI.get().getEliminationService();
 *
 * // Everybody except the team that completed the objective is out, in one atomic batch.
 * // The winning team is left alive, so Zentrix's own last-team-standing rule crowns it
 * // and the ordinary win rewards are paid out.
 * eliminations.eliminateOtherTeams(
 *         this, game, winningTeamId,
 *         EliminationOptions.scenario()
 *                 .messageKey("scenarios.my-objective.eliminated")
 *                 .build());
 * }</pre>
 *
 * <p>Every method must be called from the server main thread.
 *
 * @since 1.6.0
 */
public interface EliminationService {

    /**
     * Ends one player's match.
     *
     * @param owner   the plugin asking for it, for diagnostics
     * @param game    the match the player is in
     * @param player  the player to eliminate
     * @param options what the elimination awards and announces
     * @return what happened
     */
    @NotNull
    EliminationResult eliminate(
            @NotNull Plugin owner,
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull EliminationOptions options);

    /**
     * Pays out a death that did not end anybody's match.
     *
     * <p>The awards half of the pipeline on its own, for a rule that takes a death over and puts the
     * player straight back into the match — a revive scenario, a life system, a role that respawns.
     * Nobody is removed, nobody becomes a spectator and no win condition is checked, because nobody
     * left: this only settles what the death was worth.
     *
     * <p>Answering
     * {@link dev.itsharshxd.zentrix.api.scenario.hook.GameplayHooks#PLAYER_DEATH} with a replacement
     * tells Zentrix to stand down from a death completely, which also means the killer's kill, the
     * kill reward, first blood and the victim's recorded death all stop happening. This is how a
     * scenario hands back the parts it still wants, and {@link EliminationOptions} decides which
     * those are — {@link EliminationOptions.Builder#attacker(java.util.UUID) the attacker} has to be
     * named for any of the kill awards to reach anybody.
     *
     * <p>Call it while the victim is still a living participant of the match, which for a death that
     * is being taken over means from inside the death itself.
     *
     * @param owner   the plugin asking for it, for diagnostics
     * @param game    the match the death happened in
     * @param victim  the player who died and is staying in the match
     * @param options what the death awards and announces
     * @return true when the death was credited, false when the victim was not a living participant
     * @since 1.6.0
     */
    boolean creditDeath(
            @NotNull Plugin owner,
            @NotNull ZentrixGame game,
            @NotNull Player victim,
            @NotNull EliminationOptions options);

    /**
     * Ends the match for every living member of one team, atomically.
     *
     * <p>The win condition is checked once, after the last member is out, so a team of four never
     * ends the match three times over — and not at all if
     * {@link EliminationOptions#checkWinCondition()} is off.
     *
     * @param teamId the team's ID, as {@code ZentrixTeam#getId()} reports it
     */
    @NotNull
    EliminationResult eliminateTeam(
            @NotNull Plugin owner,
            @NotNull ZentrixGame game,
            @NotNull String teamId,
            @NotNull EliminationOptions options);

    /**
     * Ends the match for every living team of a match except the ones named, atomically.
     *
     * <p>This is the objective-win call. Every other team is eliminated in one batch and the win
     * condition is checked once at the end, which leaves the surviving team standing alone and lets
     * Zentrix's ordinary last-team-standing rule declare and reward it. Naming no surviving team at
     * all is refused rather than ending the match with nobody left.
     *
     * @param survivingTeamIds the teams that stay in the match; must not be empty
     */
    @NotNull
    EliminationResult eliminateOtherTeams(
            @NotNull Plugin owner,
            @NotNull ZentrixGame game,
            @NotNull Collection<String> survivingTeamIds,
            @NotNull EliminationOptions options);

    /** Convenience for {@link #eliminateOtherTeams} with a single surviving team. */
    @NotNull
    default EliminationResult eliminateOtherTeams(
            @NotNull Plugin owner,
            @NotNull ZentrixGame game,
            @NotNull String survivingTeamId,
            @NotNull EliminationOptions options) {
        return eliminateOtherTeams(owner, game, java.util.List.of(survivingTeamId), options);
    }
}
