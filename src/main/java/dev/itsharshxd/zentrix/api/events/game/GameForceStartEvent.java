package dev.itsharshxd.zentrix.api.events.game;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class GameForceStartEvent extends ZentrixGameEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Cause cause;
    private final Player initiator;
    private boolean cancelled;

    public GameForceStartEvent(@NotNull ZentrixGame game, @NotNull Cause cause, Player initiator) {
        super(game);
        this.cause = cause;
        this.initiator = initiator;
    }
    @NotNull public Cause getCause() { return cause; }
    @NotNull public Optional<Player> getInitiator() { return Optional.ofNullable(initiator); }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
    public enum Cause { COMMAND, API }
}
