package dev.itsharshxd.zentrix.api.compass;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Narrows who a teammate compass may point at.
 *
 * <p>A filter can only take candidates away: Zentrix first applies its own rules - a living,
 * non-spectating teammate in the same match and world - and only asks the filters about the
 * teammates that pass. It is asked on the main thread, whenever a tracker resolves or cycles its
 * target, so it must be cheap. A filter that throws counts as refusing the candidate.
 *
 * @see CompassTrackerService#registerTargetFilter
 * @since 1.8.0
 */
@FunctionalInterface
public interface CompassTargetFilter {

    /**
     * @param tracker the player holding the compass
     * @param target  a teammate Zentrix would let them track
     * @return whether the compass may point at the target
     */
    boolean canTrack(@NotNull Player tracker, @NotNull Player target);
}
