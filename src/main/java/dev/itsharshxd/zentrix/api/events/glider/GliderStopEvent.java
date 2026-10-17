package dev.itsharshxd.zentrix.api.events.glider;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a player's glide ended: they landed or touched water or lava, died, quit, mounted
 * something, became a spectator, lost Slow Falling, or the glide was stopped by Zentrix or an
 * addon. The glider is already off their back.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class GliderStopEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public GliderStopEvent(@NotNull Player player) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
    }

    /** The player who stopped gliding. */
    @NotNull
    public Player getPlayer() {
        return player;
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
