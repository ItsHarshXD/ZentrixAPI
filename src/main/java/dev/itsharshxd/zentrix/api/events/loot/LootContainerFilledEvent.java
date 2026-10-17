package dev.itsharshxd.zentrix.api.events.loot;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.loot.LootContainerType;
import dev.itsharshxd.zentrix.api.loot.LootGenerationResult;
import dev.itsharshxd.zentrix.api.loot.LootPool;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after Zentrix generated lazy loot for a container, once per pool.
 *
 * <p>For an opened container the loot is already in {@link #getInventory()}, which listeners may
 * edit directly - add, remove or replace items - before the player sees it. For a destroyed
 * container nothing has dropped yet: {@link #getDrops()} is the mutable list of items about to drop
 * at its location. Appearance counters and the generation result are final either way; editing
 * items does not give back or consume appearances of the pool's entries.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class LootContainerFilledEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final LootPool pool;
    private final LootContainerType containerType;
    private final Location location;
    private final Player player;
    private final long cycle;
    private final LootGenerationResult result;
    private final Inventory inventory;
    private final List<ItemStack> drops;

    public LootContainerFilledEvent(
            @NotNull ZentrixGame game,
            @NotNull LootPool pool,
            @NotNull LootContainerType containerType,
            @NotNull Location location,
            @Nullable Player player,
            long cycle,
            @NotNull LootGenerationResult result,
            @Nullable Inventory inventory,
            @Nullable List<ItemStack> drops) {
        super(game);
        this.pool = Objects.requireNonNull(pool, "pool");
        this.containerType = Objects.requireNonNull(containerType, "containerType");
        this.location = Objects.requireNonNull(location, "location").clone();
        this.player = player;
        this.cycle = cycle;
        this.result = Objects.requireNonNull(result, "result");
        this.inventory = inventory;
        this.drops = inventory == null ? (drops == null ? new ArrayList<>() : drops) : null;
    }

    /** The pool the loot came from. */
    @NotNull
    public LootPool getPool() {
        return pool;
    }

    /** The kind of container. */
    @NotNull
    public LootContainerType getContainerType() {
        return containerType;
    }

    /** Where the container is; a copy. */
    @NotNull
    public Location getLocation() {
        return location.clone();
    }

    /** The participant who opened it; empty when it was destroyed. */
    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    /** The refill cycle generated: 0 for the first fill, then one more per refill. */
    public long getCycle() {
        return cycle;
    }

    /** What was generated: placements, appearances that found no room, failures. */
    @NotNull
    public LootGenerationResult getResult() {
        return result;
    }

    /** Whether the container was destroyed, so the loot drops rather than staying inside. */
    public boolean isDestroyed() {
        return inventory == null;
    }

    /** The container's live inventory holding the loot; empty for a destroyed container. */
    @NotNull
    public Optional<Inventory> getInventory() {
        return Optional.ofNullable(inventory);
    }

    /**
     * The items about to drop from a destroyed container, which listeners may change; an empty,
     * unused list for an opened container.
     */
    @NotNull
    public List<ItemStack> getDrops() {
        return drops == null ? new ArrayList<>() : drops;
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
