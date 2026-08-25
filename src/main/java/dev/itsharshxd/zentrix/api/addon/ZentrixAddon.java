package dev.itsharshxd.zentrix.api.addon;

import dev.itsharshxd.zentrix.api.ZentrixAPI;
import dev.itsharshxd.zentrix.api.ZentrixProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * Base class for Zentrix addons.
 * <p>
 * Extend this class from an addon's main class. It checks API availability,
 * verifies the required version, and registers the addon during startup.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * public class MyAddon extends ZentrixAddon {
 *
 *     @Override
 *     protected void onAddonEnable() {
 *         // Initialize the addon
 *         getLogger().info("MyAddon enabled!");
 *
 *         // Access the API
 *         ZentrixAPI api = ZentrixAPI.get();
 *         api.getGameService().getActiveGames();
 *     }
 *
 *     @Override
 *     protected void onAddonDisable() {
 *         // Clean up addon state
 *         getLogger().info("MyAddon disabled!");
 *     }
 *
 *     @Override
 *     protected String getRequiredAPIVersion() {
 *         return "1.0.0"; // Minimum required API version
 *     }
 * }
 * }</pre>
 *
 * <h2>API access</h2>
 * <p>
 * Use {@link ZentrixAPI#get()} to access the API from anywhere in the addon.
 * </p>
 *
 * <h2>plugin.yml configuration</h2>
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
     * Final implementation of {@link #onEnable()}.
     * <p>
     * It checks API availability and version compatibility, registers the addon,
     * and then calls {@link #onAddonEnable()} for addon-specific initialization.
     * </p>
     */
    @Override
    public final void onEnable() {
        // Check API availability
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

        // Verify API compatibility
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

        // Register the addon
        try {
            ZentrixProvider.get().getAddonManager().registerAddon(this);
            zentrixEnabled = true;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to register addon with Zentrix", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Run addon enable logic
        try {
            onAddonEnable();
            getLogger().info(getName() + " v" + getDescription().getVersion() + " has been enabled!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error during addon enable", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Final implementation of {@link #onDisable()}.
     * <p>
     * It calls {@link #onAddonDisable()} and then unregisters the addon.
     * </p>
     */
    @Override
    public final void onDisable() {
        if (zentrixEnabled) {
            // Run addon disable logic first
            try {
                onAddonDisable();
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Error during addon disable", e);
            }

            // Unregister the addon
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
     * Called after the addon passes the startup checks.
     * <p>
     * Override this method to initialize the addon. The API is available and
     * the addon has already been registered with Zentrix.
     * </p>
     * <p>
     * Use {@link ZentrixAPI#get()} to access the API here.
     * </p>
     */
    protected abstract void onAddonEnable();

    /**
     * Called while the addon is being disabled.
     * <p>
     * Override this method to clean up addon state. It runs before the addon is
     * unregistered from Zentrix.
     * </p>
     * <p>
     * The default implementation is empty.
     * </p>
     */
    protected void onAddonDisable() {
        // No cleanup by default; subclasses may override
    }

    /**
     * Returns the addon's unique identifier.
     * <p>
     * The default is the plugin name in lowercase. Override this method to use
     * a different identifier.
     * </p>
     *
     * @return The addon ID (never null)
     */
    @NotNull
    public String getAddonId() {
        return getName().toLowerCase();
    }

    /**
     * Returns the minimum required ZentrixAPI version.
     * <p>
     * Override this method to specify the minimum API version the addon needs.
     * The default is "1.0.0".
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
     * Returns the ZentrixAPI instance.
     * <p>
     * Use {@link ZentrixAPI#get()} instead. This method remains for backwards
     * compatibility.
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
     * Returns whether the ZentrixAPI is currently available.
     * <p>
     * Use {@link ZentrixAPI#isAvailable()} when checking availability outside
     * the enable phase.
     * </p>
     *
     * @return {@code true} if the API is available
     * @see ZentrixAPI#isAvailable()
     */
    protected final boolean isAPIAvailable() {
        return apiAvailable && ZentrixProvider.isAvailable();
    }

    /**
     * Returns whether this addon is registered with Zentrix.
     *
     * @return {@code true} if the addon is registered and enabled
     */
    public final boolean isZentrixEnabled() {
        return zentrixEnabled;
    }

    /**
     * Compares two version strings for compatibility.
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

            // Compare major versions
            if (curMajor < reqMajor) {
                return false;
            }

            // Compare minor versions when the major versions match
            if (curMajor == reqMajor && reqParts.length > 1 && curParts.length > 1) {
                int reqMinor = Integer.parseInt(reqParts[1]);
                int curMinor = Integer.parseInt(curParts[1]);
                return curMinor >= reqMinor;
            }

            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // Treat unparsable versions as compatible and log a warning
            getLogger().warning("Could not parse version strings for compatibility check. " +
                    "Required: " + required + ", Current: " + current);
            return true;
        }
    }
}
