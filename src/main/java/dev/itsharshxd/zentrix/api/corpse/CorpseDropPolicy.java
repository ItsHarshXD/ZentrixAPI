package dev.itsharshxd.zentrix.api.corpse;

/** When stored corpse inventory is released into the world. */
public enum CorpseDropPolicy {
    KILL_ONLY,
    DESPAWN_ONLY,
    BOTH,
    NONE;

    public boolean dropsOnKill() { return this == KILL_ONLY || this == BOTH; }
    public boolean dropsOnDespawn() { return this == DESPAWN_ONLY || this == BOTH; }
}
