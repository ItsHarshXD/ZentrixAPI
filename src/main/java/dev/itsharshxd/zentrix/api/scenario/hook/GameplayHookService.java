package dev.itsharshxd.zentrix.api.scenario.hook;

import dev.itsharshxd.zentrix.api.addon.Registration;
import java.util.Collection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Gameplay hooks for ordinary addons, consulted in every running match without being a selected
 * scenario.
 *
 * <p>The decision points are the same {@link GameplayHooks} scenarios use, with the same request
 * and outcome rules. A handler registered here is consulted for a decision only after every active
 * scenario of that match passed, so selected scenarios keep deciding first exactly as before.
 * Among addon handlers, a higher priority is asked first and equal priorities keep registration
 * order; the first answer that does not pass decides and the rest are not asked.
 *
 * <p>Handlers run on the thread of the decision - the main thread for every built-in hook - and
 * must decide quickly. A handler that throws is treated as passing and the failure is logged under
 * its owner; it stays registered. Registrations end on their own when the owning plugin disables.
 * Lookups resolve the match from the request's player, block or entity, so a handler only ever
 * sees decisions inside running Zentrix matches.
 *
 * <pre>{@code
 * ZentrixAPI.get().getGameplayHookService().register(this, GameplayHooks.PLAYER_DAMAGE, 0,
 *     request -> request.cause().equals("FALL") ? HookOutcome.cancel() : HookOutcome.pass());
 * }</pre>
 *
 * @since 1.8.0
 */
public interface GameplayHookService {

    /**
     * Registers a handler for one decision point in every match.
     *
     * @param owner    the plugin the registration belongs to; it must be enabled
     * @param hook     the decision point
     * @param priority the order among addon handlers, higher first
     * @param handler  the handler
     * @return the registration, to unregister it early
     * @throws IllegalArgumentException when the owner is not enabled
     */
    @NotNull
    <R, V> Registration register(
            @NotNull Plugin owner,
            @NotNull GameplayHook<R, V> hook,
            int priority,
            @NotNull GameplayHookHandler<R, V> handler);

    /** The decision points any addon currently handles. */
    @NotNull
    Collection<GameplayHook<?, ?>> getHandledHooks();
}
