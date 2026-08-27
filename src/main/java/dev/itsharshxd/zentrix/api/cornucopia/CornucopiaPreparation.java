package dev.itsharshxd.zentrix.api.cornucopia;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/** Immutable snapshot of one runtime Cornucopia's preparation and release state. */
public record CornucopiaPreparation(
        @NotNull CornucopiaPreparationState state,
        boolean playersTeleported,
        boolean cagesCreated,
        boolean countdownActive,
        int countdownRemainingSeconds,
        @NotNull CornucopiaLootPopulationState lootPopulationState,
        @NotNull Optional<UUID> lootGenerationId,
        @NotNull Map<UUID, Location> podiumAssignments) {

    public CornucopiaPreparation {
        lootGenerationId = lootGenerationId == null ? Optional.empty() : lootGenerationId;
        podiumAssignments = podiumAssignments.entrySet().stream().collect(
                java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().clone()));
    }

    @Override public Map<UUID, Location> podiumAssignments() {
        return podiumAssignments.entrySet().stream().collect(
                java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().clone()));
    }
}
