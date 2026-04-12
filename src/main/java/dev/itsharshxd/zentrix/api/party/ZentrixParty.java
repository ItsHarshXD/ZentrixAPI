package dev.itsharshxd.zentrix.api.party;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a player party in Zentrix.
 * <p>
 * Parties are pre-game groups that allow players to queue and join
 * games together. Each party has a leader who can invite/kick members.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * PartyService partyService = ZentrixAPI.get().getPartyService();
 * Optional<ZentrixParty> party = partyService.getParty(player);
 * party.ifPresent(p -> {
 *     UUID leaderId = p.getLeaderId();
 *     int size = p.getSize();
 *     Set<UUID> members = p.getMembers();
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public interface ZentrixParty {

    /**
     * Gets the unique ID of this party.
     *
     * @return The party's UUID
     */
    @NotNull
    UUID getPartyId();

    /**
     * Gets the UUID of the party leader.
     *
     * @return The leader's UUID
     */
    @NotNull
    UUID getLeaderId();

    /**
     * Gets all member UUIDs in this party (including the leader).
     *
     * @return An unmodifiable set of member UUIDs
     */
    @NotNull
    Set<UUID> getMembers();

    /**
     * Gets the number of members in this party.
     *
     * @return The party size
     */
    int getSize();

    /**
     * Gets the timestamp when this party was created.
     *
     * @return Creation time in milliseconds since epoch
     */
    long getCreatedAt();

    /**
     * Checks if a player is the leader of this party.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is the leader
     */
    boolean isLeader(@NotNull UUID playerId);

    /**
     * Checks if a player is a member of this party.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is a member (including leader)
     */
    boolean isMember(@NotNull UUID playerId);

    /**
     * Gets all online members of this party.
     *
     * @return A list of online players in the party
     */
    @NotNull
    List<Player> getOnlineMembers();
}
