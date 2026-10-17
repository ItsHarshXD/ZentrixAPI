package dev.itsharshxd.zentrix.api.events.spectator;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a player became a spectator: eliminated from their match, watching one through
 * {@code /spectate}, or put back into spectating after a revival failed.
 *
 * <p>Zentrix has already isolated the player from gameplay and handed them the spectator tools
 * when this fires, so listeners may add their own tools or presentation on top. Spectators stay
 * invisible to gameplay whatever a listener does; this event is a notification.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class SpectatorStartEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ZentrixGame game;
    private final boolean eliminated;

    public SpectatorStartEvent(@NotNull Player player, @Nullable ZentrixGame game, boolean eliminated) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.game = game;
        this.eliminated = eliminated;
    }

    /** The new spectator. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The match they are watching, when they are inside one. */
    @NotNull
    public Optional<ZentrixGame> getGame() {
        return Optional.ofNullable(game);
    }

    /**
     * Whether they were eliminated from the match they watch, rather than having walked in to
     * watch it.
     */
    public boolean isEliminated() {
        return eliminated;
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
