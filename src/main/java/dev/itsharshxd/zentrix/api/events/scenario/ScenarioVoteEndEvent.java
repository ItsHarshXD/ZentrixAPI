package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.vote.ScenarioVoteSnapshot;
import java.util.List;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A match's scenario vote closed and its winners are known.
 *
 * <p>Fired before the selection is locked in, so a listener that wants to change the outcome should
 * do it in {@link ScenarioSelectionLockEvent} instead — this event is a notification.
 *
 * @since 1.6.0
 */
public final class ScenarioVoteEndEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /** Why the vote closed. */
    public enum Reason {
        /** The voting timer ran out. */
        TIMER,
        /** Every participant used all of their votes. */
        EVERYONE_VOTED,
        /** An administrator or an addon closed it early. */
        FORCED,
        /** The lobby was cancelled. */
        CANCELLED
    }

    private final ScenarioVoteSnapshot snapshot;
    private final List<String> winners;
    private final Reason reason;
    private final boolean noVotes;

    public ScenarioVoteEndEvent(
            @NotNull ZentrixGame game,
            @NotNull ScenarioVoteSnapshot snapshot,
            @NotNull List<String> winners,
            @NotNull Reason reason,
            boolean noVotes) {
        super(game);
        this.snapshot = snapshot;
        this.winners = List.copyOf(winners);
        this.reason = reason;
        this.noVotes = noVotes;
    }

    /** The final tally. */
    @NotNull
    public ScenarioVoteSnapshot getSnapshot() {
        return snapshot;
    }

    /** The elected scenarios, after tie and no-vote rules were applied. */
    @NotNull
    public List<String> getWinners() {
        return winners;
    }

    @NotNull
    public Reason getReason() {
        return reason;
    }

    /** Whether nobody voted and the no-vote rule decided the winners. */
    public boolean hadNoVotes() {
        return noVotes;
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
