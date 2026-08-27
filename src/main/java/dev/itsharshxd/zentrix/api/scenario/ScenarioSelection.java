package dev.itsharshxd.zentrix.api.scenario;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * The scenarios one match settled on, in the order they activate.
 *
 * <p>A selection is locked before the match leaves the waiting lobby and never changes afterwards,
 * which is what makes a running match immune to configuration reloads, votes that arrive late, and
 * scenarios being registered or unregistered mid-game.
 *
 * @param runtimeId   the match the selection belongs to
 * @param mode        how the set was decided
 * @param scenarioIds the scenarios, highest priority first
 * @param validation  what validation made of the set while it was being locked in
 * @param locked      whether the set is final; a pending vote is not
 * @since 1.6.0
 */
public record ScenarioSelection(
        @NotNull String runtimeId,
        @NotNull ScenarioSelectionMode mode,
        @NotNull List<String> scenarioIds,
        @NotNull ScenarioValidation validation,
        boolean locked) {

    public ScenarioSelection {
        scenarioIds = List.copyOf(scenarioIds);
    }

    /** An empty, locked selection, for matches that run no scenarios at all. */
    @NotNull
    public static ScenarioSelection empty(@NotNull String runtimeId, @NotNull ScenarioSelectionMode mode) {
        return new ScenarioSelection(
                runtimeId, mode, List.of(),
                new ScenarioValidation(true, List.of(), List.of()), true);
    }

    public boolean isEmpty() {
        return scenarioIds.isEmpty();
    }

    public boolean contains(@NotNull String scenarioId) {
        return scenarioIds.contains(scenarioId.toLowerCase(java.util.Locale.ROOT));
    }

    /** The first scenario, for matches that only ever run one. */
    @NotNull
    public Optional<String> primary() {
        return scenarioIds.isEmpty() ? Optional.empty() : Optional.of(scenarioIds.getFirst());
    }
}
