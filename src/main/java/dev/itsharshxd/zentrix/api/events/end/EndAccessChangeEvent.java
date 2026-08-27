package dev.itsharshxd.zentrix.api.events.end;

import dev.itsharshxd.zentrix.api.end.EndToggleRequest;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class EndAccessChangeEvent extends ZentrixGameEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean previousEnabled;
    private final EndToggleRequest request;
    private boolean cancelled;
    public EndAccessChangeEvent(@NotNull ZentrixGame game, boolean previousEnabled,
                                @NotNull EndToggleRequest request) {
        super(game); this.previousEnabled = previousEnabled; this.request = request;
    }
    public boolean wasEnabled() { return previousEnabled; }
    @NotNull public EndToggleRequest getRequest() { return request; }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
