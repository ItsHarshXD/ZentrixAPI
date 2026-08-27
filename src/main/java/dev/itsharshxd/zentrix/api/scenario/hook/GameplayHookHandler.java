package dev.itsharshxd.zentrix.api.scenario.hook;

import org.jetbrains.annotations.NotNull;

/**
 * A scenario's answer to one gameplay decision point.
 *
 * <p>Handlers run on the server main thread, inside the game they were registered for, and are
 * dropped automatically when the scenario or the game ends. A handler that throws is isolated: the
 * exception is logged against the owning scenario and treated as {@link HookOutcome#pass()}, so one
 * broken scenario can never break the decision for anybody else.
 *
 * @param <R> the request type
 * @param <V> the substituted value type
 * @since 1.6.0
 */
@FunctionalInterface
public interface GameplayHookHandler<R, V> {

    /**
     * Decides what happens at this decision point.
     *
     * @param request the request; mutable where the hook documents it as such
     * @return what to do, never null
     */
    @NotNull
    HookOutcome<V> handle(@NotNull R request);
}
