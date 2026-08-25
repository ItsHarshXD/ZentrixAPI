package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.scenario.ScenarioRegistration;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A scenario entered the runtime registry and is immediately usable everywhere.
 *
 * @since 1.7.0
 */
public final class ScenarioRegisteredEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ScenarioRegistration registration;

    public ScenarioRegisteredEvent(@NotNull ScenarioRegistration registration) {
        this.registration = registration;
    }

    @NotNull
    public ScenarioRegistration getRegistration() {
        return registration;
    }

    @NotNull
    public String getScenarioId() {
        return registration.descriptor().id();
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
