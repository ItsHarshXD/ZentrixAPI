package dev.itsharshxd.zentrix.api.events.end;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Fired for End portal travel inside one runtime match, before the player is moved.
 *
 * <p>{@link Direction#ENTER_END} destinations are the runtime End's standard arrival
 * platform. {@link Direction#EXIT_END} destinations are the leaving player's resolved
 * match-owned respawn point, or the paired game world's safe spawn.
 */
public final class ZentrixEndPortalEvent extends ZentrixGameEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Direction direction;
    private Location destination;
    private boolean cancelled;
    public ZentrixEndPortalEvent(@NotNull ZentrixGame game, @NotNull Player player,
                                 @NotNull Direction direction, @NotNull Location destination) {
        super(game); this.player = player; this.direction = direction; this.destination = destination.clone();
    }
    @NotNull public Player getPlayer() { return player; }
    @NotNull public Direction getDirection() { return direction; }
    @NotNull public Location getDestination() { return destination.clone(); }
    public void setDestination(@NotNull Location destination) { this.destination = destination.clone(); }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
    public enum Direction { ENTER_END, EXIT_END }
}
