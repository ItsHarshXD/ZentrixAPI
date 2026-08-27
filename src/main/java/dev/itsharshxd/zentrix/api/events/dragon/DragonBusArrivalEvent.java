package dev.itsharshxd.zentrix.api.events.dragon;

import dev.itsharshxd.zentrix.api.dragon.DragonFlight;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired when a tracked bus reaches its configured destination. */
public final class DragonBusArrivalEvent extends ZentrixEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final DragonFlight flight;
    public DragonBusArrivalEvent(@NotNull DragonFlight flight) { this.flight = flight; }
    @NotNull public DragonFlight getFlight() { return flight; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
