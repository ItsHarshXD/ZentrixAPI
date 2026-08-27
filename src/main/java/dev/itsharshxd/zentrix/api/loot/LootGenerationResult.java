package dev.itsharshxd.zentrix.api.loot;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/** Immutable output from real or simulated loot generation. */
public record LootGenerationResult(
        int targetAppearances,
        @NotNull List<LootPlacement> placements,
        @NotNull Map<String, Integer> unplacedRequiredAppearances,
        @NotNull Set<String> spaceLimitedEntries,
        @NotNull Set<String> failedEntries,
        int unfilledTarget,
        @NotNull LootValidationResult validation) {

    public LootGenerationResult {
        placements = List.copyOf(placements);
        unplacedRequiredAppearances = Map.copyOf(unplacedRequiredAppearances);
        spaceLimitedEntries = Set.copyOf(spaceLimitedEntries);
        failedEntries = Set.copyOf(failedEntries);
    }

    public boolean generatedAnything() { return !placements.isEmpty(); }
}
