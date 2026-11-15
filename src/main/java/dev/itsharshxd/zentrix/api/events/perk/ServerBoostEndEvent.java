package dev.itsharshxd.zentrix.api.events.perk;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.perk.ServerBoost;
import java.time.Instant;
import java.util.Objects;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a server-wide boost stops applying on this server, as its end is announced to the
 * players here. Every server sharing the database fires it for its own players.
 *
 * <p>Main thread. Only fired while a listener is registered.
 *
 * @since 1.16.0
 */
public final class ServerBoostEndEvent extends ZentrixEvent {

    /** Why the boost ended. */
    public enum Reason {
        /** Ended before its time. */
        ENDED,
        /** Its time ran out. */
        EXPIRED
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final ServerBoost boost;
    private final Reason reason;

    public ServerBoostEndEvent(@NotNull ServerBoost boost, @NotNull Reason reason) {
        super(false);
        this.boost = Objects.requireNonNull(boost, "boost");
        this.reason = Objects.requireNonNull(reason, "reason");
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

    /** When it was due to end. */
    @NotNull
    public Instant getExpiresAt() {
        return boost.expiresAt();
    }

    @NotNull
    public Reason getReason() {
        return reason;
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
