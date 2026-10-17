package dev.itsharshxd.zentrix.api.events.revival;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a living player uses the Revival Star from {@code content/items.yml}, after Zentrix
 * checked that they may revive in this match and phase, and before the teammate picker opens.
 *
 * <p>Cancelling keeps the picker closed; the star is not consumed either way - only a completed
 * revival spends it, see {@link RevivalItemConsumeEvent}. A listener that cancels can, for
 * instance, open its own picker and call {@code RevivalService#revive}.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class RevivalItemUseEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private boolean cancelled;

    public RevivalItemUseEvent(@NotNull ZentrixGame game, @NotNull Player player) {
        super(game);
        this.player = Objects.requireNonNull(player, "player");
    }

    /** The player using the star. */
    @NotNull
    public Player getPlayer() {
        return player;
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
