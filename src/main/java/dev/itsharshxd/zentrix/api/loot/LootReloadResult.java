package dev.itsharshxd.zentrix.api.loot;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/** Result of reloading or restoring one persisted loot pool. */
public record LootReloadResult(boolean success, @NotNull Optional<String> failureDetail) {
    public LootReloadResult {
        failureDetail = failureDetail == null ? Optional.empty() : failureDetail;
    }
}
