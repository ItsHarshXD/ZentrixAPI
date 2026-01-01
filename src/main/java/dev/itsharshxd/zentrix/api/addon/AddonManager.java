package dev.itsharshxd.zentrix.api.addon;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Manages Zentrix addon lifecycle and registration.
 * <p>
 * This manager handles the registration of third-party addons that extend
 * Zentrix functionality. Addons can register themselves to be tracked and
 * managed by the Zentrix plugin.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * AddonManager addonManager = ZentrixProvider.get().getAddonManager();
 *
 * // Check registered addons
 * Collection<AddonInfo> addons = addonManager.getRegisteredAddons();
 * for (AddonInfo addon : addons) {
 *     System.out.println("Addon: " + addon.getName() + " v" + addon.getVersion());
 * }
 *
 * // Check if a specific addon is registered
 * if (addonManager.isAddonRegistered("my-addon")) {
 *     // Addon is active
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface AddonManager {

    /**
     * Registers an addon with Zentrix.
     * <p>
     * <b>Note:</b> If you extend {@link ZentrixAddon}, registration is handled
     * automatically. Only call this directly if you're not using the base class.
     * </p>
     *
     * @param plugin The addon plugin to register
     * @throws IllegalArgumentException if plugin is null
     * @throws IllegalStateException if addon is already registered
     */
    void registerAddon(@NotNull Plugin plugin);

    /**
     * Unregisters an addon from Zentrix.
     * <p>
     * <b>Note:</b> If you extend {@link ZentrixAddon}, unregistration is handled
     * automatically. Only call this directly if you're not using the base class.
     * </p>
     *
     * @param plugin The addon plugin to unregister
     */
    void unregisterAddon(@NotNull Plugin plugin);

    /**
     * Gets information about a registered addon by its ID.
     *
     * @param addonId The addon's unique identifier (typically the plugin name in lowercase)
     * @return Optional containing the addon info, or empty if not found
     */
    @NotNull
    Optional<AddonInfo> getAddon(@NotNull String addonId);

    /**
     * Gets information about a registered addon by its plugin instance.
     *
     * @param plugin The addon plugin
     * @return Optional containing the addon info, or empty if not registered
     */
    @NotNull
    Optional<AddonInfo> getAddon(@NotNull Plugin plugin);

    /**
     * Gets all registered addons.
     *
     * @return An unmodifiable collection of addon information (never null, may be empty)
     */
    @NotNull
    Collection<AddonInfo> getRegisteredAddons();

    /**
     * Checks if an addon is registered by its ID.
     *
     * @param addonId The addon's unique identifier
     * @return {@code true} if the addon is registered
     */
    boolean isAddonRegistered(@NotNull String addonId);

    /**
     * Checks if an addon is registered by its plugin instance.
     *
     * @param plugin The addon plugin
     * @return {@code true} if the addon is registered
     */
    boolean isAddonRegistered(@NotNull Plugin plugin);

    /**
     * Gets the number of registered addons.
     *
     * @return The addon count
     */
    int getAddonCount();

    /**
     * Represents information about a registered addon.
     */
    interface AddonInfo {

        /**
         * Gets the addon's unique identifier.
         * <p>
         * This is typically the plugin name in lowercase.
         * </p>
         *
         * @return The addon ID (never null)
         */
        @NotNull
        String getId();

        /**
         * Gets the addon's display name.
         *
         * @return The addon name (never null)
         */
        @NotNull
        String getName();

        /**
         * Gets the addon's version.
         *
         * @return The version string (never null)
         */
        @NotNull
        String getVersion();

        /**
         * Gets the addon's description.
         *
         * @return The description, or empty string if none
         */
        @NotNull
        String getDescription();

        /**
         * Gets the addon's authors.
         *
         * @return An unmodifiable list of author names (never null, may be empty)
         */
        @NotNull
        Collection<String> getAuthors();

        /**
         * Gets the underlying Bukkit plugin instance.
         *
         * @return The plugin instance (never null)
         */
        @NotNull
        Plugin getPlugin();

        /**
         * Checks if the addon is currently enabled.
         *
         * @return {@code true} if the addon is enabled
         */
        boolean isEnabled();
    }
}
