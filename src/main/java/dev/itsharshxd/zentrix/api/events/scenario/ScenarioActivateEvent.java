package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.ScenarioDescriptor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * One scenario is about to start running in a match.
 *
 * <p>Cancelling keeps this one scenario out of this one match; every other scenario in the
 * selection still activates.
 *
 * @since 1.7.0
 */
public final class ScenarioActivateEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ScenarioDescriptor descriptor;
    private boolean cancelled;

    public ScenarioActivateEvent(@NotNull ZentrixGame game, @NotNull ScenarioDescriptor descriptor) {
        super(game);
        this.descriptor = descriptor;
    }

    @NotNull
    public ScenarioDescriptor getDescriptor() {
        return descriptor;
    }

    @NotNull
    public String getScenarioId() {
        return descriptor.id();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
