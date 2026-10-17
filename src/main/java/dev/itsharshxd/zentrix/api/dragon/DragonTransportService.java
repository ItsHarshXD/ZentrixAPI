package dev.itsharshxd.zentrix.api.dragon;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Dragon-bus spawning, flight/passenger queries, endpoint chunks, and glider deployment.
 *
 * <p>Every method that touches entities, worlds, or endpoint chunk state must be called on the
 * Bukkit main thread. That includes {@link #getFlight(UUID)} and {@link #getFlights()}, which read
 * live chunk load state for each route endpoint.</p>
 *
 * @since 1.6.0
 */
public interface DragonTransportService {
    @NotNull UUID spawn(
            @NotNull Collection<? extends Player> passengers,
            @NotNull Location start,
            @NotNull Location destination);
    boolean stop(@NotNull UUID dragonId);
    int stopAll();
    int getActiveCount();
    @NotNull Set<UUID> getActiveIds();
    boolean isActive(@NotNull UUID dragonId);
    @NotNull Optional<DragonFlight> getFlight(@NotNull UUID dragonId);
    @NotNull Collection<DragonFlight> getFlights();
    @NotNull Optional<EnderDragon> getDragon(@NotNull UUID dragonId);
    @NotNull Set<UUID> getPassengers(@NotNull UUID dragonId);
    boolean hasArrived(@NotNull UUID dragonId);

    /**
     * The 1.6.0 name of {@link #isGliderDeploymentAvailable()}, from when gliders came from the
     * separate Matrix Gliders plugin. Gliders are built into Zentrix now; this returns exactly what
     * {@link #isGliderDeploymentAvailable()} does and checks no external plugin.
     *
     * @deprecated since 1.8.0; use {@link #isGliderDeploymentAvailable()}, or
     *             {@link dev.itsharshxd.zentrix.api.platform.PlatformService#getCapability(String)}
     *             with {@link dev.itsharshxd.zentrix.api.platform.ZentrixCapabilities#GLIDER_VISUALS}
     *             to learn why it is not
     */
    @Deprecated(since = "1.8.0")
    boolean isMatrixGlidersAvailable();

    /**
     * Whether {@link #deployGlider(Player)} can open a glider on this server.
     *
     * <p>Deploying through this service is a Premium feature: it is {@code false} on the free
     * edition, where gliding exists only as part of Zentrix's own dragon-bus and sky-drop flows.
     * Gliders are built into Zentrix, so no other plugin is involved.
     *
     * @since 1.8.0
     */
    default boolean isGliderDeploymentAvailable() {
        return isMatrixGlidersAvailable();
    }

    /**
     * Opens a player's built-in glider where they are. Main thread only.
     *
     * @return whether the player glides now; always {@code false} while
     *         {@link #isGliderDeploymentAvailable()} is {@code false}
     */
    boolean deployGlider(@NotNull Player player);
}
