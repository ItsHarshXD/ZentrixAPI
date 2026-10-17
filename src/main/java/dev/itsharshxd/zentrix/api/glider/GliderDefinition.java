package dev.itsharshxd.zentrix.api.glider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One glider of the catalog in {@code content/cosmetics/gliders.yml}. A snapshot: later edits in
 * the glider editor do not change it.
 *
 * @param id         the stable ID cosmetics ownership is stored under
 * @param name       the display name, in Zentrix's colour format
 * @param lore       the description lines, in Zentrix's colour format
 * @param permission the permission granted to players who own it, or null for none
 * @param item       the item worn on the player's back while gliding, as resolved now; a copy
 * @since 1.8.0
 */
public record GliderDefinition(
        @NotNull String id,
        @NotNull String name,
        @NotNull List<String> lore,
        @Nullable String permission,
        @NotNull ItemStack item) {

    public GliderDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        lore = List.copyOf(lore);
        item = Objects.requireNonNull(item, "item").clone();
    }

    /** The permission granted to owners, when there is one. */
    @NotNull
    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    /** A copy of the item worn on the player's back. */
    @Override
    @NotNull
    public ItemStack item() {
        return item.clone();
    }
}
