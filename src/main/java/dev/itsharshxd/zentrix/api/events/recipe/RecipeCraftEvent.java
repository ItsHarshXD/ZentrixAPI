package dev.itsharshxd.zentrix.api.events.recipe;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.recipe.ZentrixRecipe;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a custom recipe was crafted or cooked and Zentrix counted it towards its limit.
 *
 * <p>A notification: the result has been made, the runtime-game counter that
 * {@code RecipeService#getGlobalCraftCount} reads has been advanced, and any crafting reward is on
 * its way. A block that cooked on its own reports no player. Main thread; only fired while a
 * listener is registered.
 *
 * @since 1.8.0
 */
public final class RecipeCraftEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixRecipe recipe;
    private final Player player;
    private final World world;

    public RecipeCraftEvent(
            @NotNull ZentrixRecipe recipe, @Nullable Player player, @Nullable World world) {
        super(false);
        this.recipe = recipe;
        this.player = player;
        this.world = world;
    }

    /** The custom recipe. */
    @NotNull
    public ZentrixRecipe getRecipe() {
        return recipe;
    }

    /** The crafting player; empty when a block cooked on its own. */
    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    /** The world it was made in, when known. */
    @NotNull
    public Optional<World> getWorld() {
        return Optional.ofNullable(world);
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
