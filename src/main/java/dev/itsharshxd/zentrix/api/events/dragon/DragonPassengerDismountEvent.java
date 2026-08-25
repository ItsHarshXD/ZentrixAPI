package dev.itsharshxd.zentrix.api.events.dragon;

import dev.itsharshxd.zentrix.api.dragon.DragonFlight;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired after a player leaves a tracked bus and glider deployment has been attempted. */
public final class DragonPassengerDismountEvent extends ZentrixEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final DragonFlight flight;
    private final Player player;
    private final boolean gliderDeployed;
    public DragonPassengerDismountEvent(
            @NotNull DragonFlight flight,
            @NotNull Player player,
            boolean gliderDeployed) {
        this.flight = flight; this.player = player; this.gliderDeployed = gliderDeployed;
    }
    @NotNull public DragonFlight getFlight() { return flight; }
    @NotNull public Player getPlayer() { return player; }
    public boolean isGliderDeployed() { return gliderDeployed; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
