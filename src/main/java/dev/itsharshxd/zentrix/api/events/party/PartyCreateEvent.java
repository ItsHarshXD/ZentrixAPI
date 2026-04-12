package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Called when a new party is created.
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public class PartyCreateEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID leaderId;

    public PartyCreateEvent(@NotNull ZentrixParty party, @NotNull UUID leaderId) {
        super(false);
        this.party = party;
        this.leaderId = leaderId;
    }

    /**
     * Gets the party that was created.
     *
     * @return The created party
     */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /**
     * Gets the UUID of the player who created the party.
     *
     * @return The leader's UUID
     */
    @NotNull
    public UUID getLeaderId() {
        return leaderId;
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
