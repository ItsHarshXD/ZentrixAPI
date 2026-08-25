package dev.itsharshxd.zentrix.api.locale;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Access to Zentrix's active locale and text formatting pipeline.
 *
 * <p>Addons should use this service when they need messages to match the
 * server's current Zentrix locale, prefix, small-caps setting, hex colors, and
 * legacy color handling without depending on Zentrix internals.</p>
 *
 * <h2>Inline flags</h2>
 *
 * <p>A locale value may start with whitespace-separated flags, in any order and
 * any combination. Every method here removes them before formatting, so they
 * never reach a player:</p>
 * <ul>
 *   <li>{@code !disabled} - the owner asked for this text not to be delivered to
 *       chat or console. It stays available everywhere else, so a GUI, item, or
 *       title built from the same key still renders normally.</li>
 *   <li>{@code !no-prefix} - never prepend the Zentrix prefix, even when the
 *       caller asked for a prefixed message.</li>
 *   <li>{@code !no-small-caps} - keep the original capitalization regardless of
 *       the server's small-caps setting.</li>
 * </ul>
 *
 * <p>{@code !no-prefix} and {@code !no-small-caps} are applied by the formatting
 * methods on their own. {@code !disabled} cannot be, because a returned string
 * or component carries no way to say "do not send me" - so prefer
 * {@link #send(Audience, String, Object...)} and
 * {@link #sendWithoutPrefix(Audience, String, Object...)} for chat and console
 * delivery. Only reach for {@link #isDisabled(String)} when the message has to be
 * assembled by hand, such as when click or hover components are appended to it.</p>
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
     * The lines a list-valued locale key holds, unformatted.
     *
     * <p>Everything a GUI shows more than one line of — item lore, click instructions, multi-line
     * descriptions — is a list in the locale file. Each line still carries its {@code &} colour
     * codes and placeholders, so run it through
     * {@link #formatRawComponent(String, boolean, Object...)} with whatever placeholders belong to
     * it. An addon building a menu that has to look like a Zentrix one wants this rather than its
     * own copy of the wording.
     *
     * <p>A key that is missing, or holds a single value rather than a list, returns an empty list.
     * So does a Zentrix build older than 1.13.0.
     *
     * @param key locale key
     * @return the raw lines, never null
     * @since 1.13.0
     */
    @NotNull
    default java.util.List<String> getStringList(@NotNull String key) {
        return java.util.List.of();
    }

    /**
     * One locale value exactly as the file holds it, with no formatting applied.
     *
     * <p>For the handful of locale entries that are data rather than text — a wrapping width, a
     * separator — where running them through the colour pipeline would be wrong.
     *
     * <p>Returns an empty string for a missing key, and on a Zentrix build older than 1.13.0.
     *
     * @param key locale key
     * @return the raw value, never null
     * @since 1.13.0
     */
    @NotNull
    default String getRaw(@NotNull String key) {
        return "";
    }

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

    /**
     * Returns whether a locale value carries the {@code !disabled} inline flag.
     *
     * <p>Only chat and console delivery honors the flag. Every getter on this
     * service keeps returning the formatted text, so the same key still works in
     * GUIs, item text, titles, and subtitles.</p>
     *
     * <p>A Zentrix build older than 1.8.0 has no inline flags, so it reports every
     * key as enabled.</p>
     *
     * @param key locale key
     * @return {@code true} when the value opts out of chat and console delivery
     * @since 1.8.0
     */
    default boolean isDisabled(@NotNull String key) {
        return false;
    }

    /**
     * Returns whether an addon-owned raw message carries the {@code !disabled}
     * inline flag, so text from an addon's own config can support the flag the
     * same way a Zentrix locale value does.
     *
     * <p>A Zentrix build older than 1.8.0 has no inline flags, so it reports every
     * message as enabled.</p>
     *
     * @param message raw message that may start with inline flags
     * @return {@code true} when the message opts out of chat and console delivery
     * @since 1.8.0
     */
    default boolean isRawDisabled(@NotNull String message) {
        return false;
    }

    /**
     * Sends a prefixed locale message to a player or the console, unless the value
     * opted out with {@code !disabled}.
     *
     * @param recipient player, console, or any other Adventure audience
     * @param key locale key
     * @param placeholders placeholder key/value pairs
     * @since 1.8.0
     */
    default void send(@NotNull Audience recipient, @NotNull String key, Object... placeholders) {
        if (!isDisabled(key)) {
            recipient.sendMessage(getComponent(key, placeholders));
        }
    }

    /**
     * Sends an unprefixed locale message to a player or the console, unless the
     * value opted out with {@code !disabled}.
     *
     * @param recipient player, console, or any other Adventure audience
     * @param key locale key
     * @param placeholders placeholder key/value pairs
     * @since 1.8.0
     */
    default void sendWithoutPrefix(@NotNull Audience recipient, @NotNull String key, Object... placeholders) {
        if (!isDisabled(key)) {
            recipient.sendMessage(getComponentWithoutPrefix(key, placeholders));
        }
    }
}
