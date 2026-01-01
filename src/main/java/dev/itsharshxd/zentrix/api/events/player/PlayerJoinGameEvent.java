package dev.itsharshxd.zentrix.api.events.player;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a player attempts to join a Zentrix game.
 * <p>
 * This event is fired before the player is added to the game, allowing
 * addons to prevent the join or perform setup tasks. At this point:
 * <ul>
 *   <li>The player has requested to join (via command, GUI, etc.)</li>
 *   <li>Basic validation has passed (player not already in a game)</li>
 *   <li>The game has space for the player</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>cancellable</b>. Cancelling prevents the player from joining.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onPlayerJoinGame(PlayerJoinGameEvent event) {
 *     Player player = event.getPlayer();
 *     ZentrixGame game = event.getGame();
 *
 *     // Check custom permission
 *     if (!player.hasPermission("myaddon.play")) {
 *         event.setCancelled(true);
 *         event.setCancelReason("You don't have permission to play!");
 *         return;
 *     }
 *
 *     // Log the join
 *     getLogger().info(player.getName() + " is joining game in " + event.getArenaName());
 *
 *     // Check if joining as spectator
 *     if (event.isSpectator()) {
 *         player.sendMessage("You're joining as a spectator!");
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see PlayerLeaveGameEvent
 * @see PlayerDeathGameEvent
 */
public class PlayerJoinGameEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final boolean spectator;
    private boolean cancelled;
    private String cancelReason;

    /**
     * Constructs a new PlayerJoinGameEvent.
     *
     * @param game      The game the player is joining
     * @param player    The player attempting to join
     * @param spectator Whether the player is joining as a spectator
     */
    public PlayerJoinGameEvent(@NotNull ZentrixGame game,
                               @NotNull Player player,
                               boolean spectator) {
        super(game);
        this.player = player;
        this.spectator = spectator;
        this.cancelled = false;
        this.cancelReason = null;
    }

    /**
     * Gets the player attempting to join the game.
     *
     * @return The player (never null)
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if the player is joining as a spectator.
     * <p>
     * Spectators are players who join to watch an ongoing game
     * rather than participate as active players.
     * </p>
     *
     * @return {@code true} if joining as spectator
     */
    public boolean isSpectator() {
        return spectator;
    }

    /**
     * Checks if the player is joining as an active participant.
     * <p>
     * Active participants are players who will be part of the game
     * and can be eliminated.
     * </p>
     *
     * @return {@code true} if joining as an active player (not spectator)
     */
    public boolean isActivePlayer() {
        return !spectator;
    }

    /**
     * Gets the reason the join was cancelled, if any.
     * <p>
     * This message can be shown to the player to explain why
     * they couldn't join.
     * </p>
     *
     * @return The cancel reason, or null if not cancelled or no reason set
     */
    @Nullable
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * Sets the reason for cancelling the join.
     * <p>
     * This message will be shown to the player when they
     * are prevented from joining.
     * </p>
     *
     * @param reason The reason message (can be null)
     */
    public void setCancelReason(@Nullable String reason) {
        this.cancelReason = reason;
    }

    /**
     * Cancels the event with a reason.
     * <p>
     * Convenience method that sets both cancelled state and reason.
     * </p>
     *
     * @param reason The reason for cancelling
     */
    public void cancel(@NotNull String reason) {
        this.cancelled = true;
        this.cancelReason = reason;
    }

    /**
     * Gets the player's name.
     * <p>
     * Convenience method equivalent to {@code getPlayer().getName()}.
     * </p>
     *
     * @return The player's name (never null)
     */
    @NotNull
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Gets the number of players currently in the game.
     * <p>
     * This is the count before this player joins.
     * </p>
     *
     * @return Current player count
     */
    public int getCurrentPlayerCount() {
        return getGame().getPlayerCount();
    }

    /**
     * Gets the number of players after this player joins.
     * <p>
     * Returns the current count + 1 if joining as active player,
     * or the current count if joining as spectator.
     * </p>
     *
     * @return Player count after join
     */
    public int getPlayerCountAfterJoin() {
        return spectator ? getGame().getPlayerCount() : getGame().getPlayerCount() + 1;
    }

    /**
     * Checks if the game will be full after this player joins.
     *
     * @return {@code true} if the game will reach max capacity
     */
    public boolean willBeFull() {
        return !spectator && getPlayerCountAfterJoin() >= getGame().getMaxPlayers();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the static handler list for this event type.
     *
     * @return The handler list
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
