package dev.itsharshxd.zentrix.api.events.compass;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called each time Zentrix is about to draw a teammate tracker's action bar for a player holding
 * it - every {@code CompassTrackerService#getUpdateIntervalTicks()} ticks, and on every change.
 *
 * <p>Replace the message to render the tracker differently, or cancel to leave the action bar to
 * something else this time; the compass itself still points at the target. The message already
 * shows the teammate's name as the viewer may see it, so a replacement built from
 * {@link #getTarget()} should pass names through {@code IdentityService} too. Main thread; only
 * fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CompassTrackerDisplayEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player tracker;
    private final Player target;
    private final int distance;
    private Component message;
    private boolean cancelled;

    public CompassTrackerDisplayEvent(
            @NotNull Player tracker, @Nullable Player target, int distance, @NotNull Component message) {
        super(false);
        this.tracker = tracker;
        this.target = target;
        this.distance = distance;
        this.message = message;
    }

    /** The player holding the tracker. */
    @NotNull
    public Player getTracker() {
        return tracker;
    }

    /** The tracked teammate; empty when there is none, and the message says so. */
    @NotNull
    public Optional<Player> getTarget() {
        return Optional.ofNullable(target);
    }

    /** The whole-block distance to the target, or {@code -1} without one. */
    public int getDistance() {
        return distance;
    }

    /** The action bar Zentrix will send. */
    @NotNull
    public Component getMessage() {
        return message;
    }

    /** Replaces the action bar Zentrix will send. */
    public void setMessage(@NotNull Component message) {
        this.message = Objects.requireNonNull(message, "message");
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
