package dev.itsharshxd.zentrix.api;

import dev.itsharshxd.zentrix.api.addon.ZentrixAddon;

import org.jetbrains.annotations.NotNull;

/**
 * Holds the ZentrixAPI implementation for the core plugin.
 * <p>
 * Addons should use {@link ZentrixAPI#get()} instead. This class is used by the
 * core plugin and by {@link ZentrixAddon} during addon startup and shutdown.
 * </p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Get the API instance
 * ZentrixAPI api = ZentrixAPI.get();
 * api.getGameService().getActiveGames();
 * }</pre>
 *
 * <h2>Direct use</h2>
 * <p>
 * Direct use is limited to core startup and shutdown, or to advanced cases such
 * as manual API registration and unregistration.
 * </p>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see ZentrixAPI#get()
 * @see ZentrixAPI#isAvailable()
 */
public final class ZentrixProvider {

    private static ZentrixAPI instance;

    /**
     * Private constructor to prevent instantiation.
     */
    private ZentrixProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * Returns the ZentrixAPI instance.
     * <p>
     * Addons should call {@link ZentrixAPI#get()} instead.
     * </p>
     *
     * @return The API instance
     * @throws IllegalStateException if the API has not been initialized yet
     * @see ZentrixAPI#get()
     */
    @NotNull
    public static ZentrixAPI get() {
        ZentrixAPI api = instance;
        if (api == null) {
            throw new IllegalStateException(
                    "ZentrixAPI is not initialized! " +
                    "Ensure that the Zentrix plugin is enabled and loaded before your addon. " +
                    "Add 'Zentrix' as a dependency in your plugin.yml."
            );
        }
        return api;
    }

    /**
     * Returns whether the ZentrixAPI is available.
     * <p>
     * Addons should call {@link ZentrixAPI#isAvailable()} instead.
     * </p>
     *
     * @return {@code true} if the API is initialized and ready to use
     * @see ZentrixAPI#isAvailable()
     */
    public static boolean isAvailable() {
        return instance != null;
    }

    /**
     * Registers the API implementation used by the core plugin.
     * <p>
     * <b>Internal method. Addons must not call this.</b>
     * </p>
     * <p>
     * The core plugin calls this during startup. Calling it from an addon has no
     * useful effect and may cause problems.
     * </p>
     *
     * @param api The API implementation to register
     * @throws IllegalArgumentException if api is null
     * @throws IllegalStateException if API is already registered
     */
    public static void register(@NotNull ZentrixAPI api) {
        if (api == null) {
            throw new IllegalArgumentException("API instance cannot be null");
        }
        if (instance != null) {
            throw new IllegalStateException("ZentrixAPI is already registered");
        }
        instance = api;
    }

    /**
     * Removes the API implementation.
     * <p>
     * <b>Internal method. Addons must not call this.</b>
     * </p>
     * <p>
     * The core plugin calls this during shutdown.
     * </p>
     */
    public static void unregister() {
        instance = null;
    }
}
