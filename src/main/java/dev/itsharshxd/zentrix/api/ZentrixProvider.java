package dev.itsharshxd.zentrix.api;

import dev.itsharshxd.zentrix.api.addon.ZentrixAddon;

import org.jetbrains.annotations.ApiStatus;
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
 * {@link #register(ZentrixAPI)} and {@link #unregister()} are core bootstrap functionality and
 * are not supported for addons: there is no supported case for registering or replacing the API
 * manually, and doing so does not change how Zentrix behaves. Addons retrieve the API, check
 * {@link dev.itsharshxd.zentrix.api.platform.PlatformService#getLifecycleState()}, and customize
 * Zentrix through the services' own registrations, events and hooks.
 * </p>
 *
 * <h2>Lifecycle</h2>
 * <p>
 * The API becomes available while Zentrix starts, before it has finished loading arenas and
 * saved custom games. {@link dev.itsharshxd.zentrix.api.events.lifecycle.ZentrixReadyEvent}
 * fires once it has; {@link dev.itsharshxd.zentrix.api.events.lifecycle.ZentrixShutdownEvent}
 * fires just before the API is unregistered on shutdown, after which {@link #isAvailable()} is
 * {@code false}.
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
     * The core plugin calls this during startup. Calling it from an addon is unsupported: it has
     * no useful effect and may cause problems.
     * </p>
     *
     * @param api The API implementation to register
     * @throws IllegalArgumentException if api is null
     * @throws IllegalStateException if API is already registered
     */
    @ApiStatus.Internal
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
     * The core plugin calls this during shutdown. Calling it from an addon is unsupported: it
     * would cut every other addon off from the API while Zentrix keeps running.
     * </p>
     */
    @ApiStatus.Internal
    public static void unregister() {
        instance = null;
    }
}
