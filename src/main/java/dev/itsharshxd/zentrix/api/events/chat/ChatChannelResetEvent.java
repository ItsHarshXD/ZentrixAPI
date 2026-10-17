package dev.itsharshxd.zentrix.api.events.chat;

import dev.itsharshxd.zentrix.api.chat.ChatChannel;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after Zentrix put a player back into {@link ChatChannel#GLOBAL} on its own: when they
 * became a spectator, joined or rejoined a match, were revived, or could no longer use the channel
 * they had selected, as well as through {@code ChatChannelService#resetChannel}.
 *
 * <p>These resets are not requests and cannot be cancelled, which is why they do not fire
 * {@link ChatChannelSwitchEvent}. Only fired when the player was actually in another channel.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class ChatChannelResetEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ChatChannel previousChannel;

    public ChatChannelResetEvent(@NotNull Player player, @NotNull ChatChannel previousChannel) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.previousChannel = Objects.requireNonNull(previousChannel, "previousChannel");
    }

    /** The player whose channel was reset. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The channel they were in before. */
    @NotNull
    public ChatChannel getPreviousChannel() {
        return previousChannel;
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
