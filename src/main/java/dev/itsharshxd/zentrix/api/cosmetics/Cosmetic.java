package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One cosmetic as menus show it, of a built-in category or an addon's type. A snapshot: later
 * edits do not change it.
 *
 * <p>Zentrix's own cosmetics have no rarity or price: they are granted, or unlocked by their
 * permission. An addon's carry whatever its {@link CosmeticDefinition} sets.
 *
 * @param type       the type it belongs to
 * @param id         the stable ID ownership is stored under
 * @param name       the display name, in Zentrix's colour format
 * @param icon       the icon menus show it with; a copy
 * @param lore       the description lines, in Zentrix's colour format
 * @param rarity     the rarity label, in Zentrix's colour format, or null for none
 * @param permission the permission that unlocks it without owning it, or null for none
 * @param price      what it costs in Zentrix's currency, or null when it is not for sale
 * @param source     the name of the plugin that provides it, {@code Zentrix} for built-in ones
 * @since 1.11.0
 */
public record Cosmetic(
        @NotNull CosmeticTypeKey type,
        @NotNull String id,
        @NotNull String name,
        @NotNull ItemStack icon,
        @NotNull List<String> lore,
        @Nullable String rarity,
        @Nullable String permission,
        @Nullable Double price,
        @NotNull String source) {

    public Cosmetic {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        icon = Objects.requireNonNull(icon, "icon").clone();
        lore = List.copyOf(lore);
        Objects.requireNonNull(source, "source");
    }

    /** A copy of the icon. */
    @Override
    @NotNull
    public ItemStack icon() {
        return icon.clone();
    }

    /** The rarity label, when there is one. */
    @NotNull
    public Optional<String> getRarity() {
        return Optional.ofNullable(rarity);
    }

    /** The permission that unlocks it, when there is one. */
    @NotNull
    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    /** What it costs; empty when it is not for sale. */
    @NotNull
    public OptionalDouble getPrice() {
        return price == null ? OptionalDouble.empty() : OptionalDouble.of(price);
    }

    /** Whether it can be bought with Zentrix's currency. */
    public boolean isForSale() {
        return price != null;
    }

    /** Whether Zentrix itself provides it. */
    public boolean isBuiltIn() {
        return type.isBuiltIn();
    }
}
