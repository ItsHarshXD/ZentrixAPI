package dev.itsharshxd.zentrix.api.arena;

import org.jetbrains.annotations.NotNull;

/** Immutable result of registering, replacing, or unregistering a source arena. */
public record ArenaSourceResult(
        @NotNull ArenaSourceStatus status,
        @NotNull String sourceArenaName,
        @NotNull String detail) {

    public ArenaSourceResult {
        if (status == null) throw new IllegalArgumentException("status cannot be null");
        sourceArenaName = sourceArenaName == null ? "" : sourceArenaName;
        detail = detail == null ? "" : detail;
    }

    public boolean isSuccess() {
        return status == ArenaSourceStatus.REGISTERED
                || status == ArenaSourceStatus.REPLACED
                || status == ArenaSourceStatus.UNREGISTERED
                || status == ArenaSourceStatus.ALREADY_REGISTERED;
    }
}
