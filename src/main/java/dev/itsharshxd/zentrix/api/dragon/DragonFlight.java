package dev.itsharshxd.zentrix.api.dragon;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/** Immutable route, passenger, endpoint, and lifecycle snapshot of one dragon bus. */
public record DragonFlight(
        @NotNull UUID dragonId,
        @NotNull Location start,
        @NotNull Location destination,
        @NotNull Set<UUID> passengers,
        @NotNull List<DragonEndpointChunk> endpointChunks,
        @NotNull DragonFlightStatus status,
        long startedAtMillis,
        long completedAtMillis) {

    public DragonFlight {
        start = start.clone();
        destination = destination.clone();
        passengers = Set.copyOf(passengers);
        endpointChunks = List.copyOf(endpointChunks);
    }
    @Override public Location start() { return start.clone(); }
    @Override public Location destination() { return destination.clone(); }
    public boolean active() { return status == DragonFlightStatus.FLYING; }
    public boolean arrived() { return status == DragonFlightStatus.ARRIVED; }
}
