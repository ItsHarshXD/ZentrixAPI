package dev.itsharshxd.zentrix.api.events;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all Zentrix events that are associated with a specific game.
 * <p>
 * This class provides common functionality for events that occur within the
 * context of a Zentrix game, such as game lifecycle events, player events
 * within games, and team events.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onAnyGameEvent(ZentrixGameEvent event) {
 *     ZentrixGame game = event.getGame();
 *     String arenaName = event.getArenaName();
 *     int playerCount = event.getPlayerCount();
 *
 *     getLogger().info("Event in arena " + arenaName + " with " + playerCount + " players");
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see ZentrixEvent
 * @see dev.itsharshxd.zentrix.api.events.game.GameStartEvent
 * @see dev.itsharshxd.zentrix.api.events.game.GameEndEvent
 */
public abstract class ZentrixGameEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixGame game;

    /**
     * Constructs a new synchronous game event.
     *
     * @param game The game this event is associated with
     * @throws IllegalArgumentException if game is null
     */
    protected ZentrixGameEvent(@NotNull ZentrixGame game) {
        super(false);
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        this.game = game;
    }

    /**
     * Constructs a new game event.
     *
     * @param game    The game this event is associated with
     * @param isAsync {@code true} if the event is fired asynchronously
     * @throws IllegalArgumentException if game is null
     */
    protected ZentrixGameEvent(@NotNull ZentrixGame game, boolean isAsync) {
        super(isAsync);
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        this.game = game;
    }

    /**
     * Gets the game this event is associated with.
     *
     * @return The game instance (never null)
     */
    @NotNull
    public ZentrixGame getGame() {
        return game;
    }

    /**
     * Gets the unique identifier of the game.
     * <p>
     * Convenience method equivalent to {@code getGame().getGameId()}.
     * </p>
     *
     * @return The game ID (never null)
     */
    @NotNull
    public String getGameId() {
        return game.getGameId();
    }

    /**
     * Gets the arena name this game is running on.
     * <p>
     * Convenience method equivalent to {@code getGame().getArenaName()}.
     * </p>
     *
     * @return The arena name (never null)
     */
    @NotNull
    public String getArenaName() {
        return game.getArenaName();
    }

    /**
     * Gets the current number of alive players in the game.
     * <p>
     * Convenience method equivalent to {@code getGame().getPlayerCount()}.
     * </p>
     *
     * @return The player count
     */
    public int getPlayerCount() {
        return game.getPlayerCount();
    }

    /**
     * Gets the current game state.
     * <p>
     * Convenience method equivalent to {@code getGame().getState()}.
     * </p>
     *
     * @return The game state (never null)
     */
    @NotNull
    public ZentrixGame.GameState getGameState() {
        return game.getState();
    }

    /**
     * Gets the game type name (e.g., "solo", "duo", "squad").
     * <p>
     * Convenience method equivalent to {@code getGame().getGameTypeName()}.
     * </p>
     *
     * @return The game type name (never null)
     */
    @NotNull
    public String getGameTypeName() {
        return game.getGameTypeName();
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
