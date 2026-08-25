package dev.itsharshxd.zentrix.api.scenario.vote;

/**
 * How much of a running vote players are allowed to see.
 *
 * @since 1.7.0
 */
public enum ScenarioVoteVisibility {

    /** Tallies update live in the voting menu. */
    LIVE,

    /** Tallies stay hidden until voting closes, then the result is announced. */
    RESULT_ONLY,

    /** Nothing is shown; players only learn which scenarios ended up running. */
    HIDDEN
}
