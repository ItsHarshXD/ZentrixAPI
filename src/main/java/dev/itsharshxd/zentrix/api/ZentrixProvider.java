package dev.itsharshxd.zentrix.api;

import org.jetbrains.annotations.NotNull;

/**
 * Static provider for accessing the ZentrixAPI.
 * <p>
 * Third-party addons use this class to obtain an instance of the {@link ZentrixAPI}
 * for interacting with Zentrix Battle Royale games.
 * </p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * // Check if API is available
 * if (ZentrixProvider.isAvailable()) {
 *     ZentrixAPI api = ZentrixProvider.get();
 *
 *     // Use the API
 *     api.getGameService().getActiveGames();
 * }
 * }</pre>
 *
 * <h2>Important Notes</h2>
 * <ul>
 *   <li>Always check {@link #isAvailable()} before calling {@link #get()} if unsure whether Zentrix is loaded</li>
 *   <li>The API becomes available after Zentrix plugin is fully enabled</li>
 *   <li>The API becomes unavailable when Zentrix plugin is disabled</li>
 * </ul>
 *
 * @author ItsHarshXD
 * @since 1.0.0
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
     * This method should only be called after confirming the API is available
     * using {@link #isAvailable()}, or from within a Zentrix addon that has
     * already been enabled (which guarantees Zentrix is loaded).
     * </p>
     *
     * @return The API instance
     * @throws IllegalStateException if the API has not been initialized yet
     *         (Zentrix plugin is not enabled)
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
     * Use this method to safely check whether the API can be accessed
     * before calling {@link #get()}.
     * </p>
     *
     * <pre>{@code
     * if (ZentrixProvider.isAvailable()) {
     *     ZentrixAPI api = ZentrixProvider.get();
     *     // Safe to use API
     * } else {
     *     // Zentrix is not loaded
     * }
     * }</pre>
     *
     * @return {@code true} if the API is initialized and ready to use,
     *         {@code false} otherwise
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
