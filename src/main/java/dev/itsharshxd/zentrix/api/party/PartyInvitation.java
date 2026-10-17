package dev.itsharshxd.zentrix.api.party;

import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * A pending party invitation. A snapshot: accepting, denying or expiring it does not change it.
 *
 * @param inviterId the party leader who sent it
 * @param inviteeId the invited player
 * @param partyId   the party the invitee would join
 * @param expiresAt when it expires, in epoch milliseconds
 * @since 1.8.0
 */
public record PartyInvitation(
        @NotNull UUID inviterId, @NotNull UUID inviteeId, @NotNull UUID partyId, long expiresAt) {

    public PartyInvitation {
        Objects.requireNonNull(inviterId, "inviterId");
        Objects.requireNonNull(inviteeId, "inviteeId");
        Objects.requireNonNull(partyId, "partyId");
    }

    /** Whether it has expired by now. */
    public boolean isExpired() {
        return System.currentTimeMillis() >= expiresAt;
    }

    /** How an invitation ended. */
    public enum Resolution {
        /** The invitee accepted and joined the party. */
        ACCEPTED,
        /** The invitee turned it down. */
        DENIED,
        /** It ran out before the invitee answered. */
        EXPIRED,
        /**
         * It was withdrawn or could no longer be honoured: the leader left, the party disbanded or
         * filled up, the invitee joined another party or disconnected, or an addon cancelled it.
         */
        CANCELLED
    }
}
