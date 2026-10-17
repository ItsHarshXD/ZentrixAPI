package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import java.util.UUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a party leader invites a player, after Zentrix's own checks passed and before the
 * invitation is stored or anyone is messaged.
 *
 * <p>Cancel to refuse the invitation; Zentrix sends no message then, so explain it to the leader
 * if needed. The expiry can be changed within {@value #MIN_EXPIRATION_SECONDS} to
 * {@value #MAX_EXPIRATION_SECONDS} seconds. Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class PartyInviteEvent extends ZentrixEvent implements Cancellable {

    /** The shortest expiry a listener may set. */
    public static final int MIN_EXPIRATION_SECONDS = 5;

    /** The longest expiry a listener may set. */
    public static final int MAX_EXPIRATION_SECONDS = 3600;

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID inviterId;
    private final UUID inviteeId;
    private int expirationSeconds;
    private boolean cancelled;

    public PartyInviteEvent(
            @NotNull ZentrixParty party, @NotNull UUID inviterId, @NotNull UUID inviteeId,
            int expirationSeconds) {
        super(false);
        this.party = party;
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
        this.expirationSeconds = expirationSeconds;
    }

    /** The inviting party. */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /** The leader sending the invitation. */
    @NotNull
    public UUID getInviterId() {
        return inviterId;
    }

    /** The invited player. */
    @NotNull
    public UUID getInviteeId() {
        return inviteeId;
    }

    /** Seconds until the invitation expires; the configured expiry unless a listener changed it. */
    public int getExpirationSeconds() {
        return expirationSeconds;
    }

    /**
     * Changes how long the invitation stays open.
     *
     * @param seconds the expiry, clamped to {@value #MIN_EXPIRATION_SECONDS}..{@value #MAX_EXPIRATION_SECONDS}
     */
    public void setExpirationSeconds(int seconds) {
        this.expirationSeconds = Math.max(MIN_EXPIRATION_SECONDS, Math.min(MAX_EXPIRATION_SECONDS, seconds));
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
