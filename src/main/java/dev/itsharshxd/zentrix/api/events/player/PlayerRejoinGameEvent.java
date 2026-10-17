package dev.itsharshxd.zentrix.api.events.player;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a player returned, through their corpse, to the running match they left.
 *
 * <p>A notification: the player is back in the match with the corpse's inventory, their
 * experience, ender chest and team, and the corpse is gone. To refuse a return, cancel the
 * {@link PlayerJoinGameEvent} that precedes it, whose {@link PlayerJoinGameEvent#isRejoin()} is
 * true. Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class PlayerRejoinGameEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String teamId;

    public PlayerRejoinGameEvent(@NotNull ZentrixGame game, @NotNull Player player, @Nullable String teamId) {
        super(game);
        this.player = player;
        this.teamId = teamId;
    }

    /** The returning player. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The team they returned to, in a match played in teams. */
    @NotNull
    public Optional<String> getTeamId() {
        return Optional.ofNullable(teamId);
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
