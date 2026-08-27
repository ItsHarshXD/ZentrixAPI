package dev.itsharshxd.zentrix.api.scenario.hook;

/**
 * A registered gameplay override, kept only so a scenario can drop it early.
 *
 * <p>Releasing is optional: every handle a scenario registers through its game context is released
 * automatically when the scenario is deactivated or the game ends, which is what keeps an override
 * scoped to one match and restores Zentrix's own behaviour afterwards.
 *
 * @since 1.6.0
 */
public interface HookHandle extends AutoCloseable {

    /** Removes the override. Calling this more than once is harmless. */
    void release();

    /** Whether the override is still installed. */
    boolean isActive();

    @Override
    default void close() {
        release();
    }
}
