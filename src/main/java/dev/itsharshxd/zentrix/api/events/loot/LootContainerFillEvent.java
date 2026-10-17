package dev.itsharshxd.zentrix.api.events.loot;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.loot.LootContainerType;
import dev.itsharshxd.zentrix.api.loot.LootPool;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before Zentrix generates lazy loot into a container: when a participant first opens it,
 * when they open it again after a refill, or when it is destroyed before being opened and its loot
 * drops instead.
 *
 * <p>The container has already been claimed for this refill cycle when this fires, so cancelling
 * leaves it exactly as it is - with whatever it holds now, not cleared even for a replacing
 * refill - and it is not generated again until the next refill. Use
 * {@link LootContainerFilledEvent} to adjust what was generated instead.
 *
 * <p>A container that holds slots of two pools, such as a double chest on the Cornucopia's edge,
 * fires once per pool. Eager Cornucopia population at match start does not fire this event.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class LootContainerFillEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final LootPool pool;
    private final LootContainerType containerType;
    private final Location location;
    private final Player player;
    private final long cycle;
    private final boolean replacing;
    private final boolean destroyed;
    private boolean cancelled;

    public LootContainerFillEvent(
            @NotNull ZentrixGame game,
            @NotNull LootPool pool,
            @NotNull LootContainerType containerType,
            @NotNull Location location,
            @Nullable Player player,
            long cycle,
            boolean replacing,
            boolean destroyed) {
        super(game);
        this.pool = Objects.requireNonNull(pool, "pool");
        this.containerType = Objects.requireNonNull(containerType, "containerType");
        this.location = Objects.requireNonNull(location, "location").clone();
        this.player = player;
        this.cycle = cycle;
        this.replacing = replacing;
        this.destroyed = destroyed;
    }

    /** The pool the loot comes from. */
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

    /** The refill cycle being generated: 0 for the first fill, then one more per refill. */
    public long getCycle() {
        return cycle;
    }

    /** Whether this is a replacing refill, which clears the container's loot slots first. */
    public boolean isReplacing() {
        return replacing;
    }

    /** Whether the container was destroyed, so the loot will drop at its location. */
    public boolean isDestroyed() {
        return destroyed;
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
