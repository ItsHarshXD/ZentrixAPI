package dev.itsharshxd.zentrix.api.loot;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/** One generated item appearance and its destination slot. */
public record LootPlacement(
        @NotNull String entryId,
        int quantity,
        @NotNull String containerId,
        @NotNull LootContainerType containerType,
        int slot,
        @NotNull ItemStack item) {

    public LootPlacement {
        item = item.clone();
    }

    @Override public ItemStack item() { return item.clone(); }
}
