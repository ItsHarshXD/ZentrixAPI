package dev.itsharshxd.zentrix.api.end;

import dev.itsharshxd.zentrix.api.world.WorldBorderSnapshot;
import java.util.Objects;
import java.util.Optional;

/** Immutable status snapshot for one runtime game's End. */
public record EndStatus(
    boolean configured,
    EndPreparationState preparationState,
    boolean accessEnabled,
    Optional<String> worldName,
    boolean worldLoaded,
    Optional<Boolean> pvpEnabled,
    Optional<WorldBorderSnapshot> border
) {
    public EndStatus {
        Objects.requireNonNull(preparationState, "preparationState");
        worldName = worldName == null ? Optional.empty() : worldName;
        pvpEnabled = pvpEnabled == null ? Optional.empty() : pvpEnabled;
        border = border == null ? Optional.empty() : border;
    }

    public boolean isPreparing() { return preparationState == EndPreparationState.PREPARING; }
    public boolean isPrepared() { return preparationState == EndPreparationState.READY; }
}
