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
 * live chunk load and plugin-ticket state for each route endpoint.</p>
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
    boolean isMatrixGlidersAvailable();
    boolean deployGlider(@NotNull Player player);
}
