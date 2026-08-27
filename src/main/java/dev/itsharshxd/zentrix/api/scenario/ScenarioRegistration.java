package dev.itsharshxd.zentrix.api.scenario;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A live entry in the scenario registry.
 *
 * <p>Holding the registration is the tidiest way for an addon to remove its scenario again, but it
 * is never required: Zentrix unregisters every scenario a plugin owns as soon as that plugin is
 * disabled.
 *
 * @since 1.6.0
 */
public interface ScenarioRegistration {

    /** The registered scenario. */
    @NotNull
    Scenario scenario();

    /** The scenario's metadata. */
    @NotNull
    default ScenarioDescriptor descriptor() {
        return scenario().descriptor();
    }

    /** The plugin that registered the scenario. */
    @NotNull
    Plugin owner();

    /** Whether Zentrix itself provides this scenario. */
    boolean builtIn();

    /** Whether the entry is still in the registry. */
    boolean isRegistered();

    /**
     * Removes the scenario from the registry.
     *
     * <p>Matches already running it keep going until their instances are deactivated, which happens
     * immediately for a scenario removed mid-match.
     *
     * @return true when this call removed the entry
     */
    boolean unregister();
}
