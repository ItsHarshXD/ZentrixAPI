package dev.itsharshxd.zentrix.api.scenario.vote;

/**
 * Where a match's vote stands.
 *
 * <p>A match may only leave the waiting lobby once the vote reached {@link #RESOLVED} or
 * {@link #NOT_APPLICABLE}.
 *
 * @since 1.7.0
 */
public enum ScenarioVoteStatus {

    /** This match does not vote; its scenarios come from another selection mode. */
    NOT_APPLICABLE,

    /** The lobby exists but voting has not opened yet. */
    PENDING,

    /** Voting is open and players may cast or change votes. */
    RUNNING,

    /** Voting closed and the winners are locked in. The match may start. */
    RESOLVED,

    /** The lobby was cancelled before the vote could resolve. */
    CANCELLED
}
