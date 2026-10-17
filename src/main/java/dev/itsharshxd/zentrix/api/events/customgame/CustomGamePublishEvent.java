package dev.itsharshxd.zentrix.api.events.customgame;

import dev.itsharshxd.zentrix.api.customgame.CustomGameInfo;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a host published a custom game and it was saved, just as it is announced.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CustomGamePublishEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CustomGameInfo game;

    public CustomGamePublishEvent(@NotNull CustomGameInfo game) {
        super(false);
        this.game = Objects.requireNonNull(game, "game");
    }

    /** The published game. */
    @NotNull
    public CustomGameInfo getCustomGame() {
        return game;
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
