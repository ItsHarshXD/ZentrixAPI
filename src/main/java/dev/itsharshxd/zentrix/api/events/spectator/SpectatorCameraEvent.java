package dev.itsharshxd.zentrix.api.events.spectator;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before a spectator enters a living player's point of view, whether they clicked the
 * player or an addon called {@code PlayerService#setSpectatorCameraTarget}.
 *
 * <p>Cancelling keeps the spectator in free flight. The target cannot be changed here: Zentrix
 * only lets spectators view living players of the match they watch.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class SpectatorCameraEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player spectator;
    private final Player target;
    private boolean cancelled;

    public SpectatorCameraEvent(@NotNull Player spectator, @NotNull Player target) {
        super(false);
        this.spectator = Objects.requireNonNull(spectator, "spectator");
        this.target = Objects.requireNonNull(target, "target");
    }

    /** The spectator about to change their view. */
    @NotNull
    public Player getSpectator() {
        return spectator;
    }

    /** The living player whose view they would take. */
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
