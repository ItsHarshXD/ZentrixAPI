package dev.itsharshxd.zentrix.api.events.game;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Called when a Zentrix game starts (transitions to PLAYING state).
 * <p>
 * This event is fired after all players have been teleported to the arena
 * and the game officially begins. At this point:
 * <ul>
 *   <li>All players are in the game world</li>
 *   <li>Teams have been assigned</li>
 *   <li>The first phase is about to start</li>
 *   <li>Players are in survival mode</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>NOT cancellable</b>.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onGameStart(GameStartEvent event) {
 *     ZentrixGame game = event.getGame();
 *     int players = event.getStartingPlayerCount();
 *     int teams = event.getStartingTeamCount();
 *
 *     getLogger().info("Game started in " + event.getArenaName() +
 *                      " with " + players + " players in " + teams + " teams!");
 *
 *     // Give all players a starting bonus
 *     for (ZentrixPlayer player : game.getPlayers()) {
 *         player.getBukkitPlayer().ifPresent(p -> {
 *             p.sendMessage("Good luck!");
 *         });
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see GameEndEvent
 * @see dev.itsharshxd.zentrix.api.events.game.GamePhaseChangeEvent
 */
public class GameStartEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final int startingPlayerCount;
    private final int startingTeamCount;

    /**
     * Constructs a new GameStartEvent.
     *
     * @param game The game that is starting
     */
    public GameStartEvent(@NotNull ZentrixGame game) {
        super(game);
        this.startingPlayerCount = game.getPlayerCount();
        this.startingTeamCount = game.getTeams().size();
    }

    /**
     * Gets the number of players when the game started.
     * <p>
     * This is the initial player count at game start, which may be
     * different from the current count if players leave during the event.
     * </p>
     *
     * @return The starting player count
     */
    public int getStartingPlayerCount() {
        return startingPlayerCount;
    }

    /**
     * Gets the number of teams when the game started.
     * <p>
     * For solo games, this will equal the player count.
     * For team games, this will be the number of formed teams.
     * </p>
     *
     * @return The starting team count
     */
    public int getStartingTeamCount() {
        return startingTeamCount;
    }

    /**
     * Gets all teams in the game at start.
     * <p>
     * Convenience method equivalent to {@code getGame().getTeams()}.
     * </p>
     *
     * @return An unmodifiable collection of teams
     */
    @NotNull
    public Collection<ZentrixTeam> getTeams() {
        return getGame().getTeams();
    }

    /**
     * Gets the maximum players allowed in this game.
     *
     * @return The maximum player count
     */
    public int getMaxPlayers() {
        return getGame().getMaxPlayers();
    }

    /**
     * Gets the team size for this game type.
     *
     * @return The team size (1 for solo games)
     */
    public int getTeamSize() {
        return getGame().getTeamSize();
    }

    /**
     * Checks if this is a solo game (team size of 1).
     *
     * @return {@code true} if this is a solo game
     */
    public boolean isSoloGame() {
        return getGame().getTeamSize() == 1;
    }

    /**
     * Checks if this is a team game (team size greater than 1).
     *
     * @return {@code true} if this is a team game
     */
    public boolean isTeamGame() {
        return getGame().getTeamSize() > 1;
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
