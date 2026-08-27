package dev.itsharshxd.zentrix.api.loot;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.Nullable;

/** Supported block-container families. */
public enum LootContainerType {
    CHEST,
    TRAPPED_CHEST,
    BARREL,
    SHULKER_BOX;

    @Nullable
    public static LootContainerType fromMaterial(@Nullable Material material) {
        if (material == Material.CHEST) return CHEST;
        if (material == Material.TRAPPED_CHEST) return TRAPPED_CHEST;
        if (material == Material.BARREL) return BARREL;
        return material != null && Tag.SHULKER_BOXES.isTagged(material) ? SHULKER_BOX : null;
    }
}
