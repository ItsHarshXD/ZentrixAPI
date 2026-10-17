package dev.itsharshxd.zentrix.api.events.compass;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a player's teammate compass changed who it points at.
 *
 * <p>A notification: the new target is already in effect. To keep a compass off someone, register
 * a {@link dev.itsharshxd.zentrix.api.compass.CompassTargetFilter} instead. Main thread; only fired
 * while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CompassTargetChangeEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player tracker;
    private final UUID previousTarget;
    private final UUID newTarget;
    private final Cause cause;

    public CompassTargetChangeEvent(
            @NotNull Player tracker, @Nullable UUID previousTarget, @Nullable UUID newTarget,
            @NotNull Cause cause) {
        super(false);
        this.tracker = tracker;
        this.previousTarget = previousTarget;
        this.newTarget = newTarget;
        this.cause = cause;
    }

    /** The player whose compass changed. */
    @NotNull
    public Player getTracker() {
        return tracker;
    }

    /** Who it pointed at before, if anyone. */
    @NotNull
    public Optional<UUID> getPreviousTarget() {
        return Optional.ofNullable(previousTarget);
    }

    /** Who it points at now; empty when it has no target any more. */
    @NotNull
    public Optional<UUID> getNewTarget() {
        return Optional.ofNullable(newTarget);
    }

    /** Why it changed. */
    @NotNull
    public Cause getCause() {
        return cause;
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

    /** Why a compass target changed. */
    public enum Cause {
        /** The player cycled to the next teammate. */
        CYCLE,
        /** An addon chose the target through {@code CompassTrackerService#setTarget}. */
        SET,
        /** The selection was cleared through {@code CompassTrackerService#clearTarget}. */
        CLEAR,
        /** Zentrix picked the nearest eligible teammate because there was no valid target. */
        AUTOMATIC,
        /** The target stopped being eligible: died, left, changed team or world, or was filtered. */
        LOST
    }
}
