package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A match's scenario vote is opening.
 *
 * <p>Listeners may narrow or widen the eligible pool and change the duration. Cancelling skips the
 * vote entirely, in which case the configured no-vote rule decides what the match runs — the match
 * is never left waiting.
 *
 * @since 1.7.0
 */
public final class ScenarioVoteStartEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Set<String> eligible;
    private int durationSeconds;
    private boolean cancelled;

    public ScenarioVoteStartEvent(
            @NotNull ZentrixGame game, @NotNull Set<String> eligible, int durationSeconds) {
        super(game);
        this.eligible = new LinkedHashSet<>(eligible);
        this.durationSeconds = durationSeconds;
    }

    /** The mutable pool players will be able to vote for. */
    @NotNull
    public Set<String> getEligible() {
        return eligible;
    }

    public void addEligible(@NotNull String scenarioId) {
        eligible.add(scenarioId.toLowerCase(Locale.ROOT));
    }

    public boolean removeEligible(@NotNull String scenarioId) {
        return eligible.remove(scenarioId.toLowerCase(Locale.ROOT));
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    /** Shortens or extends the vote. Values below one second are clamped up. */
    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = Math.max(1, durationSeconds);
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
