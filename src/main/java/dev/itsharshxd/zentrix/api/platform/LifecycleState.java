package dev.itsharshxd.zentrix.api.platform;

/**
 * Where Zentrix is in its own startup and shutdown.
 *
 * @see dev.itsharshxd.zentrix.api.events.lifecycle.ZentrixReadyEvent
 * @see dev.itsharshxd.zentrix.api.events.lifecycle.ZentrixShutdownEvent
 * @since 1.8.0
 */
public enum LifecycleState {
    /**
     * The API is registered, but Zentrix has not finished starting: arenas and saved custom games
     * may still be loading.
     */
    STARTING,
    /** Zentrix has finished starting. */
    READY,
    /** Zentrix is shutting down; the API is about to be unregistered. */
    STOPPING
}
