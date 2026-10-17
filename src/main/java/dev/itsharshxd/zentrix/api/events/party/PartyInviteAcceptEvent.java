package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import java.util.UUID;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player accepts a pending party invitation, before they join.
 *
 * <p>Cancel to keep them out; the invitation stays pending, and Zentrix sends no message, so
 * explain it to the player if needed. Zentrix still checks the party's size and the player's own
 * party afterwards. {@link PartyMemberJoinEvent} follows once they have joined. Main thread; only
 * fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class PartyInviteAcceptEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID inviterId;
    private final UUID inviteeId;
    private boolean cancelled;

    public PartyInviteAcceptEvent(
            @NotNull ZentrixParty party, @NotNull UUID inviterId, @NotNull UUID inviteeId) {
        super(false);
        this.party = party;
        this.inviterId = inviterId;
        this.inviteeId = inviteeId;
    }

    /** The party the player would join. */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /** The leader who sent the invitation. */
    @NotNull
    public UUID getInviterId() {
        return inviterId;
    }

    /** The accepting player. */
    @NotNull
    public UUID getInviteeId() {
        return inviteeId;
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
