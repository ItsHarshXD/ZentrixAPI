package dev.itsharshxd.zentrix.api.compass;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Teammate tracker item, target selection, and live action-bar control. @since 1.6.0 */
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
}
