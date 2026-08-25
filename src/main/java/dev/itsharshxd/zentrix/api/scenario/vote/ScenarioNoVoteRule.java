package dev.itsharshxd.zentrix.api.scenario.vote;

/**
 * What a match runs when voting closes without a single vote.
 *
 * @since 1.7.0
 */
public enum ScenarioNoVoteRule {

    /** Run no scenarios at all. */
    NONE,

    /** Draw from the eligible pool the way automatic selection would. */
    RANDOM,

    /** Fall back to the administrator-configured set for this arena. */
    ADMIN_SET,

    /** Run the highest-priority eligible scenarios up to the winner count. */
    HIGHEST_PRIORITY
}
