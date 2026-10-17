package dev.itsharshxd.zentrix.api.settings;

import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * One {@code settings.yml} value Zentrix lets administrators edit in its settings menu, and
 * therefore lets addons edit through {@link SettingsService}.
 *
 * @param id            the setting's ID in the settings menu
 * @param path          its path in {@code settings.yml}, such as {@code cornucopia.enabled}
 * @param type          the kind of value it holds
 * @param minimum       the smallest accepted number, for {@link Type#INTEGER} and {@link Type#DECIMAL}
 * @param maximum       the largest accepted number, for {@link Type#INTEGER} and {@link Type#DECIMAL}
 * @param step          the menu's step size, for numbers
 * @param allowedValues the accepted values, for {@link Type#ENUM}; empty otherwise
 * @since 1.8.0
 */
public record SettingDescriptor(
        @NotNull String id,
        @NotNull String path,
        @NotNull Type type,
        double minimum,
        double maximum,
        double step,
        @NotNull List<String> allowedValues) {

    public SettingDescriptor {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(type, "type");
        allowedValues = List.copyOf(allowedValues);
    }

    /** The kind of value a setting holds, and the Java value {@link SettingsService#set} expects. */
    public enum Type {
        /** A {@link Boolean}. */
        BOOLEAN,
        /** A whole number ({@link Integer} or {@link Long}) within the bounds. */
        INTEGER,
        /** A number within the bounds. */
        DECIMAL,
        /** Free text, in Zentrix's colour format. */
        STRING,
        /** A lowercase identifier. */
        IDENTIFIER,
        /** The name of a file inside the plugin folder. */
        SAFE_FILE,
        /** The name of a schematic file for the Cornucopia. */
        SCHEMATIC_FILE,
        /** The name of a world folder used as a template. */
        WORLD_TEMPLATE,
        /** The name of a locale file. */
        LOCALE_FILE,
        /** A Bukkit material name. */
        MATERIAL,
        /** A sound key. */
        SOUND,
        /** One of {@link SettingDescriptor#allowedValues()}. */
        ENUM,
        /** A {@link org.bukkit.Location}. */
        LOCATION,
        /** A list of {@link org.bukkit.Location}s. */
        LOCATION_LIST,
        /** A {@link org.bukkit.Location}, or null to clear it. */
        NULLABLE_LOCATION
    }
}
