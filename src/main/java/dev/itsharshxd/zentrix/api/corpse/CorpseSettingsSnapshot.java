package dev.itsharshxd.zentrix.api.corpse;

import org.jetbrains.annotations.NotNull;

/** Read-only active corpse settings, including any API drop-policy override. */
public record CorpseSettingsSnapshot(
        boolean enabled,
        @NotNull String displayNameFormat,
        int despawnSeconds,
        boolean warningEnabled,
        int warningThresholdSeconds,
        @NotNull CorpseDropPolicy dropPolicy,
        boolean spawnOnQuit,
        boolean spawnOnKick,
        boolean spawnOnCommand) {
}
