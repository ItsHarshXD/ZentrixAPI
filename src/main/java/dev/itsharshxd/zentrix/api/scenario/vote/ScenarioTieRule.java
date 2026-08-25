package dev.itsharshxd.zentrix.api.scenario.vote;

/**
 * How a vote resolves when several scenarios finish on the same number of votes and only some of
 * them fit into the winner count.
 *
 * <p>Every rule is deterministic in the sense that it always produces a full, valid winner set; only
 * {@link #RANDOM} is deliberately non-repeatable.
 *
 * @since 1.7.0
 */
public enum ScenarioTieRule {

    /** Pick between the tied scenarios at random. */
    RANDOM,

    /** Prefer the tied scenario with the highest scenario priority, then the lowest ID. */
    PRIORITY,

    /** Keep the scenario whose first vote was cast earliest. */
    FIRST_VOTE,

    /** Run every tied scenario, going over the configured winner count. */
    INCLUDE_ALL,

    /** Drop every tied scenario and fill the remaining slots from the next-highest tally. */
    EXCLUDE_ALL
}
