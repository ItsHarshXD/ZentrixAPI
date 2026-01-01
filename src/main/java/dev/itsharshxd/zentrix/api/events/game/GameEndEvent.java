package dev.itsharshxd.zentrix.api.events.game;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Called when a Zentrix game ends.
 * <p>
 * This event is fired when a game concludes, either because a winner has been
 * determined or the game was forcibly ended. At this point:
 * <ul>
 *   <li>The winning team/player has been determined (if any)</li>
 *   <li>Game phases have stopped</li>
 *   <li>Players are transitioning to end-game state</li>
 *   <li>Rewards are about to be distributed</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>NOT cancellable</b>.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onGameEnd(GameEndEvent event) {
 *     ZentrixGame game = event.getGame();
 *     EndReason reason = event.getEndReason();
 *
 *     if (reason == EndReason.WINNER_DETERMINED) {
 *         event.getWinningTeam().ifPresent(team -> {
 *             getLogger().info("Team " + team.getDisplayName() + " won!");
 *         });
 *
 *         // Announce winners
 *         for (ZentrixPlayer winner : event.getWinners()) {
 *             winner.getBukkitPlayer().ifPresent(p -> {
 *                 Bukkit.broadcastMessage(p.getName() + " is victorious!");
 *             });
 *         }
 *     }
 *
 *     getLogger().info("Game in " + event.getArenaName() + " ended. " +
 *                      "Duration: " + event.getGameDuration() + " seconds");
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see GameStartEvent
 * @see EndReason
 */
public class GameEndEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final EndReason endReason;
    private final ZentrixTeam winningTeam;
    private final Collection<ZentrixPlayer> winners;
    private final long gameDuration;

    /**
     * Constructs a new GameEndEvent.
     *
     * @param game         The game that is ending
     * @param endReason    The reason the game ended
     * @param winningTeam  The winning team (may be null if no winner)
     * @param winners      The winning players (may be empty if no winners)
     * @param gameDuration The total game duration in seconds
     */
    public GameEndEvent(@NotNull ZentrixGame game,
                        @NotNull EndReason endReason,
                        @Nullable ZentrixTeam winningTeam,
                        @NotNull Collection<ZentrixPlayer> winners,
                        long gameDuration) {
        super(game);
        this.endReason = endReason;
        this.winningTeam = winningTeam;
        this.winners = winners != null ? Collections.unmodifiableCollection(winners) : Collections.emptyList();
        this.gameDuration = gameDuration;
    }

    /**
     * Gets the reason the game ended.
     *
     * @return The end reason (never null)
     */
    @NotNull
    public EndReason getEndReason() {
        return endReason;
    }

    /**
     * Gets the winning team.
     * <p>
     * Returns empty if there is no winner (e.g., game was force-ended
     * or all players left).
     * </p>
     *
     * @return Optional containing the winning team, or empty if no winner
     */
    @NotNull
    public Optional<ZentrixTeam> getWinningTeam() {
        return Optional.ofNullable(winningTeam);
    }

    /**
     * Gets all winning players.
     * <p>
     * For team games, this includes all members of the winning team.
     * For solo games, this will contain a single player (the winner).
     * Returns an empty collection if there is no winner.
     * </p>
     *
     * @return An unmodifiable collection of winners (never null, may be empty)
     */
    @NotNull
    public Collection<ZentrixPlayer> getWinners() {
        return winners;
    }

    /**
     * Checks if there is a winner.
     *
     * @return {@code true} if there are winners
     */
    public boolean hasWinner() {
        return !winners.isEmpty();
    }

    /**
     * Gets the total game duration in seconds.
     * <p>
     * This is the time from when the game entered PLAYING state
     * until this end event was fired.
     * </p>
     *
     * @return Game duration in seconds
     */
    public long getGameDuration() {
        return gameDuration;
    }

    /**
     * Gets the game duration formatted as a human-readable string.
     * <p>
     * Examples: "45s", "2m 30s", "1h 15m"
     * </p>
     *
     * @return Formatted duration string (never null)
     */
    @NotNull
    public String getFormattedDuration() {
        if (gameDuration < 60) {
            return gameDuration + "s";
        } else if (gameDuration < 3600) {
            long minutes = gameDuration / 60;
            long seconds = gameDuration % 60;
            return minutes + "m " + seconds + "s";
        } else {
            long hours = gameDuration / 3600;
            long minutes = (gameDuration % 3600) / 60;
            return hours + "h " + minutes + "m";
        }
    }

    /**
     * Gets the number of winners.
     *
     * @return The winner count
     */
    public int getWinnerCount() {
        return winners.size();
    }

    /**
     * Checks if the game ended normally with a winner.
     *
     * @return {@code true} if the game ended with a winner determined
     */
    public boolean isNormalEnd() {
        return endReason == EndReason.WINNER_DETERMINED;
    }

    /**
     * Checks if the game was forcibly ended.
     *
     * @return {@code true} if the game was force-ended
     */
    public boolean isForcedEnd() {
        return endReason == EndReason.FORCE_ENDED || endReason == EndReason.PLUGIN_DISABLED;
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
     * Represents the reason a game ended.
     */
    public enum EndReason {

        /**
         * The game ended normally with a winner (last team/player standing).
         */
        WINNER_DETERMINED,

        /**
         * All players left the game before a winner could be determined.
         */
        ALL_PLAYERS_LEFT,

        /**
         * The game was forcibly ended by an administrator.
         */
        FORCE_ENDED,

        /**
         * The game ended because the plugin is being disabled.
         */
        PLUGIN_DISABLED,

        /**
         * The game ended due to an error or unexpected condition.
         */
        ERROR
    }
}
