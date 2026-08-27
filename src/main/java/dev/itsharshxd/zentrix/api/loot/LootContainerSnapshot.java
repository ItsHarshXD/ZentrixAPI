package dev.itsharshxd.zentrix.api.loot;

import java.util.List;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/** Read-only description of one logical block container (including both halves of a chest). */
public record LootContainerSnapshot(
        @NotNull String id,
        @NotNull LootContainerType type,
        @NotNull List<Location> positions,
        int size,
        boolean available,
        boolean processed,
        boolean playerPlaced) {

    public LootContainerSnapshot {
        positions = positions.stream().map(Location::clone).toList();
    }

    @Override public List<Location> positions() {
        return positions.stream().map(Location::clone).toList();
    }
}
