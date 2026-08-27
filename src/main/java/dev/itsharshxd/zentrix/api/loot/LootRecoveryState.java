package dev.itsharshxd.zentrix.api.loot;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/** Files and state behind a pool's last-known-good recovery snapshot. */
public record LootRecoveryState(
        @NotNull Path primaryFile,
        @NotNull Path recoveryFile,
        boolean primaryAvailable,
        boolean recoveryAvailable,
        boolean activeFromRecovery,
        @NotNull Optional<Instant> recoveryLastModified) {

    public LootRecoveryState {
        failureIfNull(primaryFile, "primaryFile");
        failureIfNull(recoveryFile, "recoveryFile");
        recoveryLastModified = recoveryLastModified == null
                ? Optional.empty()
                : recoveryLastModified;
    }

    private static void failureIfNull(Object value, String name) {
        if (value == null) throw new IllegalArgumentException(name + " cannot be null");
    }
}
