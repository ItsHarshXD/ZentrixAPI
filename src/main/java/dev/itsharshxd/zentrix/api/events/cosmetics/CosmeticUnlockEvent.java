package dev.itsharshxd.zentrix.api.events.cosmetics;

import dev.itsharshxd.zentrix.api.cosmetics.CosmeticTypeKey;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before cosmetics are unlocked for a player through the API: granted by an addon with
 * {@code CosmeticsService#grant} or {@code #grantTimed}, or bought with
 * {@code CosmeticsService#purchase} or in the cosmetics menu. The player may be offline.
 *
 * <p>Cancelling leaves the player's cosmetics as they are; a cancelled purchase takes no payment.
 * Grants made by administrators' commands do not fire this event.
 * {@link CosmeticOwnershipChangeEvent} reports every change after it was stored.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.11.0
 */
public final class CosmeticUnlockEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /** How the cosmetics are unlocked. */
    public enum Cause {
        /** An addon grants them. */
        GRANT,
        /** The player buys one. */
        PURCHASE
    }

    private final UUID playerId;
    private final CosmeticTypeKey type;
    private final Set<String> cosmeticIds;
    private final Cause cause;
    private final Duration duration;
    private final Double price;
    private boolean cancelled;

    /**
     * @param duration how long a timed grant lasts, or null for a permanent one
     * @param price    what a purchase costs, or null for a grant
     */
    public CosmeticUnlockEvent(@NotNull UUID playerId, @NotNull CosmeticTypeKey type,
                               @NotNull Collection<String> cosmeticIds, @NotNull Cause cause,
                               @Nullable Duration duration, @Nullable Double price) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.type = Objects.requireNonNull(type, "type");
        this.cosmeticIds = Set.copyOf(cosmeticIds);
        this.cause = Objects.requireNonNull(cause, "cause");
        this.duration = duration;
        this.price = price;
    }

    /** The player gaining them. */
    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    /** The type they belong to. */
    @NotNull
    public CosmeticTypeKey getType() {
        return type;
    }

    /** The cosmetics unlocked. */
    @NotNull
    public Set<String> getCosmeticIds() {
        return cosmeticIds;
    }

    /** How they are unlocked. */
    @NotNull
    public Cause getCause() {
        return cause;
    }

    /** How long a timed grant lasts; empty for a permanent unlock. */
    @NotNull
    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    /** What a purchase costs; empty for a grant. */
    @NotNull
    public OptionalDouble getPrice() {
        return price == null ? OptionalDouble.empty() : OptionalDouble.of(price);
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
