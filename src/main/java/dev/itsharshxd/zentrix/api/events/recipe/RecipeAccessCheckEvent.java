package dev.itsharshxd.zentrix.api.events.recipe;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.recipe.ZentrixRecipe;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when Zentrix is about to allow a custom recipe: a player preparing it in a crafting grid
 * or at a stonecutter, or a furnace, smoker or campfire about to cook it.
 *
 * <p>This is an additional access policy. It only fires once Zentrix's own rules - disabled
 * phases and game types, one-time use and craft limits - have allowed the recipe; cancel it to
 * refuse the recipe this time, and it behaves as if it did not exist: nothing is shown, said or
 * cooked. A listener cannot allow a recipe Zentrix refused.
 *
 * <p>It is asked often - a lit furnace asks on every cooking tick - so listeners must be cheap.
 * Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class RecipeAccessCheckEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixRecipe recipe;
    private final Player player;
    private final World world;
    private boolean cancelled;

    public RecipeAccessCheckEvent(
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

    /** The crafting player; empty when a block cooks on its own. */
    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    /** The world the recipe is made in, when known. */
    @NotNull
    public Optional<World> getWorld() {
        return Optional.ofNullable(world);
    }

    /** Whether a listener refused the recipe. */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /** Refuses the recipe this time. */
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
