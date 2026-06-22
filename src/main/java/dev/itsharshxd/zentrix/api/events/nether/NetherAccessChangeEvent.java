package dev.itsharshxd.zentrix.api.events.nether;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.nether.NetherToggleRequest;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class NetherAccessChangeEvent extends ZentrixGameEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean previousEnabled;
    private final NetherToggleRequest request;
    private boolean cancelled;
    public NetherAccessChangeEvent(@NotNull ZentrixGame game, boolean previousEnabled,
                                   @NotNull NetherToggleRequest request) {
        super(game); this.previousEnabled = previousEnabled; this.request = request;
    }
    public boolean wasEnabled() { return previousEnabled; }
    @NotNull public NetherToggleRequest getRequest() { return request; }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
