package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.ScenarioDescriptor;
import dev.itsharshxd.zentrix.api.scenario.ScenarioInstance;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * One scenario stopped running in a match.
 *
 * <p>Fired after the scenario's own teardown and after everything it registered through its context
 * has been released, so the match is already back to Zentrix's default behaviour.
 *
 * @since 1.6.0
 */
public final class ScenarioDeactivateEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ScenarioDescriptor descriptor;
    private final ScenarioInstance.DeactivationReason reason;

    public ScenarioDeactivateEvent(
            @NotNull ZentrixGame game,
            @NotNull ScenarioDescriptor descriptor,
            @NotNull ScenarioInstance.DeactivationReason reason) {
        super(game);
        this.descriptor = descriptor;
        this.reason = reason;
    }

    @NotNull
    public ScenarioDescriptor getDescriptor() {
        return descriptor;
    }

    @NotNull
    public String getScenarioId() {
        return descriptor.id();
    }

    @NotNull
    public ScenarioInstance.DeactivationReason getReason() {
        return reason;
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
