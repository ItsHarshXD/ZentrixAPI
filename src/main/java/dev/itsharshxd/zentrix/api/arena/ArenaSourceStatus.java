package dev.itsharshxd.zentrix.api.arena;

/** Result status for a dynamic source-arena operation. */
public enum ArenaSourceStatus {
    REGISTERED,
    REPLACED,
    UNREGISTERED,
    ALREADY_REGISTERED,
    NOT_REGISTERED,
    INVALID_CONFIG,
    WORLD_UNAVAILABLE,
    SOURCE_BUSY,
    UNSUPPORTED,
    INTERNAL_ERROR
}
