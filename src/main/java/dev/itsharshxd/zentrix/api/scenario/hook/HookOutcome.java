package dev.itsharshxd.zentrix.api.scenario.hook;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What a scenario decided about one gameplay decision point.
 *
 * <p>Three answers are possible, and the difference matters for how a combination of scenarios
 * resolves:
 *
 * <ul>
 *   <li>{@link #pass()} — the scenario only observed, or modified the request in place. Zentrix
 *       keeps asking the remaining scenarios and finally falls back to its own behaviour.</li>
 *   <li>{@link #replace(Object)} — the scenario supplies the result itself. No lower-priority
 *       scenario is consulted and Zentrix's own behaviour is skipped.</li>
 *   <li>{@link #cancel()} — the behaviour must not happen at all. Again final.</li>
 * </ul>
 *
 * <p>Requests are mutable where a hook says so, which is what makes "observe, modify, extend" work
 * without taking the decision away from anybody: a scenario can adjust the request and still pass.
 *
 * @param <V> the substituted value type of the hook
 * @since 1.7.0
 */
public final class HookOutcome<V> {

    private static final HookOutcome<?> PASS = new HookOutcome<>(Kind.PASS, null);
    private static final HookOutcome<?> CANCEL = new HookOutcome<>(Kind.CANCEL, null);

    /** The three answers a handler can give. */
    public enum Kind {
        /** Defer to the remaining scenarios and, finally, to Zentrix. */
        PASS,
        /** Take over the decision with a substituted value. */
        REPLACE,
        /** Suppress the behaviour entirely. */
        CANCEL
    }

    private final Kind kind;
    private final V value;

    private HookOutcome(Kind kind, V value) {
        this.kind = kind;
        this.value = value;
    }

    /** Defer: keep consulting other scenarios, then Zentrix's own behaviour. */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <V> HookOutcome<V> pass() {
        return (HookOutcome<V>) PASS;
    }

    /** Suppress the behaviour entirely; no other scenario is consulted. */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <V> HookOutcome<V> cancel() {
        return (HookOutcome<V>) CANCEL;
    }

    /** Take the decision over with this value; no other scenario is consulted. */
    @NotNull
    public static <V> HookOutcome<V> replace(@Nullable V value) {
        return new HookOutcome<>(Kind.REPLACE, value);
    }

    @NotNull
    public Kind kind() {
        return kind;
    }

    public boolean isPass() {
        return kind == Kind.PASS;
    }

    public boolean isCancelled() {
        return kind == Kind.CANCEL;
    }

    public boolean isReplacement() {
        return kind == Kind.REPLACE;
    }

    /** The substituted value, empty unless this outcome is a replacement carrying a value. */
    @NotNull
    public Optional<V> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public String toString() {
        return kind == Kind.REPLACE ? "HookOutcome[REPLACE " + value + "]" : "HookOutcome[" + kind + "]";
    }
}
