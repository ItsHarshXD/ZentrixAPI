package dev.itsharshxd.zentrix.api.addon;

import dev.itsharshxd.zentrix.api.ZentrixAPI;
import dev.itsharshxd.zentrix.api.ZentrixProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * Base class for Zentrix addons.
 * <p>
 * Extend this class in your addon's main class for automatic lifecycle management
 * and seamless integration with the ZentrixAPI. This class handles API availability
 * checks, version compatibility, and addon registration automatically.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * public class MyAddon extends ZentrixAddon {
 *
 *     @Override
 *     protected void onAddonEnable() {
 *         // Your addon initialization code
 *         getLogger().info("MyAddon enabled!");
 *
 *         // Access the API (within addon class)
 *         ZentrixAPI api = ZentrixAPI.get();
 *         api.getGameService().getActiveGames();
 *     }
 *
 *     @Override
 *     protected void onAddonDisable() {
 *         // Your addon cleanup code
 *         getLogger().info("MyAddon disabled!");
 *     }
 *
 *     @Override
 *     protected String getRequiredAPIVersion() {
 *         return "1.0.0"; // Minimum API version required
 *     }
 * }
 * }</pre>
 *
 * <h2>API Access</h2>
 * <p>
 * Use {@link ZentrixAPI#get()} to access the API from anywhere in your addon.
 * This is the single recommended way to obtain the API instance.
 * </p>
 *
 * <h2>plugin.yml Configuration</h2>
 * <p>
 * Your addon's plugin.yml should declare Zentrix as a dependency:
 * </p>
 * <pre>
 * name: MyAddon
 * version: 1.0.0
 * main: com.example.myaddon.MyAddon
 * depend: [Zentrix]
 * api-version: '1.21'
 * </pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see ZentrixAPI#get()
 */
public abstract class ZentrixAddon extends JavaPlugin {

    private boolean zentrixEnabled = false;
    private boolean apiAvailable = false;

    /**
     * Final implementation of onEnable.
     * <p>
     * This method handles ZentrixAPI availability checks, version compatibility,
     * and addon registration. Override {@link #onAddonEnable()} instead to add
     * your addon's initialization logic.
     * </p>
     */
    @Override
    public final void onEnable() {
        // Check if ZentrixAPI is available
        if (!ZentrixProvider.isAvailable()) {
            getLogger().severe("========================================");
            getLogger().severe("Zentrix is not loaded!");
            getLogger().severe("This addon requires Zentrix to function.");
            getLogger().severe("Please ensure Zentrix is installed and enabled.");
            getLogger().severe("Disabling " + getName() + "...");
            getLogger().severe("========================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        apiAvailable = true;

        // Verify API version compatibility
        String requiredVersion = getRequiredAPIVersion();
        String currentVersion = ZentrixProvider.get().getAPIVersion();

        if (!isVersionCompatible(requiredVersion, currentVersion)) {
            getLogger().severe("========================================");
            getLogger().severe("Incompatible ZentrixAPI version!");
            getLogger().severe("Required: " + requiredVersion + " or higher");
            getLogger().severe("Found: " + currentVersion);
            getLogger().severe("Please update Zentrix or this addon.");
            getLogger().severe("Disabling " + getName() + "...");
            getLogger().severe("========================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register with addon manager
        try {
            ZentrixProvider.get().getAddonManager().registerAddon(this);
            zentrixEnabled = true;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to register addon with Zentrix", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Call addon-specific enable logic
        try {
            onAddonEnable();
            getLogger().info(getName() + " v" + getDescription().getVersion() + " has been enabled!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error during addon enable", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Final implementation of onDisable.
     * <p>
     * This method handles addon unregistration and cleanup.
     * Override {@link #onAddonDisable()} instead to add your addon's
     * cleanup logic.
     * </p>
     */
    @Override
    public final void onDisable() {
        if (zentrixEnabled) {
            // Call addon-specific disable logic first
            try {
                onAddonDisable();
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Error during addon disable", e);
            }

            // Unregister from addon manager
            if (ZentrixProvider.isAvailable()) {
                try {
                    ZentrixProvider.get().getAddonManager().unregisterAddon(this);
                } catch (Exception e) {
                    getLogger().log(Level.WARNING, "Failed to unregister addon from Zentrix", e);
                }
            }

            zentrixEnabled = false;
        }

        getLogger().info(getName() + " has been disabled.");
    }

    /**
     * Called when the addon is enabled.
     * <p>
     * Override this method to add your addon's initialization logic.
     * This is called after ZentrixAPI availability and version checks pass,
     * and after the addon has been registered with Zentrix.
     * </p>
     * <p>
     * At this point, you can safely use {@link ZentrixAPI#get()} to access the API.
     * </p>
     */
    protected abstract void onAddonEnable();

    /**
     * Called when the addon is disabled.
     * <p>
     * Override this method to add your addon's cleanup logic.
     * This is called before the addon is unregistered from Zentrix.
     * </p>
     * <p>
     * The default implementation does nothing.
     * </p>
     */
    protected void onAddonDisable() {
        // Default implementation - override if needed
    }

    /**
     * Gets the addon's unique identifier.
     * <p>
     * By default, this returns the plugin name in lowercase.
     * Override this method to provide a custom identifier.
     * </p>
     *
     * @return The addon ID (never null)
     */
    @NotNull
    public String getAddonId() {
        return getName().toLowerCase();
    }

    /**
     * Gets the minimum required ZentrixAPI version.
     * <p>
     * Override this method to specify the minimum API version your addon
     * requires. The default is "1.0.0".
     * </p>
     * <p>
     * Version format follows semantic versioning: MAJOR.MINOR.PATCH
     * Compatibility is determined by the major version number.
     * </p>
     *
     * @return The minimum required API version (e.g., "1.0.0")
     */
    @NotNull
    protected String getRequiredAPIVersion() {
        return "1.0.0";
    }

    /**
     * Gets the ZentrixAPI instance.
     * <p>
     * <b>Prefer using {@link ZentrixAPI#get()} instead.</b>
     * This method is kept for backwards compatibility but using the static
     * method on ZentrixAPI directly is the recommended approach.
     * </p>
     *
     * @return The ZentrixAPI instance (never null)
     * @throws IllegalStateException if called before the addon is enabled
     *         or if Zentrix is not available
     * @see ZentrixAPI#get()
     */
    @NotNull
    protected final ZentrixAPI getAPI() {
        if (!apiAvailable) {
            throw new IllegalStateException(
                    "Cannot access ZentrixAPI before addon is enabled or Zentrix is not available"
            );
        }
        return ZentrixProvider.get();
    }

    /**
     * Checks if the ZentrixAPI is currently available.
     * <p>
     * <b>Prefer using {@link ZentrixAPI#isAvailable()} instead.</b>
     * This is useful for checking API availability outside of the enable phase.
     * </p>
     *
     * @return {@code true} if the API is available
     * @see ZentrixAPI#isAvailable()
     */
    protected final boolean isAPIAvailable() {
        return apiAvailable && ZentrixProvider.isAvailable();
    }

    /**
     * Checks if this addon has been successfully registered with Zentrix.
     *
     * @return {@code true} if the addon is registered and enabled
     */
    public final boolean isZentrixEnabled() {
        return zentrixEnabled;
    }

    /**
     * Checks if two version strings are compatible.
     * <p>
     * Compatibility is determined by comparing major version numbers.
     * A current version is compatible if its major version is greater than
     * or equal to the required major version.
     * </p>
     *
     * @param required The required version
     * @param current  The current version
     * @return {@code true} if versions are compatible
     */
    private boolean isVersionCompatible(String required, String current) {
        try {
            String[] reqParts = required.split("\\.");
            String[] curParts = current.split("\\.");

            int reqMajor = Integer.parseInt(reqParts[0]);
            int curMajor = Integer.parseInt(curParts[0]);

            // Major version must be compatible
            if (curMajor < reqMajor) {
                return false;
            }

            // If same major version, check minor version
            if (curMajor == reqMajor && reqParts.length > 1 && curParts.length > 1) {
                int reqMinor = Integer.parseInt(reqParts[1]);
                int curMinor = Integer.parseInt(curParts[1]);
                return curMinor >= reqMinor;
            }

            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // If parsing fails, assume compatible and log warning
            getLogger().warning("Could not parse version strings for compatibility check. " +
                    "Required: " + required + ", Current: " + current);
            return true;
        }
    }
}
