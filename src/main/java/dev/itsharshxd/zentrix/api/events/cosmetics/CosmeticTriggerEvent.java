package dev.itsharshxd.zentrix.api.events.cosmetics;

import dev.itsharshxd.zentrix.api.cosmetics.CosmeticTrigger;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import java.util.Objects;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before a player's cosmetic of an addon's type is triggered at a moment of a match, before
 * its handler hears of it. Cancelling skips this one trigger. A
 * {@link dev.itsharshxd.zentrix.api.cosmetics.CosmeticMoment#GAME_LEAVE GAME_LEAVE} trigger
 * cannot be cancelled, so the type always gets to clean up.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.11.0
 */
public final class CosmeticTriggerEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CosmeticTrigger trigger;
    private boolean cancelled;

    public CosmeticTriggerEvent(@NotNull CosmeticTrigger trigger) {
        super(Objects.requireNonNull(trigger, "trigger").game());
        this.trigger = trigger;
    }

    /** What is triggered. */
    @NotNull
    public CosmeticTrigger getTrigger() {
        return trigger;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /** Has no effect on a {@code GAME_LEAVE} trigger. */
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
