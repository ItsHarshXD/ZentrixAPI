package dev.itsharshxd.zentrix.api.events.perk;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.perk.ServerBoost;
import java.time.Instant;
import java.util.Objects;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a server-wide boost starts applying on this server, as it is announced to the
 * players here: on the server that started it at once, and on the others sharing its database as
 * they read it, within half a minute. Boosts already running when Zentrix starts do not fire it.
 *
 * <p>Main thread. Only fired while a listener is registered.
 *
 * @since 1.16.0
 */
public final class ServerBoostStartEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ServerBoost boost;

    public ServerBoostStartEvent(@NotNull ServerBoost boost) {
        super(false);
        this.boost = Objects.requireNonNull(boost, "boost");
    }

    @NotNull
    public ServerBoost getBoost() {
        return boost;
    }

    /** Who or what started it. */
    @NotNull
    public String getSource() {
        return boost.source();
    }

    /** When it ends. */
    @NotNull
    public Instant getExpiresAt() {
        return boost.expiresAt();
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
