package dev.itsharshxd.zentrix.api.events.revival;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Cancellable event fired immediately before a teammate is restored to a match. */
public final class PlayerReviveEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player reviver;
    private final Player target;
    private Location revivalLocation;
    private boolean restoreClassItems;
    private boolean restoreLostLoot;
    private boolean cancelled;

    public PlayerReviveEvent(
            @NotNull ZentrixGame game,
            @NotNull Player reviver,
            @NotNull Player target,
            @NotNull Location revivalLocation,
            boolean restoreClassItems,
            boolean restoreLostLoot) {
        super(game);
        this.reviver = reviver;
        this.target = target;
        this.revivalLocation = revivalLocation.clone();
        this.restoreClassItems = restoreClassItems;
        this.restoreLostLoot = restoreLostLoot;
    }

    @NotNull public Player getReviver() { return reviver; }
    @NotNull public Player getTarget() { return target; }
    @NotNull public Location getRevivalLocation() { return revivalLocation.clone(); }
    public void setRevivalLocation(@NotNull Location location) { this.revivalLocation = location.clone(); }
    public boolean shouldRestoreClassItems() { return restoreClassItems; }
    public void setRestoreClassItems(boolean value) { this.restoreClassItems = value; }
    public boolean shouldRestoreLostLoot() { return restoreLostLoot; }
    public void setRestoreLostLoot(boolean value) { this.restoreLostLoot = value; }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
