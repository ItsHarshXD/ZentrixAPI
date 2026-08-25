package dev.itsharshxd.zentrix.api.chat;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Manages player chat channels in Zentrix.
 * <p>
 * Players can switch between global, team, and party chat. Available channels
 * depend on the player's current state, such as being in a game or party.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * ChatChannelService chatService = ZentrixAPI.get().getChatChannelService();
 *
 * // Get the player's current channel
 * ChatChannel channel = chatService.getChannel(player);
 *
 * // Switch to team chat when available
 * if (chatService.canUseTeamChat(player)) {
 *     chatService.setChannel(player, ChatChannel.TEAM);
 * }
 *
 * // Move to the next available channel
 * ChatChannel newChannel = chatService.toggleChannel(player);
 * player.sendMessage("Switched to " + newChannel.getId() + " chat");
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public interface ChatChannelService {

    /**
     * Gets a player's current chat channel.
     *
     * @param player The player
     * @return The player's current chat channel (defaults to {@link ChatChannel#GLOBAL})
     */
    @NotNull
    ChatChannel getChannel(@NotNull Player player);

    /**
     * Gets a player's current chat channel by UUID.
     *
     * @param playerId The player's UUID
     * @return The player's current chat channel (defaults to {@link ChatChannel#GLOBAL})
     */
    @NotNull
    ChatChannel getChannel(@NotNull UUID playerId);

    /**
     * Sets a player's chat channel.
     *
     * @param player  The player
     * @param channel The channel to set
     */
    void setChannel(@NotNull Player player, @NotNull ChatChannel channel);

    /**
     * Toggles a player's chat channel to the next available channel.
     * <p>
     * Cycle order: GLOBAL &rarr; TEAM &rarr; PARTY &rarr; GLOBAL,
     * skipping channels that are not currently available to the player.
     * </p>
     *
     * @param player The player
     * @return The new chat channel after toggling
     */
    @NotNull
    ChatChannel toggleChannel(@NotNull Player player);

    /**
     * Resets a player's chat channel back to {@link ChatChannel#GLOBAL}.
     *
     * @param player The player
     */
    void resetChannel(@NotNull Player player);

    /**
     * Checks if a player can use team chat.
     * <p>
     * Team chat requires the player to be in a game with a team size greater
     * than 1 and not be a spectator.
     * </p>
     *
     * @param player The player
     * @return {@code true} if team chat is available
     */
    boolean canUseTeamChat(@NotNull Player player);

    /**
     * Checks if a player can use party chat.
     * <p>
     * Party chat requires the player to be in a party.
     * </p>
     *
     * @param player The player
     * @return {@code true} if party chat is available
     */
    boolean canUsePartyChat(@NotNull Player player);
}
