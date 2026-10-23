package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A kind of cosmetic as the cosmetics menu shows it: one of Zentrix's own categories or one an
 * addon registered. A snapshot.
 *
 * @param key          the type's key
 * @param name         the display name, in Zentrix's colour format
 * @param description  the description lines, in Zentrix's colour format; empty for built-in ones,
 *                     whose description is in the locale file
 * @param icon         the icon of its tile in the cosmetics menu; a copy
 * @param defaultId    the cosmetic used by players who chose none, or null when there is none
 * @param source       the name of the plugin that provides it, {@code Zentrix} for built-in ones
 * @param available    whether it can be used on this server; Zentrix's gliders and cage designs
 *                     are not part of the Free edition
 * @since 1.11.0
 */
public record CosmeticType(
        @NotNull CosmeticTypeKey key,
        @NotNull String name,
        @NotNull List<String> description,
        @NotNull ItemStack icon,
        @Nullable String defaultId,
        @NotNull String source,
        boolean available) {

    public CosmeticType {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(name, "name");
        description = List.copyOf(description);
        icon = Objects.requireNonNull(icon, "icon").clone();
        Objects.requireNonNull(source, "source");
    }

    /** A copy of the icon. */
    @Override
    @NotNull
    public ItemStack icon() {
        return icon.clone();
    }

    /** The cosmetic used by players who chose none, when there is one. */
    @NotNull
    public Optional<String> getDefaultId() {
        return Optional.ofNullable(defaultId);
    }

    /** The built-in category this is; empty for an addon's type. */
    @NotNull
    public Optional<CosmeticCategory> getCategory() {
        return key.getCategory();
    }

    /** Whether Zentrix itself provides it. */
    public boolean isBuiltIn() {
        return key.isBuiltIn();
    }
}
