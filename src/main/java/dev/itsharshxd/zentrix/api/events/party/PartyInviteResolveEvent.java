package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.PartyInvitation;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called once a party invitation has ended, however it ended: each stored invitation is reported
 * exactly once, except those still pending when Zentrix shuts down. A notification: the outcome has
 * already been applied.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class PartyInviteResolveEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PartyInvitation invitation;
    private final PartyInvitation.Resolution resolution;

    public PartyInviteResolveEvent(
            @NotNull PartyInvitation invitation, @NotNull PartyInvitation.Resolution resolution) {
        super(false);
        this.invitation = invitation;
        this.resolution = resolution;
    }

    /** The invitation, as it stood. */
    @NotNull
    public PartyInvitation getInvitation() {
        return invitation;
    }

    /** How it ended. */
    @NotNull
    public PartyInvitation.Resolution getResolution() {
        return resolution;
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
