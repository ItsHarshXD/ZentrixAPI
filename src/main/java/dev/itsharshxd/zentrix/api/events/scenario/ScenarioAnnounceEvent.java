package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.ScenarioDescriptor;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * One scenario's start announcement is about to reach a match.
 *
 * <p>Zentrix announces every scenario a match runs once its players are in the arena: the first
 * the moment they land, the rest one at a time with the configured delay between them. This event
 * is fired immediately before each of those announcements is sent, so a listener sees them in the
 * same order the players do.
 *
 * <p>{@link #getLines()} is mutable and holds the resolved wording — the locale entry for this
 * scenario, the lines the scenario itself declared, or the shared fallback layout, whichever
 * applied. Lines still carry {@code &} colour codes and {@code <center>} tags at this point;
 * Zentrix formats them afterwards. Clearing the list is the same as cancelling.
 *
 * <p>Cancelling drops this one announcement. The rest of the match's scenarios are still announced
 * on schedule, and the scenario itself is unaffected either way: this event decides what players
 * are told, never what a match runs.
 *
 * @since 1.9.0
 */
public final class ScenarioAnnounceEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ScenarioDescriptor descriptor;
    private final List<String> lines;
    private final int index;
    private final int total;
    private boolean cancelled;

    public ScenarioAnnounceEvent(
            @NotNull ZentrixGame game,
            @NotNull ScenarioDescriptor descriptor,
            @NotNull List<String> lines,
            int index,
            int total) {
        super(game);
        this.descriptor = descriptor;
        this.lines = new ArrayList<>(lines);
        this.index = index;
        this.total = total;
    }

    /** The scenario being announced. */
    @NotNull
    public ScenarioDescriptor getDescriptor() {
        return descriptor;
    }

    /** The announced scenario's ID, for listeners that only care which one it is. */
    @NotNull
    public String getScenarioId() {
        return descriptor.id();
    }

    /**
     * The mutable lines that will be sent.
     *
     * <p>Edit, replace or clear them freely; an empty list sends nothing.
     */
    @NotNull
    public List<String> getLines() {
        return lines;
    }

    /** This announcement's position in the match's sequence, starting at zero. */
    public int getIndex() {
        return index;
    }

    /** How many scenarios this match announces in total. */
    public int getTotal() {
        return total;
    }

    /** Whether this is the announcement sent the moment the players land. */
    public boolean isFirst() {
        return index == 0;
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
