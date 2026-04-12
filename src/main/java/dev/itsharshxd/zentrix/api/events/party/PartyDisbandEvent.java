package dev.itsharshxd.zentrix.api.events.party;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.party.ZentrixParty;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Called when a party is disbanded.
 * <p>
 * At the time this event fires, the party still contains all members
 * so listeners can inspect the final state.
 * </p>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public class PartyDisbandEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixParty party;
    private final UUID disbandedBy;

    public PartyDisbandEvent(@NotNull ZentrixParty party, @NotNull UUID disbandedBy) {
        super(false);
        this.party = party;
        this.disbandedBy = disbandedBy;
    }

    /**
     * Gets the party that is being disbanded.
     *
     * @return The party
     */
    @NotNull
    public ZentrixParty getParty() {
        return party;
    }

    /**
     * Gets the UUID of the player who disbanded the party.
     *
     * @return The disbander's UUID (typically the leader)
     */
    @NotNull
    public UUID getDisbandedBy() {
        return disbandedBy;
    }

    /**
     * Gets all members who were in the party at the time of disbanding.
     *
     * @return An unmodifiable set of member UUIDs
     */
    @NotNull
    public Set<UUID> getMembers() {
        return party.getMembers();
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
