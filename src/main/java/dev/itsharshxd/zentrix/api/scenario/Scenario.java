package dev.itsharshxd.zentrix.api.scenario;

import org.jetbrains.annotations.NotNull;

/**
 * A gameplay modifier that a match can run.
 *
 * <p>A scenario is registered once and produces one {@link ScenarioInstance} per match it takes
 * part in, which is what keeps its runtime state isolated to a single game and arena.
 *
 * <p>Third-party scenarios are registered through
 * {@link ScenarioService#register(org.bukkit.plugin.Plugin, Scenario)} and are treated exactly like
 * the ones Zentrix ships: they appear in the scenario commands, the management and voting GUIs, the
 * automatic-selection pool, validation and every API lookup, and they get the same metadata,
 * settings, dependencies, conflicts, priorities, per-game state, lifecycle hooks, scheduling and
 * automatic cleanup.
 *
 * <p>Registration is in-memory only. Nothing about a dynamically registered scenario is written to
 * {@code scenarios.yml} until an administrator explicitly configures it, so an addon that is
 * removed again leaves no trace behind.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * public final class NoFallScenario implements Scenario {
 *
 *     private static final ScenarioDescriptor DESCRIPTOR = ScenarioDescriptor.builder("no-fall")
 *             .displayName("No Fall")
 *             .description("Fall damage is disabled.")
 *             .icon(Material.FEATHER)
 *             .setting(ScenarioSetting.bool("also-in-deathmatch", true))
 *             .build();
 *
 *     @Override public ScenarioDescriptor descriptor() { return DESCRIPTOR; }
 *
 *     @Override public ScenarioInstance createInstance() {
 *         return new ScenarioInstance() {
 *             @Override public void onActivate(ScenarioContext context) {
 *                 context.override(GameplayHooks.PLAYER_DAMAGE, request ->
 *                         request.cause().equals("FALL") && context.isParticipant(request.victim())
 *                                 ? HookOutcome.cancel()
 *                                 : HookOutcome.pass());
 *             }
 *         };
 *     }
 * }
 *
 * ZentrixAPI.get().getScenarioService().register(this, new NoFallScenario());
 * }</pre>
 *
 * @since 1.6.0
 */
public interface Scenario {

    /** The scenario's immutable metadata. Must return the same descriptor every time. */
    @NotNull
    ScenarioDescriptor descriptor();

    /**
     * Creates the runtime instance for one match.
     *
     * <p>Called once per match the scenario takes part in, just before activation. Return a fresh
     * object every time: sharing one instance between matches would leak state between them.
     */
    @NotNull
    ScenarioInstance createInstance();

    /**
     * The scenario has been added to the registry.
     *
     * <p>Runs on the thread that registered it and is a good place for one-off setup that does not
     * belong to any match. Throwing here aborts the registration.
     */
    default void onRegister() {
    }

    /**
     * The scenario has been removed from the registry, whether explicitly or because its plugin was
     * disabled.
     *
     * <p>Matches that were already running the scenario have had their instances deactivated
     * before this is called.
     */
    default void onUnregister() {
    }
}
