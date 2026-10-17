package dev.itsharshxd.zentrix.api.events.lifecycle;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called once Zentrix has finished starting: arenas are loaded and saved custom games restored.
 *
 * <p>Addons that {@code depend} on Zentrix are enabled after this has fired and can read
 * {@code PlatformService#getLifecycleState()} instead. The event is for plugins that start
 * earlier, such as ones that only {@code softdepend} on Zentrix.
 *
 * @since 1.8.0
 */
public final class ZentrixReadyEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String apiVersion;

    public ZentrixReadyEvent(@NotNull String apiVersion) {
        super(false);
        this.apiVersion = apiVersion;
    }

    /** The API version the started Zentrix implements. */
    @NotNull
    public String getApiVersion() {
        return apiVersion;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
