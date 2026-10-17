package dev.itsharshxd.zentrix.api.events.identity;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.identity.MaskedIdentity;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a player's mask was removed: its handle was released, its match ended, its owner
 * disabled, or an addon unmasked the player. Their real profile is restored to everybody now, or
 * as soon as they reconnect when they are offline. Not fired while Zentrix shuts down.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class IdentityUnmaskEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final MaskedIdentity identity;

    public IdentityUnmaskEvent(@NotNull UUID playerId, @NotNull MaskedIdentity identity) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.identity = Objects.requireNonNull(identity, "identity");
    }

    /** The player who is no longer masked. */
    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    /** The identity they wore. */
    @NotNull
    public MaskedIdentity getIdentity() {
        return identity;
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
