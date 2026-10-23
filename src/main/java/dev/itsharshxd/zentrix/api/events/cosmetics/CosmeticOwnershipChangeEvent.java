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
 * administrator or addon, a choice in the cosmetics menu or through the API, a timed cosmetic
 * running out, or ownership imported from MatrixGliders. Only fired when something actually
 * changed. The player may be offline.
 *
 * <p>Only for built-in categories, so {@link #getCategory()} always has one; changes to an addon's
 * types are not reported here.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CosmeticOwnershipChangeEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * What made the change.
     *
     * @since 1.9.0
     */
    public enum Cause {
        /** Cosmetics were granted, permanently or for a limited time, or a timed grant extended. */
        GRANTED,
        /** Cosmetics were taken away by an administrator or addon. */
        REVOKED,
        /** The player's choice changed, in the cosmetics menu or through the API. */
        SELECTED,
        /** Timed cosmetics ran out. A shop may offer to renew them. */
        EXPIRED,
        /** Ownership was imported from another plugin. */
        IMPORTED
    }

    private final UUID playerId;
    private final CosmeticChange change;
    private final Cause cause;

    public CosmeticOwnershipChangeEvent(@NotNull UUID playerId, @NotNull CosmeticChange change) {
        this(playerId, change, Cause.GRANTED);
    }

    /** @since 1.9.0 */
    public CosmeticOwnershipChangeEvent(@NotNull UUID playerId, @NotNull CosmeticChange change,
                                        @NotNull Cause cause) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.change = Objects.requireNonNull(change, "change");
        this.cause = Objects.requireNonNull(cause, "cause");
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

    /**
     * What made the change.
     *
     * @since 1.9.0
     */
    @NotNull
    public Cause getCause() {
        return cause;
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
