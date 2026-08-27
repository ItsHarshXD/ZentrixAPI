package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.ScenarioSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A match is about to lock its scenarios in.
 *
 * <p>This is the last moment the set can be changed. Listeners may add or remove IDs through
 * {@link #getScenarioIds()}; Zentrix validates whatever is left, so a change that breaks a
 * dependency or introduces a conflict is corrected rather than honoured blindly.
 *
 * <p>Cancelling makes the match run no scenarios at all. It never stops the match itself.
 *
 * @since 1.6.0
 */
public final class ScenarioSelectionLockEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ScenarioSelection proposed;
    private final List<String> scenarioIds;
    private boolean cancelled;

    public ScenarioSelectionLockEvent(@NotNull ZentrixGame game, @NotNull ScenarioSelection proposed) {
        super(game);
        this.proposed = proposed;
        this.scenarioIds = new ArrayList<>(proposed.scenarioIds());
    }

    /** The selection Zentrix arrived at, before any listener changed it. */
    @NotNull
    public ScenarioSelection getProposed() {
        return proposed;
    }

    /** The mutable set that will be locked in, subject to validation. */
    @NotNull
    public List<String> getScenarioIds() {
        return scenarioIds;
    }

    /** Adds a scenario to the set. */
    public void add(@NotNull String scenarioId) {
        String normalized = scenarioId.toLowerCase(Locale.ROOT);
        if (!scenarioIds.contains(normalized)) {
            scenarioIds.add(normalized);
        }
    }

    /** Removes a scenario from the set. */
    public boolean remove(@NotNull String scenarioId) {
        return scenarioIds.remove(scenarioId.toLowerCase(Locale.ROOT));
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
