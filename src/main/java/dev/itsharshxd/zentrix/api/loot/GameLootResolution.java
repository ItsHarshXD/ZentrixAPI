package dev.itsharshxd.zentrix.api.loot;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.jetbrains.annotations.NotNull;

/** Runtime game and game-loot pool owning a world. */
public record GameLootResolution(@NotNull ZentrixGame game, @NotNull GameLootPool pool) {
}
