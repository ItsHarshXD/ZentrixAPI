package dev.itsharshxd.zentrix.api.corpse;

import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Complete inventory and visual data used for an explicitly spawned corpse. */
public record CorpseSpawnRequest(
        @NotNull Location location,
        @NotNull ItemStack[] storedContents,
        @NotNull ItemStack[] visualArmor,
        @Nullable ItemStack visualMainHand,
        @Nullable ItemStack visualOffHand) {

    public CorpseSpawnRequest {
        location = location.clone();
        storedContents = copy(storedContents);
        visualArmor = copy(visualArmor);
        visualMainHand = visualMainHand == null ? null : visualMainHand.clone();
        visualOffHand = visualOffHand == null ? null : visualOffHand.clone();
    }

    @Override public Location location() { return location.clone(); }
    @Override public ItemStack[] storedContents() { return copy(storedContents); }
    @Override public ItemStack[] visualArmor() { return copy(visualArmor); }
    @Override public ItemStack visualMainHand() {
        return visualMainHand == null ? null : visualMainHand.clone();
    }
    @Override public ItemStack visualOffHand() {
        return visualOffHand == null ? null : visualOffHand.clone();
    }

    private static ItemStack[] copy(ItemStack[] items) {
        if (items == null) return new ItemStack[0];
        return Arrays.stream(items).map(item -> item == null ? null : item.clone())
                .toArray(ItemStack[]::new);
    }
}
