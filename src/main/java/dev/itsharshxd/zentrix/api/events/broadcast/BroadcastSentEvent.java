package dev.itsharshxd.zentrix.api.events.broadcast;

import dev.itsharshxd.zentrix.api.broadcast.BroadcastType;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a broadcast was sent. A notification; nothing about the delivery can change.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class BroadcastSentEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String broadcastId;
    private final BroadcastType type;
    private final BroadcastDeliverEvent.Origin origin;
    private final Set<UUID> recipients;

    public BroadcastSentEvent(
            @NotNull String broadcastId,
            @NotNull BroadcastType type,
            @NotNull BroadcastDeliverEvent.Origin origin,
            @NotNull Set<UUID> recipients) {
        super(false);
        this.broadcastId = broadcastId;
        this.type = type;
        this.origin = origin;
        this.recipients = Set.copyOf(recipients);
    }

    /** The broadcast's id. */
    @NotNull
    public String getBroadcastId() {
        return broadcastId;
    }

    /** How it was shown. */
    @NotNull
    public BroadcastType getType() {
        return type;
    }

    /** Where the broadcast comes from and what sent it. */
    @NotNull
    public BroadcastDeliverEvent.Origin getOrigin() {
        return origin;
    }

    /** The players it was sent to. */
    @NotNull
    public Set<UUID> getRecipients() {
        return recipients;
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
