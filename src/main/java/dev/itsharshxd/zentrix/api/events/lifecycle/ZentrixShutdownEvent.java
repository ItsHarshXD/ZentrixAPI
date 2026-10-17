package dev.itsharshxd.zentrix.api.events.lifecycle;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called while Zentrix shuts down, just before its API is unregistered.
 *
 * <p>Every service still answers while this runs, so it is the last point at which an addon can
 * read or release its Zentrix state. Shutdown cannot be cancelled, and Zentrix still releases
 * everything an addon registered whether or not it does so here.
 *
 * @since 1.8.0
 */
public final class ZentrixShutdownEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public ZentrixShutdownEvent() {
        super(false);
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
