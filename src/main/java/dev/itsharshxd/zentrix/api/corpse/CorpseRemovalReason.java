package dev.itsharshxd.zentrix.api.corpse;

/** Terminal reason a corpse left tracking. */
public enum CorpseRemovalReason {
    KILLED,
    EXPIRED,
    GAME_CLEANUP,
    WORLD_CLEANUP,
    API,
    INVALID
}
