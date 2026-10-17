package dev.itsharshxd.zentrix.api.phase;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Where one match is in its phase timeline, read at one moment. A snapshot.
 *
 * <p>A match captures its phase sequence when it is created, so this describes the phases it
 * actually runs even after {@code gameplay/phases.yml} was reloaded or phases were registered or
 * unregistered - unlike the global queries of {@link PhaseService}, which describe the current
 * configuration.
 *
 * @param status           where the timeline is
 * @param phaseNames       the names of the phases this match runs, in order; empty when the match
 *                         captured none yet
 * @param currentIndex     the index of the running phase in {@code phaseNames}; -1 unless
 *                         {@link Status#RUNNING} or {@link Status#PAUSED}
 * @param durationSeconds  the running phase's configured length; 0 without one
 * @param elapsedSeconds   how long the running phase has been running; 0 without one
 * @param remainingSeconds how long until it ends; 0 without one or while paused
 * @since 1.8.0
 */
public record PhaseRuntimeState(
        @NotNull Status status,
        @NotNull List<String> phaseNames,
        int currentIndex,
        int durationSeconds,
        int elapsedSeconds,
        int remainingSeconds) {

    public PhaseRuntimeState {
        Objects.requireNonNull(status, "status");
        phaseNames = List.copyOf(phaseNames);
    }

    /** The running phase's name. */
    @NotNull
    public Optional<String> currentPhaseName() {
        return currentIndex >= 0 && currentIndex < phaseNames.size()
                ? Optional.of(phaseNames.get(currentIndex)) : Optional.empty();
    }

    /** The name of the phase that follows the running one; empty during the last phase. */
    @NotNull
    public Optional<String> nextPhaseName() {
        int next = currentIndex + 1;
        return currentIndex >= 0 && next < phaseNames.size()
                ? Optional.of(phaseNames.get(next)) : Optional.empty();
    }

    /** Where a match's phase timeline is. */
    public enum Status {
        /** The match has not started its phases, or is no longer running them. */
        NOT_RUNNING,
        /** A phase is running. */
        RUNNING,
        /** The timeline is paused. */
        PAUSED,
        /** Every phase has ended while the match goes on. */
        COMPLETED
    }
}
