package dev.itsharshxd.zentrix.api.events.chat;

import dev.itsharshxd.zentrix.api.chat.ChatChannel;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Fired when a player switches chat channels.
 * <p>
 * This event is <b>cancellable</b>. Cancelling prevents the change.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * @EventHandler
 * public void onChannelSwitch(ChatChannelSwitchEvent event) {
 *     if (event.getNewChannel() == ChatChannel.PARTY) {
 *         // Block party chat for selected players
 *         if (!hasPermission(event.getPlayer())) {
 *             event.setCancelled(true);
 *         }
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public class ChatChannelSwitchEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ChatChannel oldChannel;
    private ChatChannel newChannel;
    private boolean cancelled;

    public ChatChannelSwitchEvent(@NotNull Player player,
                                  @NotNull ChatChannel oldChannel,
                                  @NotNull ChatChannel newChannel) {
        super(false);
        this.player = player;
        this.oldChannel = oldChannel;
        this.newChannel = newChannel;
        this.cancelled = false;
    }

    /**
     * Gets the player switching channels.
     *
     * @return The player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's UUID.
     *
     * @return The player's UUID
     */
    @NotNull
    public UUID getPlayerId() {
        return player.getUniqueId();
    }

    /**
     * Gets the channel the player is switching from.
     *
     * @return The old/current channel
     */
    @NotNull
    public ChatChannel getOldChannel() {
        return oldChannel;
    }

    /**
     * Gets the channel the player is switching to.
     *
     * @return The new channel
     */
    @NotNull
    public ChatChannel getNewChannel() {
        return newChannel;
    }

    /**
     * Sets the channel the player will switch to.
     * <p>
     * This allows addons to redirect channel switches.
     * </p>
     *
     * @param newChannel The new target channel
     */
    public void setNewChannel(@NotNull ChatChannel newChannel) {
        this.newChannel = newChannel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
