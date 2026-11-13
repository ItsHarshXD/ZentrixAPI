package dev.itsharshxd.zentrix.api.gui;

import dev.itsharshxd.zentrix.api.addon.Registration;
import dev.itsharshxd.zentrix.api.gui.background.MenuBackgroundCondition;
import dev.itsharshxd.zentrix.api.gui.background.MenuBackgroundResolver;
import dev.itsharshxd.zentrix.api.gui.background.MenuBackgroundValue;
import dev.itsharshxd.zentrix.api.gui.background.MenuView;
import java.util.Optional;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * GUI ID registration plus configured size, slot, filler, and pagination resolution. Runtime
 * registration is reserved for addon IDs and never replaces a shipped built-in definition.
 *
 * <h2>Menu backgrounds</h2>
 *
 * <p>Since 1.15.0 this service also reaches the resource-pack textures that the {@code background}
 * blocks of {@code guis.yml} give menus. Zentrix decides a menu's texture after the menu has
 * placed its buttons and before it opens, and again whenever the menu redraws, from what it shows.
 * Add-ons can read what an open menu shows ({@link #getOpenMenu}), give server owners conditions
 * and values to write in those rules ({@link #registerBackgroundCondition},
 * {@link #registerBackgroundValue}), choose a texture themselves
 * ({@link #registerBackgroundResolver}), and have an open menu decided again when something their
 * conditions depend on changes ({@link #refreshBackground}).
 *
 * <p>Background methods must be called on the main thread. Registrations end on their own when
 * the registering plugin is disabled.
 *
 * @since 1.6.0
 */
public interface GuiLayoutService {
    @NotNull Set<String> getRegisteredIds();
    boolean isRegistered(@NotNull String guiId);
    boolean isBuiltIn(@NotNull String guiId);
    boolean register(@NotNull GuiLayoutDefinition definition);
    boolean unregister(@NotNull String guiId);
    @NotNull GuiLayout resolve(@NotNull String guiId);
    @NotNull GuiLayout resolve(@NotNull String guiId, int automaticSize);
    void reload();

    // ==========================================
    // Menu backgrounds (1.15.0)
    // ==========================================

    /**
     * What the Zentrix menu a player has open shows, and the texture it was given.
     *
     * @param player the viewer
     * @return the menu, or empty when the player has no Zentrix menu open, or one that placed
     *         nothing through a {@code guis.yml} layout
     * @throws IllegalStateException when called off the main thread
     * @since 1.15.0
     */
    @NotNull
    default Optional<MenuView> getOpenMenu(@NotNull Player player) {
        throw new UnsupportedOperationException("Menu backgrounds require Zentrix API 1.15.0");
    }

    /**
     * Decides the texture of the menu a player has open again, now, and updates its title when it
     * changed. Menus are decided when they open and redraw; call this when something an add-on
     * condition, value or resolver depends on changes while the menu stays open.
     *
     * @param player the viewer
     * @return whether the player had a menu with a background to decide
     * @throws IllegalStateException when called off the main thread
     * @since 1.15.0
     */
    default boolean refreshBackground(@NotNull Player player) {
        throw new UnsupportedOperationException("Menu backgrounds require Zentrix API 1.15.0");
    }

    /**
     * Adds a condition server owners can write in the {@code when} of background rules, as
     * {@link MenuBackgroundCondition#nameOf(Plugin, String) namespace:name}.
     *
     * <p>{@code guis.yml} is read again when the registration starts or ends, so rules that use the
     * condition work as soon as it is registered and are reported as unknown once it is gone.
     * Conditions registered while the server starts are in place before any rule is checked.
     *
     * @param owner     the registering plugin, whose namespace the name is written under
     * @param name      the name within the plugin, using {@code a-z}, {@code 0-9}, {@code _} and
     *                  {@code -}
     * @param condition the condition
     * @return the registration, to remove the condition early
     * @throws IllegalArgumentException when the name is invalid, or the plugin already registered
     *                                  a condition or value under it
     * @since 1.15.0
     */
    @NotNull
    default Registration registerBackgroundCondition(
            @NotNull Plugin owner, @NotNull String name, @NotNull MenuBackgroundCondition condition) {
        throw new UnsupportedOperationException("Menu backgrounds require Zentrix API 1.15.0");
    }

    /**
     * Adds a value server owners can compare in the {@code when} of background rules, as
     * {@link MenuBackgroundCondition#nameOf(Plugin, String) namespace:name}. Conditions and values
     * share one set of names per plugin.
     *
     * @param owner the registering plugin, whose namespace the name is written under
     * @param name  the name within the plugin, using {@code a-z}, {@code 0-9}, {@code _} and
     *              {@code -}
     * @param value the value
     * @return the registration, to remove the value early
     * @throws IllegalArgumentException when the name is invalid, or the plugin already registered
     *                                  a condition or value under it
     * @see #registerBackgroundCondition(Plugin, String, MenuBackgroundCondition)
     * @since 1.15.0
     */
    @NotNull
    default Registration registerBackgroundValue(
            @NotNull Plugin owner, @NotNull String name, @NotNull MenuBackgroundValue value) {
        throw new UnsupportedOperationException("Menu backgrounds require Zentrix API 1.15.0");
    }

    /**
     * Lets an add-on choose the texture of one menu, or of every menu, itself. See
     * {@link MenuBackgroundResolver} for the order resolvers are asked in.
     *
     * <p>Menus that are open keep their title until they redraw or are
     * {@link #refreshBackground(Player) refreshed}. In the Free edition, which keeps
     * {@code guis.yml} at its defaults, the registration is refused and returned inactive.
     *
     * @param owner    the registering plugin
     * @param menuId   the menu's ID in {@code guis.yml}, or {@link MenuBackgroundResolver#ALL_MENUS}
     * @param resolver the resolver
     * @return the registration, to remove the resolver early
     * @since 1.15.0
     */
    @NotNull
    default Registration registerBackgroundResolver(
            @NotNull Plugin owner, @NotNull String menuId, @NotNull MenuBackgroundResolver resolver) {
        throw new UnsupportedOperationException("Menu backgrounds require Zentrix API 1.15.0");
    }
}
