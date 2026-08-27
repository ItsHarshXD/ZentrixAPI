package dev.itsharshxd.zentrix.api.loot;

/** Every independently persisted Zentrix loot pool. */
public enum LootPool {
    CORNUCOPIA,
    GAME_MAIN,
    GAME_NETHER,
    GAME_END,
    GAME_DEATHMATCH;

    public boolean isGameLoot() {
        return this != CORNUCOPIA;
    }
}
