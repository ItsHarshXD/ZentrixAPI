package dev.itsharshxd.zentrix.api.nether;

import dev.itsharshxd.zentrix.api.world.WorldBorderSnapshot;
import java.util.Optional;
import java.util.Objects;

/** Immutable status snapshot for one runtime game's Nether. */
public record NetherStatus(
    boolean configured,
    NetherPreparationState preparationState,
    boolean accessEnabled,
    Optional<String> worldName,
    boolean worldLoaded,
    Optional<Boolean> pvpEnabled,
    Optional<WorldBorderSnapshot> border
) {
    public NetherStatus {
        Objects.requireNonNull(preparationState, "preparationState");
        worldName = worldName == null ? Optional.empty() : worldName;
        pvpEnabled = pvpEnabled == null ? Optional.empty() : pvpEnabled;
        border = border == null ? Optional.empty() : border;
    }

    public boolean isPreparing() { return preparationState == NetherPreparationState.PREPARING; }
    public boolean isPrepared() { return preparationState == NetherPreparationState.READY; }
}
