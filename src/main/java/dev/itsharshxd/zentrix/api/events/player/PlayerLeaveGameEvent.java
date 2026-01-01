package dev.itsharshxd.zentrix.api.events.player;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Called when a player leaves a Zentrix game.
 * <p>
 * This event is fired after a player has left the game, either by:
 * <ul>
 *   <li>Using the leave command</li>
 *   <li>Disconnecting from the server</li>
 *   <li>Being kicked</li>
 *   <li>Being eliminated (death)</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>NOT cancellable</b> - the player has already left.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onPlayerLeaveGame(PlayerLeaveGameEvent event) {
 *     ZentrixPlayer player = event.getPlayer();
 *     ZentrixGame game = event.getGame();
 *     LeaveReason reason = event.getReason();
 *
 *     switch (reason) {
 *         case DEATH -> getLogger().info(player.getName() + " was eliminated");
 *         case QUIT -> getLogger().info(player.getName() + " disconnected");
 *         case COMMAND -> getLogger().info(player.getName() + " left voluntarily");
 *         case KICK -> getLogger().info(player.getName() + " was kicked");
 *     }
 *
 *     // Check remaining players
 *     int remaining = event.getRemainingPlayers();
 *     game.broadcast("&7" + remaining + " players remain!");
 *
 *     // Check if this triggers a win condition
 *     if (event.couldTriggerWin()) {
 *         getLogger().info("This leave may end the game!");
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see PlayerJoinGameEvent
 * @see PlayerDeathGameEvent
 */
public class PlayerLeaveGameEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixPlayer player;
    private final ZentrixTeam team;
    private final LeaveReason reason;
    private final int remainingPlayers;
    private final int remainingTeams;
    private final int playerKills;
    private final long survivalTime;

    /**
     * Constructs a new PlayerLeaveGameEvent.
     *
     * @param game             The game the player is leaving
     * @param player           The player who is leaving
     * @param team             The player's team (may be null if not on a team)
     * @param reason           The reason for leaving
     * @param remainingPlayers The number of players remaining after this leave
     * @param remainingTeams   The number of teams remaining after this leave
     * @param playerKills      The player's kill count when leaving
     * @param survivalTime     The player's survival time in seconds
     */
    public PlayerLeaveGameEvent(@NotNull ZentrixGame game,
                                @NotNull ZentrixPlayer player,
                                @Nullable ZentrixTeam team,
                                @NotNull LeaveReason reason,
                                int remainingPlayers,
                                int remainingTeams,
                                int playerKills,
                                long survivalTime) {
        super(game);
        this.player = player;
        this.team = team;
        this.reason = reason;
        this.remainingPlayers = remainingPlayers;
        this.remainingTeams = remainingTeams;
        this.playerKills = playerKills;
        this.survivalTime = survivalTime;
    }

    /**
     * Gets the player who is leaving the game.
     *
     * @return The player (never null)
     */
    @NotNull
    public ZentrixPlayer getPlayer() {
        return player;
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
     * Gets the player's team at the time of leaving.
     * <p>
     * Returns empty if the player was not assigned to a team.
     * </p>
     *
     * @return Optional containing the team, or empty if no team
     */
    @NotNull
    public Optional<ZentrixTeam> getTeam() {
        return Optional.ofNullable(team);
    }

    /**
     * Gets the reason the player left the game.
     *
     * @return The leave reason (never null)
     */
    @NotNull
    public LeaveReason getReason() {
        return reason;
    }

    /**
     * Checks if the player left voluntarily (by command).
     *
     * @return {@code true} if the player used a leave command
     */
    public boolean wasVoluntary() {
        return reason == LeaveReason.COMMAND;
    }

    /**
     * Checks if the player left due to death/elimination.
     *
     * @return {@code true} if the player was eliminated
     */
    public boolean wasDeath() {
        return reason == LeaveReason.DEATH;
    }

    /**
     * Checks if the player left due to disconnecting.
     *
     * @return {@code true} if the player disconnected
     */
    public boolean wasDisconnect() {
        return reason == LeaveReason.QUIT;
    }

    /**
     * Checks if the player was kicked.
     *
     * @return {@code true} if the player was kicked
     */
    public boolean wasKicked() {
        return reason == LeaveReason.KICK;
    }

    /**
     * Gets the number of players remaining after this leave.
     *
     * @return Remaining player count
     */
    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    /**
     * Gets the number of teams remaining after this leave.
     *
     * @return Remaining team count
     */
    public int getRemainingTeams() {
        return remainingTeams;
    }

    /**
     * Gets the player's kill count at the time of leaving.
     *
     * @return The player's kills in this game
     */
    public int getPlayerKills() {
        return playerKills;
    }

    /**
     * Gets the player's survival time in seconds.
     * <p>
     * Measured from game start (PLAYING state) until leaving.
     * </p>
     *
     * @return Survival time in seconds
     */
    public long getSurvivalTime() {
        return survivalTime;
    }

    /**
     * Checks if this leave could trigger a win condition.
     * <p>
     * Returns true if only one team/player remains after this leave.
     * </p>
     *
     * @return {@code true} if this leave may end the game
     */
    public boolean couldTriggerWin() {
        return remainingTeams <= 1;
    }

    /**
     * Checks if the player had any kills before leaving.
     *
     * @return {@code true} if the player got at least one kill
     */
    public boolean hadKills() {
        return playerKills > 0;
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

    /**
     * Represents the reason a player left a game.
     */
    public enum LeaveReason {

        /**
         * Player used a leave command (e.g., /leave).
         */
        COMMAND,

        /**
         * Player disconnected from the server.
         */
        QUIT,

        /**
         * Player was kicked from the game or server.
         */
        KICK,

        /**
         * Player was eliminated (died in game).
         * <p>
         * Note: This converts the player to a spectator, they don't
         * fully leave unless they also disconnect or use a command.
         * </p>
         */
        DEATH,

        /**
         * Player was removed due to the game ending.
         */
        GAME_END,

        /**
         * Player was removed for an unknown or other reason.
         */
        OTHER
    }
}
