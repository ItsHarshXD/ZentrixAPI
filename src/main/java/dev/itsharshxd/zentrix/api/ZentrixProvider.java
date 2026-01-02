package dev.itsharshxd.zentrix.api;

import org.jetbrains.annotations.NotNull;

/**
 * Internal provider for the ZentrixAPI instance.
 * <p>
 * <b>For addon developers:</b> Use {@link ZentrixAPI#get()} instead of this class.
 * The static methods on {@link ZentrixAPI} are the recommended way to access the API.
 * </p>
 *
 * <h2>Recommended Usage</h2>
 * <pre>{@code
 * // Use ZentrixAPI.get() instead (recommended)
 * ZentrixAPI api = ZentrixAPI.get();
 * api.getGameService().getActiveGames();
 * }</pre>
 *
 * <h2>When to use ZentrixProvider directly</h2>
 * <p>
 * This class is primarily used internally by the Zentrix core plugin and the
 * {@link ZentrixAddon} base class. Direct usage is only needed for advanced
 * scenarios like manual API registration/unregistration.
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
     * Gets the ZentrixAPI instance.
     * <p>
     * <b>Prefer using {@link ZentrixAPI#get()} instead.</b>
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
     * Checks if the ZentrixAPI is available.
     * <p>
     * <b>Prefer using {@link ZentrixAPI#isAvailable()} instead.</b>
     * </p>
     *
     * @return {@code true} if the API is initialized and ready to use
     * @see ZentrixAPI#isAvailable()
     */
    public static boolean isAvailable() {
        return instance != null;
    }

    /**
     * Registers the API implementation.
     * <p>
     * <b>Internal method - DO NOT CALL FROM ADDONS.</b>
     * </p>
     * <p>
     * This method is called by the Zentrix core plugin during startup
     * to register the API implementation. Calling this from an addon
     * will have no effect and may cause issues.
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
     * Unregisters the API implementation.
     * <p>
     * <b>Internal method - DO NOT CALL FROM ADDONS.</b>
     * </p>
     * <p>
     * This method is called by the Zentrix core plugin during shutdown
     * to unregister the API implementation.
     * </p>
     */
    public static void unregister() {
        instance = null;
    }
}
