package dev.itsharshxd.zentrix.api.events.customgame;

import dev.itsharshxd.zentrix.api.customgame.CustomGameInfo;
import dev.itsharshxd.zentrix.api.customgame.CustomGameState;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a published custom game changed state: its lobby was built ({@code WAITING}), torn
 * down again before the start ({@code SCHEDULED}), its match started ({@code ACTIVE}), finished
 * ({@code COMPLETED}) or the game was called off ({@code CANCELLED}).
 *
 * <p>A notification; the change has already happened.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CustomGameStateChangeEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CustomGameInfo game;
    private final CustomGameState previousState;
    private final CancelReason cancelReason;

    public CustomGameStateChangeEvent(
            @NotNull CustomGameInfo game,
            @NotNull CustomGameState previousState,
            @Nullable CancelReason cancelReason) {
        super(false);
        this.game = Objects.requireNonNull(game, "game");
        this.previousState = Objects.requireNonNull(previousState, "previousState");
        this.cancelReason = cancelReason;
    }

    /** The game, in its new state. */
    @NotNull
    public CustomGameInfo getCustomGame() {
        return game;
    }

    /** The state it was in before. */
    @NotNull
    public CustomGameState getPreviousState() {
        return previousState;
    }

    /** The new state. */
    @NotNull
    public CustomGameState getNewState() {
        return game.state();
    }

    /** Why the game was called off, when the new state is {@link CustomGameState#CANCELLED}. */
    @NotNull
    public Optional<CancelReason> getCancelReason() {
        return Optional.ofNullable(cancelReason);
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

    /** Why a custom game was called off. */
    public enum CancelReason {
        /** Its host called it off. */
        HOST,
        /** An administrator called it off. */
        ADMIN,
        /** An administrator ended its running match. */
        TERMINATED,
        /** Custom games were disabled. */
        DISABLED,
        /** Nobody joined before its start time. */
        NO_PLAYERS,
        /** Its match could not start. */
        CANNOT_START,
        /** Its worlds could not be prepared. */
        PREPARATION_FAILED
    }
}
