package dev.itsharshxd.zentrix.api.placeholder;

import dev.itsharshxd.zentrix.api.addon.Registration;
import java.util.Optional;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Lets addons add their own values to Zentrix's {@code %zentrix_...%} placeholders.
 *
 * <p>An addon registers a namespace and a resolver; its values render as
 * {@code %zentrix_<namespace>:<key>%} everywhere Zentrix placeholders work - scoreboards, chat
 * formats, nametags, and any other plugin that renders PlaceholderAPI text. The {@code :} keeps
 * them apart from Zentrix's own placeholders, so an addon can never shadow or change those.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * ZentrixAPI.get().getPlaceholderService().register(this, "bounty",
 *         (player, key) -> key.equals("amount") && player != null
 *                 ? String.valueOf(bounties.get(player.getUniqueId()))
 *                 : null);
 * // %zentrix_bounty:amount%
 * }</pre>
 *
 * @since 1.8.0
 */
public interface PlaceholderService {

    /**
     * Registers an addon's placeholders under a namespace.
     *
     * @param owner     the registering plugin; the placeholders are removed when it is disabled
     * @param namespace lowercase letters, digits, {@code _} and {@code -}, such as the addon's id
     * @param resolver  supplies the values
     * @return the registration, to remove the placeholders early
     * @throws IllegalArgumentException if the namespace is malformed, is {@code zentrix}, or is
     *                                  already registered by an enabled plugin
     */
    @NotNull
    Registration register(
            @NotNull Plugin owner, @NotNull String namespace, @NotNull PlaceholderResolver resolver);

    /** The namespaces currently registered. */
    @NotNull
    Set<String> getNamespaces();

    /**
     * Renders one addon placeholder directly, without PlaceholderAPI.
     *
     * @param player     the player to render it for, or {@code null}
     * @param identifier {@code namespace:key}, without the {@code %zentrix_} and {@code %}
     * @return the value, or empty when no registered resolver supplies one
     */
    @NotNull
    Optional<String> resolve(@Nullable OfflinePlayer player, @NotNull String identifier);
}
