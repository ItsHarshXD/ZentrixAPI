package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.scenario.ScenarioDescriptor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A scenario left the runtime registry.
 *
 * <p>Fired after every match running it has had the scenario deactivated and cleaned up, so by the
 * time listeners see this the scenario owns nothing anywhere.
 *
 * @since 1.7.0
 */
public final class ScenarioUnregisteredEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /** Why the scenario left the registry. */
    public enum Reason {
        /** Somebody called unregister explicitly. */
        REQUESTED,
        /** The providing plugin was disabled. */
        PROVIDER_DISABLED,
        /** Zentrix is shutting down. */
        SHUTDOWN
    }

    private final ScenarioDescriptor descriptor;
    private final Reason reason;

    public ScenarioUnregisteredEvent(@NotNull ScenarioDescriptor descriptor, @NotNull Reason reason) {
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
    public Reason getReason() {
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
