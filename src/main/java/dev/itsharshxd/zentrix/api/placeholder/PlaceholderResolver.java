package dev.itsharshxd.zentrix.api.placeholder;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supplies the values of one addon's placeholders.
 *
 * <p>Called whenever one of the addon's placeholders is rendered, on whatever thread renders it -
 * usually the main thread, but not always - so it must be cheap and thread-safe. A resolver that
 * throws renders nothing for that placeholder; it never breaks the text around it.
 *
 * @see PlaceholderService#register
 * @since 1.8.0
 */
@FunctionalInterface
public interface PlaceholderResolver {

    /**
     * @param player the player the text is rendered for, or {@code null} when there is none
     * @param key    the part after {@code namespace:}, lowercase, such as {@code "kills"} for
     *               {@code %zentrix_myaddon:kills%}
     * @return the value, or {@code null} when the key is not one of the addon's placeholders
     */
    @Nullable
    String resolve(@Nullable OfflinePlayer player, @NotNull String key);
}
