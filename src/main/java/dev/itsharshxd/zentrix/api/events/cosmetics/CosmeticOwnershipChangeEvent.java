package dev.itsharshxd.zentrix.api.events.cosmetics;

import dev.itsharshxd.zentrix.api.cosmetics.CosmeticCategory;
import dev.itsharshxd.zentrix.api.cosmetics.CosmeticChange;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a change to what a player owns or chose was stored: a grant or revocation by an
 * administrator or addon, a choice in the cosmetics menu or through the API, or ownership imported
 * from MatrixGliders. Only fired when something actually changed. The player may be offline.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CosmeticOwnershipChangeEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final CosmeticChange change;

    public CosmeticOwnershipChangeEvent(@NotNull UUID playerId, @NotNull CosmeticChange change) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.change = Objects.requireNonNull(change, "change");
    }

    /** The player whose cosmetics changed. */
    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    /** The category that changed. */
    @NotNull
    public CosmeticCategory getCategory() {
        return change.after().category();
    }

    /** The cosmetics before and after. */
    @NotNull
    public CosmeticChange getChange() {
        return change;
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
