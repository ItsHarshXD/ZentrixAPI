package dev.itsharshxd.zentrix.api.revival;

import java.util.Optional;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/** Optional per-operation overrides for a revival. Empty values use Zentrix settings. */
public record RevivalOptions(
        @NotNull Optional<Boolean> restoreClassItems,
        @NotNull Optional<Boolean> restoreLostLoot,
        @NotNull Optional<Location> destination) {

    public RevivalOptions {
        restoreClassItems = restoreClassItems == null ? Optional.empty() : restoreClassItems;
        restoreLostLoot = restoreLostLoot == null ? Optional.empty() : restoreLostLoot;
        destination = destination == null
                ? Optional.empty()
                : destination.map(Location::clone);
    }

    @Override
    public Optional<Location> destination() {
        return destination.map(Location::clone);
    }

    @NotNull
    public static RevivalOptions defaults() {
        return new RevivalOptions(Optional.empty(), Optional.empty(), Optional.empty());
    }

    @NotNull
    public RevivalOptions withRestoreClassItems(boolean value) {
        return new RevivalOptions(Optional.of(value), restoreLostLoot, destination);
    }

    @NotNull
    public RevivalOptions withRestoreLostLoot(boolean value) {
        return new RevivalOptions(restoreClassItems, Optional.of(value), destination);
    }

    @NotNull
    public RevivalOptions at(@NotNull Location value) {
        return new RevivalOptions(restoreClassItems, restoreLostLoot, Optional.of(value));
    }
}
