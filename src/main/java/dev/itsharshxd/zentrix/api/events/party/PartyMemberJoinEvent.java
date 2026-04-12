package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Called when a player joins a party (accepts an invite).
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public class PartyMemberJoinEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID memberId;

    public PartyMemberJoinEvent(@NotNull ZentrixParty party, @NotNull UUID memberId) {
        super(false);
        this.party = party;
        this.memberId = memberId;
    }

    /**
     * Gets the party the player joined.
     *
     * @return The party
     */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /**
     * Gets the UUID of the player who joined the party.
     *
     * @return The new member's UUID
     */
    @NotNull
    public UUID getMemberId() {
        return memberId;
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
