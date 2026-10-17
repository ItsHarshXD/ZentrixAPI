package dev.itsharshxd.zentrix.api.compass;

import dev.itsharshxd.zentrix.api.addon.Registration;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Teammate tracker item, target selection, and live action-bar control.
 *
 * <p>Since 1.8.0 the tracker can also be customized: {@link #registerTargetFilter} narrows who a
 * compass may point at, {@link dev.itsharshxd.zentrix.api.events.compass.CompassTargetChangeEvent}
 * reports every target change, and
 * {@link dev.itsharshxd.zentrix.api.events.compass.CompassTrackerDisplayEvent} rewrites or hides
 * the tracker's action bar. Teammate-only tracking stays the rule either way.
 *
 * @since 1.6.0
 */
public interface CompassTrackerService {
    boolean isEnabled();
    @NotNull Optional<ItemStack> createTrackerItem();
    boolean isTrackerItem(@Nullable ItemStack item);
    boolean giveTracker(@NotNull ZentrixGame game, @NotNull Player player);
    boolean isTeamBased(@NotNull ZentrixGame game);
    @NotNull List<Player> getEligibleTargets(@NotNull Player player);
    boolean cycleTarget(@NotNull Player player);
    boolean setTarget(@NotNull Player player, @NotNull Player target);
    @NotNull Optional<Player> getCurrentTarget(@NotNull Player player);
    void clearTarget(@NotNull Player player);
    void refresh(@NotNull Player player);
    boolean isActionBarEnabled(@NotNull Player player);
    void setActionBarEnabled(@NotNull Player player, boolean enabled);
    void clearActionBar(@NotNull Player player);

    /**
     * How often, in ticks, Zentrix repoints every tracker and redraws its action bar.
     *
     * @since 1.8.0
     */
    default long getUpdateIntervalTicks() {
        return 0L;
    }

    /**
     * Adds a filter every tracker target must pass, on top of Zentrix's own teammate rules.
     *
     * <p>Filters only narrow the eligible teammates, in {@link #getEligibleTargets},
     * {@link #cycleTarget}, {@link #setTarget} and the automatic nearest-teammate choice alike; a
     * remembered target a filter starts refusing is dropped at the next update. A teammate is
     * eligible only when every registered filter allows it.
     *
     * @param owner  the registering plugin; the filter is removed when it is disabled
     * @param filter the filter
     * @return the registration, to remove the filter early
     * @since 1.8.0
     */
    @NotNull
    default Registration registerTargetFilter(@NotNull Plugin owner, @NotNull CompassTargetFilter filter) {
        throw new UnsupportedOperationException("Compass target filters require Zentrix API 1.8.0");
    }
}
