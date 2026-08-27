package dev.itsharshxd.zentrix.api.loot;

/** One lazily generated game-loot scope. */
public enum GameLootPool {
    MAIN,
    NETHER,
    END,
    DEATHMATCH;

    public LootPool asLootPool() {
        return LootPool.valueOf("GAME_" + name());
    }
}
