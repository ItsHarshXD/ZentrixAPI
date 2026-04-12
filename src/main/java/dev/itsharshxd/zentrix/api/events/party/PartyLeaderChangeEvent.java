package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Called when the leader of a party changes (promotion or leader leaving).
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public class PartyLeaderChangeEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID oldLeaderId;
    private final UUID newLeaderId;

    public PartyLeaderChangeEvent(@NotNull ZentrixParty party,
                                  @NotNull UUID oldLeaderId,
                                  @NotNull UUID newLeaderId) {
        super(false);
        this.party = party;
        this.oldLeaderId = oldLeaderId;
        this.newLeaderId = newLeaderId;
    }

    /**
     * Gets the party whose leadership changed.
     *
     * @return The party
     */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /**
     * Gets the UUID of the previous leader.
     *
     * @return The old leader's UUID
     */
    @NotNull
    public UUID getOldLeaderId() {
        return oldLeaderId;
    }

    /**
     * Gets the UUID of the new leader.
     *
     * @return The new leader's UUID
     */
    @NotNull
    public UUID getNewLeaderId() {
        return newLeaderId;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
