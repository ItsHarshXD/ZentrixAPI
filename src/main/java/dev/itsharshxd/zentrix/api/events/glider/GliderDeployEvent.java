package dev.itsharshxd.zentrix.api.events.glider;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before a player starts gliding: leaving the dragon bus, landing from a sky drop, or
 * through {@code GliderService#deploy}.
 *
 * <p>Cancelling leaves the player falling without a glider, so a listener that cancels is
 * responsible for getting them down safely - for instance by deploying its own descent.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class GliderDeployEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String gliderId;
    private boolean cancelled;

    public GliderDeployEvent(@NotNull Player player, @Nullable String gliderId) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.gliderId = gliderId;
    }

    /** The player about to glide. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * The glider they would wear; empty in the Free edition, which glides without the catalog, or
     * when every glider has been deleted.
     */
    @NotNull
    public Optional<String> getGliderId() {
        return Optional.ofNullable(gliderId);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
