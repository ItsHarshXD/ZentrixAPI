package dev.itsharshxd.zentrix.api.events.loot;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.loot.LootPool;
import java.util.Set;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a loot refill started a new cycle for some of a match's pools, whether a phase's
 * refill action or {@code GameLootService#refill} triggered it.
 *
 * <p>Refills are lazy: no container is touched now. Each container is filled again the next time
 * a participant opens it, firing {@link LootContainerFillEvent}. Several refills before a container
 * is opened collapse into the latest one.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class LootRefillEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Set<LootPool> pools;
    private final boolean replacing;

    public LootRefillEvent(@NotNull ZentrixGame game, @NotNull Set<LootPool> pools, boolean replacing) {
        super(game);
        this.pools = Set.copyOf(pools);
        this.replacing = replacing;
    }

    /** The pools that started a new cycle. */
    @NotNull
    public Set<LootPool> getPools() {
        return pools;
    }

    /** Whether containers are cleared before being filled again, rather than topped up. */
    public boolean isReplacing() {
        return replacing;
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
