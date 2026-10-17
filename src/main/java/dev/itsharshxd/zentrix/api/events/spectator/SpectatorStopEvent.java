package dev.itsharshxd.zentrix.api.events.spectator;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a player stopped spectating.
 *
 * <p>Leaving spectator mode is never optional for Zentrix, so this is a notification. By the time
 * it fires the player is already visible and back in survival mode: in the lobby for
 * {@link Reason#LEFT}, inside their match for {@link Reason#REVIVED}.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class SpectatorStopEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Reason reason;

    public SpectatorStopEvent(@NotNull Player player, @NotNull Reason reason) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.reason = Objects.requireNonNull(reason, "reason");
    }

    /** The former spectator. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** Why they stopped spectating. */
    @NotNull
    public Reason getReason() {
        return reason;
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

    /** Why a player stopped spectating. */
    public enum Reason {
        /** They left the match they watched, or it ended, and were returned to the lobby. */
        LEFT,
        /** They were revived and play in their match again. */
        REVIVED,
        /** They disconnected. The player object is about to go offline. */
        DISCONNECTED
    }
}
