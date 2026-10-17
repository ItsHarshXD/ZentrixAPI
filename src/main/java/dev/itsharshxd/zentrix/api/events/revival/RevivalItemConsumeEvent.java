package dev.itsharshxd.zentrix.api.events.revival;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a revival through the teammate picker completed, just before one Revival Star is
 * taken from the reviver.
 *
 * <p>The revival itself has already happened and {@link PlayerRevivedEvent} has fired; cancelling
 * only keeps the star in the reviver's inventory, for instance to make a revival free or to charge
 * for it some other way. Revivals made through {@code RevivalService#revive} never consume a star
 * and do not fire this event.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class RevivalItemConsumeEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player reviver;
    private final Player target;
    private boolean cancelled;

    public RevivalItemConsumeEvent(@NotNull ZentrixGame game, @NotNull Player reviver, @NotNull Player target) {
        super(game);
        this.reviver = Objects.requireNonNull(reviver, "reviver");
        this.target = Objects.requireNonNull(target, "target");
    }

    /** The player whose star would be consumed. */
    @NotNull
    public Player getReviver() {
        return reviver;
    }

    /** The teammate they revived. */
    @NotNull
    public Player getTarget() {
        return target;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
