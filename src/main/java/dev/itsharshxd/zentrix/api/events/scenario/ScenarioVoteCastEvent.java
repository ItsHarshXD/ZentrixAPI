package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A player is casting or withdrawing a scenario vote.
 *
 * <p>Cancelling rejects the vote; the player keeps whatever they had voted for before.
 *
 * @since 1.7.0
 */
public final class ScenarioVoteCastEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String scenarioId;
    private final boolean withdrawal;
    private boolean cancelled;

    public ScenarioVoteCastEvent(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull String scenarioId,
            boolean withdrawal) {
        super(game);
        this.player = player;
        this.scenarioId = scenarioId;
        this.withdrawal = withdrawal;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public String getScenarioId() {
        return scenarioId;
    }

    /** Whether the player is taking a vote back rather than casting one. */
    public boolean isWithdrawal() {
        return withdrawal;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
