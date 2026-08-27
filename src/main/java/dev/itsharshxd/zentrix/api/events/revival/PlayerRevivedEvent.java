package dev.itsharshxd.zentrix.api.events.revival;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired after a teammate has successfully returned as a living participant. */
public final class PlayerRevivedEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player reviver;
    private final Player target;
    private final Location revivalLocation;
    private final boolean restoredClassItems;
    private final boolean restoredLostLoot;

    public PlayerRevivedEvent(
            @NotNull ZentrixGame game,
            @NotNull Player reviver,
            @NotNull Player target,
            @NotNull Location revivalLocation,
            boolean restoredClassItems,
            boolean restoredLostLoot) {
        super(game);
        this.reviver = reviver;
        this.target = target;
        this.revivalLocation = revivalLocation.clone();
        this.restoredClassItems = restoredClassItems;
        this.restoredLostLoot = restoredLostLoot;
    }

    @NotNull public Player getReviver() { return reviver; }
    @NotNull public Player getTarget() { return target; }
    @NotNull public Location getRevivalLocation() { return revivalLocation.clone(); }
    public boolean restoredClassItems() { return restoredClassItems; }
    public boolean restoredLostLoot() { return restoredLostLoot; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
