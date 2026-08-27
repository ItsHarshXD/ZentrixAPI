package dev.itsharshxd.zentrix.api.scenario.vote;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * A detached read of one match's vote at a single moment.
 *
 * <p>Snapshots never change after they are handed out, so an addon can hold one without racing the
 * live session.
 *
 * @param runtimeId      the match this vote belongs to
 * @param status         where the vote stands
 * @param secondsLeft    how long voting stays open, 0 once it closed
 * @param eligible       the scenarios that could be voted for
 * @param tallies        votes per scenario; scenarios with no votes are present with a count of 0
 * @param votesByPlayer  what each player voted for, empty once voting closed and the session was
 *                       cleared
 * @param winners        the locked-in winners, empty until the vote resolves
 * @param voteLimit      how many scenarios one player may vote for
 * @param winnerCount    how many scenarios the vote elects
 * @param allowChanges   whether a player may take a vote back
 * @param visibility     how much of the tally players may see
 * @since 1.6.0
 */
public record ScenarioVoteSnapshot(
        @NotNull String runtimeId,
        @NotNull ScenarioVoteStatus status,
        int secondsLeft,
        @NotNull Set<String> eligible,
        @NotNull Map<String, Integer> tallies,
        @NotNull Map<UUID, Set<String>> votesByPlayer,
        @NotNull List<String> winners,
        int voteLimit,
        int winnerCount,
        boolean allowChanges,
        @NotNull ScenarioVoteVisibility visibility) {

    public ScenarioVoteSnapshot {
        eligible = Collections.unmodifiableSet(new LinkedHashSet<>(eligible));
        tallies = Collections.unmodifiableMap(new LinkedHashMap<>(tallies));
        Map<UUID, Set<String>> copiedVotes = new LinkedHashMap<>();
        votesByPlayer.forEach((player, choices) ->
                copiedVotes.put(player, Collections.unmodifiableSet(new LinkedHashSet<>(choices))));
        votesByPlayer = Collections.unmodifiableMap(copiedVotes);
        winners = List.copyOf(winners);
    }

    /** Whether players may still cast or change votes. */
    public boolean isOpen() {
        return status == ScenarioVoteStatus.RUNNING;
    }

    /** How many votes a scenario has. */
    public int votesFor(@NotNull String scenarioId) {
        return tallies.getOrDefault(scenarioId, 0);
    }

    /** What one player voted for. */
    @NotNull
    public Set<String> votesOf(@NotNull UUID playerId) {
        return votesByPlayer.getOrDefault(playerId, Set.of());
    }

    /** How many distinct players cast at least one vote. */
    public int voterCount() {
        return (int) votesByPlayer.values().stream().filter(choices -> !choices.isEmpty()).count();
    }
}
