package dev.itsharshxd.zentrix.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all Zentrix events.
 * <p>
 * All custom events fired by Zentrix extend this class, providing a common
 * base for addon developers to work with. Events allow addons to react to
 * game state changes, player actions, and other significant occurrences.
 * </p>
 *
 * <h2>Listening to Events</h2>
 * <pre>{@code
 * public class MyListener implements Listener {
 *
 *     @EventHandler
 *     public void onGameStart(GameStartEvent event) {
 *         ZentrixGame game = event.getGame();
 *         int playerCount = game.getPlayerCount();
 *         Bukkit.broadcastMessage("A game started with " + playerCount + " players!");
 *     }
 * }
 * }</pre>
 *
 * <h2>Available Event Categories</h2>
 * <ul>
 *   <li><b>Game Events</b> - Game lifecycle events (start, end, phase changes)</li>
 *   <li><b>Player Events</b> - Player-specific events (join, leave, death, kill)</li>
 *   <li><b>Team Events</b> - Team-related events (elimination, win)</li>
 *   <li><b>Currency Events</b> - Economy events (balance changes, rewards)</li>
 * </ul>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see dev.itsharshxd.zentrix.api.events.game.GameStartEvent
 * @see dev.itsharshxd.zentrix.api.events.game.GameEndEvent
 * @see dev.itsharshxd.zentrix.api.events.player.PlayerKillEvent
 * @see dev.itsharshxd.zentrix.api.events.team.TeamEliminatedEvent
 */
public abstract class ZentrixEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Constructs a new synchronous Zentrix event.
     */
    protected ZentrixEvent() {
        super(false);
    }

    /**
     * Constructs a new Zentrix event.
     *
     * @param isAsync {@code true} if the event is fired asynchronously,
     *                {@code false} if fired on the main thread
     */
    protected ZentrixEvent(boolean isAsync) {
        super(isAsync);
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
     * <p>
     * Required by Bukkit's event system.
     * </p>
     *
     * @return The handler list
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
