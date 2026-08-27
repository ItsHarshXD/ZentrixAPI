package dev.itsharshxd.zentrix.api.loot;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/** Result of validating and atomically saving a loot-pool editor snapshot. */
public record LootSaveResult(
        boolean success,
        @NotNull LootValidationResult validation,
        @NotNull Optional<String> failureDetail) {

    public LootSaveResult {
        failureDetail = failureDetail == null ? Optional.empty() : failureDetail;
    }
}
