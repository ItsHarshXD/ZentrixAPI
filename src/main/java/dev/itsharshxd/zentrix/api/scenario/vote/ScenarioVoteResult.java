package dev.itsharshxd.zentrix.api.scenario.vote;

import org.jetbrains.annotations.NotNull;

/**
 * The outcome of asking Zentrix to cast, change or clear a vote.
 *
 * @since 1.6.0
 */
public enum ScenarioVoteResult {

    /** The vote was recorded. */
    ACCEPTED,

    /** The vote was taken back. */
    WITHDRAWN,

    /** The player already voted for this scenario and changes are disabled. */
    ALREADY_VOTED,

    /** The player used up their vote limit. */
    LIMIT_REACHED,

    /** Voting is not open for this match. */
    NOT_RUNNING,

    /** The scenario is not in this match's eligible pool. */
    NOT_ELIGIBLE,

    /** The player is not in this match. */
    NOT_IN_GAME,

    /** An addon cancelled the vote through the vote event. */
    CANCELLED;

    /** Whether the vote actually changed anything. */
    public boolean isSuccess() {
        return this == ACCEPTED || this == WITHDRAWN;
    }

    @NotNull
    public String key() {
        return name().toLowerCase(java.util.Locale.ROOT).replace('_', '-');
    }
}
