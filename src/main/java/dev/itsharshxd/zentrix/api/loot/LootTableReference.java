package dev.itsharshxd.zentrix.api.loot;

import org.jetbrains.annotations.NotNull;

/** Stable reference to a discoverable loot table. */
public record LootTableReference(
        @NotNull LootTableCategory category,
        @NotNull String id,
        @NotNull String displayName) {
}
