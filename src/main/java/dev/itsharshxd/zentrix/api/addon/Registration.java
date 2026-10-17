package dev.itsharshxd.zentrix.api.addon;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Something an addon registered with Zentrix, and the way to take it back.
 *
 * <p>Every registration belongs to the plugin that made it and ends on its own when that plugin is
 * disabled, so an addon that forgets to unregister cannot leave stale code running inside Zentrix.
 * Unregistering twice is harmless.
 *
 * @since 1.8.0
 */
public interface Registration {

    /** The plugin that made the registration. */
    @NotNull
    Plugin owner();

    /** Whether the registration is still in effect. */
    boolean isActive();

    /**
     * Ends the registration.
     *
     * @return true when this call ended it
     */
    boolean unregister();
}
