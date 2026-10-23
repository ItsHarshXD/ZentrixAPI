package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Names a cosmetic type: one of Zentrix's own {@link CosmeticCategory categories}, under the
 * {@code zentrix} namespace, or one an addon registered, under the addon's namespace. Written
 * {@code namespace:key}, such as {@code zentrix:gliders} or {@code myhats:hats}.
 *
 * <p>Both parts use {@code a-z}, {@code 0-9}, {@code _} and {@code -}. An addon's cosmetics are
 * stored under its key, so keep it stable across versions.
 *
 * @param namespace who defines the type: {@code zentrix}, or the addon's namespace
 * @param key       the type within its namespace
 * @since 1.11.0
 */
public record CosmeticTypeKey(@NotNull String namespace, @NotNull String key) {

    /** The namespace of Zentrix's own categories. */
    public static final String ZENTRIX = "zentrix";

    private static final Pattern PART = Pattern.compile("[a-z0-9_-]+");

    public CosmeticTypeKey {
        Objects.requireNonNull(namespace, "namespace");
        Objects.requireNonNull(key, "key");
        if (!PART.matcher(namespace).matches()) {
            throw new IllegalArgumentException("Cosmetic type namespaces use a-z, 0-9, _ and -: " + namespace);
        }
        if (!PART.matcher(key).matches()) {
            throw new IllegalArgumentException("Cosmetic type keys use a-z, 0-9, _ and -: " + key);
        }
    }

    /** The key of one of Zentrix's own categories. */
    @NotNull
    public static CosmeticTypeKey of(@NotNull CosmeticCategory category) {
        return new CosmeticTypeKey(ZENTRIX, Objects.requireNonNull(category, "category").getId());
    }

    /**
     * A key in a plugin's own namespace: its name in lowercase, with every character outside
     * {@code a-z}, {@code 0-9}, {@code _} and {@code -} replaced by {@code _}.
     *
     * @param key the type within the plugin's namespace
     */
    @NotNull
    public static CosmeticTypeKey of(@NotNull Plugin plugin, @NotNull String key) {
        return new CosmeticTypeKey(namespaceOf(plugin), key);
    }

    /** The namespace {@link #of(Plugin, String)} gives a plugin's types. */
    @NotNull
    public static String namespaceOf(@NotNull Plugin plugin) {
        return Objects.requireNonNull(plugin, "plugin").getName().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9_-]", "_");
    }

    /**
     * Reads a key written as {@code namespace:key}, ignoring case. A key without a namespace, such
     * as {@code gliders}, names one of Zentrix's own categories.
     *
     * @return empty when the text is not a key
     */
    @NotNull
    public static Optional<CosmeticTypeKey> parse(@Nullable String text) {
        if (text == null) {
            return Optional.empty();
        }
        String lower = text.trim().toLowerCase(Locale.ROOT);
        int separator = lower.indexOf(':');
        String namespace = separator < 0 ? ZENTRIX : lower.substring(0, separator);
        String key = separator < 0 ? lower : lower.substring(separator + 1);
        if (!PART.matcher(namespace).matches() || !PART.matcher(key).matches()) {
            return Optional.empty();
        }
        return Optional.of(new CosmeticTypeKey(namespace, key));
    }

    /** Whether this names one of Zentrix's own categories. */
    public boolean isBuiltIn() {
        return getCategory().isPresent();
    }

    /** The built-in category this names; empty for an addon's type. */
    @NotNull
    public Optional<CosmeticCategory> getCategory() {
        return ZENTRIX.equals(namespace) ? CosmeticCategory.fromId(key) : Optional.empty();
    }

    /** The key as {@code namespace:key}. */
    @NotNull
    public String asString() {
        return namespace + ":" + key;
    }

    @Override
    public String toString() {
        return asString();
    }
}
