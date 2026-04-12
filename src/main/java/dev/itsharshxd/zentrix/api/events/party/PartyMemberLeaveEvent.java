package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Called when a player leaves a party.
 * <p>
 * This covers all forms of leaving: voluntary leave, being kicked,
 * disconnect handling, and party disbanding.
 * </p>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public class PartyMemberLeaveEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID memberId;
    private final LeaveReason reason;

    public PartyMemberLeaveEvent(@NotNull ZentrixParty party,
                                 @NotNull UUID memberId,
                                 @NotNull LeaveReason reason) {
        super(false);
        this.party = party;
        this.memberId = memberId;
        this.reason = reason;
    }

    /**
     * Gets the party the player left.
     *
     * @return The party
     */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /**
     * Gets the UUID of the player who left.
     *
     * @return The member's UUID
     */
    @NotNull
    public UUID getMemberId() {
        return memberId;
    }

    /**
     * Gets the reason for leaving.
     *
     * @return The leave reason
     */
    @NotNull
    public LeaveReason getReason() {
        return reason;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Reasons a player can leave a party.
     */
    public enum LeaveReason {
        /** Player voluntarily left */
        LEFT,
        /** Player was kicked by the leader */
        KICKED,
        /** Player disconnected */
        DISCONNECT,
        /** Party was disbanded */
        DISBAND
    }
}
