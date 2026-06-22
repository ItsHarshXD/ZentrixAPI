package dev.itsharshxd.zentrix.api.locale;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Access to Zentrix's active locale and text formatting pipeline.
 *
 * <p>Addons should use this service when they need messages to match the
 * server's current Zentrix locale, prefix, small-caps setting, hex colors, and
 * legacy color handling without depending on Zentrix internals.</p>
 *
 * @since 1.5.0
 */
public interface LocaleService {

    /**
     * Gets the active global Zentrix locale name, such as {@code en}.
     *
     * @return active locale name
     */
    @NotNull
    String getCurrentLocale();

    /**
     * Resolves a locale key as a prefixed Adventure component.
     *
     * @param key locale key
     * @param placeholders placeholder key/value pairs
     * @return formatted component with the Zentrix prefix
     */
    @NotNull
    Component getComponent(@NotNull String key, Object... placeholders);

    /**
     * Resolves a locale key as an unprefixed Adventure component.
     *
     * @param key locale key
     * @param placeholders placeholder key/value pairs
     * @return formatted component without the Zentrix prefix
     */
    @NotNull
    Component getComponentWithoutPrefix(@NotNull String key, Object... placeholders);

    /**
     * Resolves a locale key as a prefixed legacy string.
     *
     * @param key locale key
     * @param placeholders placeholder key/value pairs
     * @return formatted string with the Zentrix prefix
     */
    @NotNull
    String getMessage(@NotNull String key, Object... placeholders);

    /**
     * Resolves a locale key as an unprefixed legacy string.
     *
     * @param key locale key
     * @param placeholders placeholder key/value pairs
     * @return formatted string without the Zentrix prefix
     */
    @NotNull
    String getMessageWithoutPrefix(@NotNull String key, Object... placeholders);

    /**
     * Formats a raw addon-owned message through Zentrix's text pipeline.
     *
     * @param message raw message containing Zentrix placeholders/color codes
     * @param includePrefix whether to prepend the active Zentrix prefix
     * @param placeholders placeholder key/value pairs
     * @return formatted component
     */
    @NotNull
    Component formatRawComponent(@NotNull String message, boolean includePrefix, Object... placeholders);

    /**
     * Formats a raw addon-owned message through Zentrix's text pipeline.
     *
     * @param message raw message containing Zentrix placeholders/color codes
     * @param includePrefix whether to prepend the active Zentrix prefix
     * @param placeholders placeholder key/value pairs
     * @return formatted legacy string
     */
    @NotNull
    String formatRaw(@NotNull String message, boolean includePrefix, Object... placeholders);
}
