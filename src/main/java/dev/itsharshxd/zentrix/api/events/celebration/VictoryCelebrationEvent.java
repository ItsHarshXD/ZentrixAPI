package dev.itsharshxd.zentrix.api.events.celebration;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a won match is about to celebrate, with the celebration each winner will play.
 *
 * <p>{@link #getCelebrations()} maps every winner still in the match to the ID of the celebration
 * they will play - their own choice or the server's default. Listeners may give a winner another
 * existing celebration through {@link #setCelebration(UUID, String)} or keep one from celebrating
 * through {@link #removeWinner(UUID)}; winners cannot be added. Cancelling plays no celebration at
 * all. The win announcement and the match's restart are not affected either way.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class VictoryCelebrationEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Set<UUID> winningTeam;
    private final Map<UUID, String> celebrations;
    private final Predicate<String> exists;
    private boolean cancelled;

    public VictoryCelebrationEvent(
            @NotNull ZentrixGame game,
            @NotNull Map<UUID, String> celebrations,
            @NotNull Set<UUID> winningTeam,
            @NotNull Predicate<String> exists) {
        super(game);
        this.celebrations = new LinkedHashMap<>(celebrations);
        this.winningTeam = Set.copyOf(winningTeam);
        this.exists = Objects.requireNonNull(exists, "exists");
    }

    /** The celebration each winner will play, by winner; a read-only view. */
    @NotNull
    public Map<UUID, String> getCelebrations() {
        return Collections.unmodifiableMap(celebrations);
    }

    /**
     * Gives a winner another celebration.
     *
     * @throws IllegalArgumentException when the player does not celebrate in this match, or no
     *                                  celebration with that ID exists
     */
    public void setCelebration(@NotNull UUID winnerId, @NotNull String celebrationId) {
        if (!celebrations.containsKey(winnerId)) {
            throw new IllegalArgumentException(winnerId + " does not celebrate in this match");
        }
        if (!exists.test(celebrationId)) {
            throw new IllegalArgumentException("Unknown celebration: " + celebrationId);
        }
        celebrations.put(winnerId, celebrationId);
    }

    /**
     * Keeps a winner from celebrating.
     *
     * @return whether they would have celebrated
     */
    public boolean removeWinner(@NotNull UUID winnerId) {
        return celebrations.remove(winnerId) != null;
    }

    /** The winning team, including members who fell before the win. */
    @NotNull
    public Set<UUID> getWinningTeam() {
        return winningTeam;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
