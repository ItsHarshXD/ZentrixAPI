package dev.itsharshxd.zentrix.api.events.game;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class DeathmatchReadyEvent extends ZentrixGameEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean successful;
    public DeathmatchReadyEvent(@NotNull ZentrixGame game, boolean successful) {
        super(game); this.successful = successful;
    }
    public boolean isSuccessful() { return successful; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
