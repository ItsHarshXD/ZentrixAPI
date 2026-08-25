package dev.itsharshxd.zentrix.api.loot;

import org.jetbrains.annotations.NotNull;

/** Result of importing table-preview entries into one pool. */
public record LootImportResult(int importedItems, @NotNull LootSaveResult saveResult) {
}
