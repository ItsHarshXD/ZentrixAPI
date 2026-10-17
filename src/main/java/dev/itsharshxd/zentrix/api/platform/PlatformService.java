package dev.itsharshxd.zentrix.api.platform;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * What the installed Zentrix is and what it can do on this server.
 *
 * <p>Use it to decide on prerequisites before registering content: a method that exists in the
 * API can still be unavailable because of the installed edition, a missing or outdated dependency,
 * or the server's configuration. {@link #getCapability(String)} says which, and whether it may
 * change without touching the installation.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * PlatformService platform = ZentrixAPI.get().getPlatformService();
 * CapabilityStatus gliders = platform.getCapability(ZentrixCapabilities.GLIDER_DEPLOYMENT);
 * if (!gliders.isAvailable()) {
 *     getLogger().info("Sky drops will fall without a glider: " + gliders.reason());
 * }
 * }</pre>
 *
 * <p>No secret is ever exposed here: no license key, database credential or connection string.
 * Every method may be called from any thread; the answers are snapshots.
 *
 * @since 1.8.0
 */
public interface PlatformService {

    /** The installed Zentrix plugin version, as its {@code plugin.yml} declares it. */
    @NotNull
    String getPluginVersion();

    /** The API contract the installed Zentrix implements; the same as {@code ZentrixAPI#getAPIVersion()}. */
    @NotNull
    String getApiVersion();

    /** The installed edition. */
    @NotNull
    ZentrixEdition getEdition();

    /** The server Zentrix runs on. */
    @NotNull
    ServerPlatform getServer();

    /** Where Zentrix is in its startup and shutdown. */
    @NotNull
    LifecycleState getLifecycleState();

    /** Every optional plugin Zentrix integrates with, installed or not. */
    @NotNull
    Collection<IntegrationStatus> getIntegrations();

    /**
     * One integration by plugin name, case-insensitively.
     *
     * @param name the plugin name, such as {@code "WorldGuard"}
     * @return the integration, or empty when Zentrix does not integrate with that plugin
     */
    @NotNull
    Optional<IntegrationStatus> getIntegration(@NotNull String name);

    /** The storage backend Zentrix keeps profiles and balances in. */
    @NotNull
    StorageStatus getStorageStatus();

    /**
     * One capability.
     *
     * @param id a {@link ZentrixCapabilities} id, or one an addon registered
     * @return its status; {@link CapabilityState#UNKNOWN} when nothing reports that id
     */
    @NotNull
    CapabilityStatus getCapability(@NotNull String id);

    /** Every built-in capability, followed by every capability addons currently report. */
    @NotNull
    Collection<CapabilityStatus> getCapabilities();

    /**
     * Reports a capability of the caller's own, so other addons can depend on it the same way they
     * depend on Zentrix's.
     *
     * <p>The id must be namespaced, {@code namespace:name}, and may not use the {@code zentrix}
     * namespace. The supplier is asked whenever the capability is read, on the reader's thread, so
     * it must be cheap and thread-safe; the id and provider of what it returns are replaced with
     * the registered ones. A supplier that throws reports {@link CapabilityState#FAILED}.
     *
     * @param owner  the reporting plugin; the registration ends when it is disabled
     * @param id     the namespaced capability id
     * @param status the capability's current status
     * @return the registration
     * @throws IllegalArgumentException if the id is not namespaced, uses the {@code zentrix}
     *                                  namespace, or is already registered by an enabled plugin
     */
    @NotNull
    CapabilityRegistration registerCapability(
            @NotNull Plugin owner, @NotNull String id, @NotNull Supplier<CapabilityStatus> status);
}
