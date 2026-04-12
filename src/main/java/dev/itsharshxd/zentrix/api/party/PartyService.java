package dev.itsharshxd.zentrix.api.party;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Service for party-related operations in Zentrix.
 * <p>
 * Provides access to the in-memory party system, allowing addons to
 * query party state, membership, and perform party operations.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * PartyService partyService = ZentrixAPI.get().getPartyService();
 *
 * // Check if player is in a party
 * if (partyService.isInParty(player)) {
 *     ZentrixParty party = partyService.getParty(player).orElse(null);
 *     if (party != null) {
 *         player.sendMessage("Party size: " + party.getSize());
 *     }
 * }
 *
 * // Check if player is a party leader
 * if (partyService.isPartyLeader(player)) {
 *     player.sendMessage("You are the party leader!");
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public interface PartyService {

    /**
     * Gets the party a player belongs to.
     *
     * @param player The player
     * @return Optional containing the party, or empty if not in a party
     */
    @NotNull
    Optional<ZentrixParty> getParty(@NotNull Player player);

    /**
     * Gets the party a player belongs to by UUID.
     *
     * @param playerId The player's UUID
     * @return Optional containing the party, or empty if not in a party
     */
    @NotNull
    Optional<ZentrixParty> getParty(@NotNull UUID playerId);

    /**
     * Checks if a player is currently in a party.
     *
     * @param player The player
     * @return {@code true} if the player is in a party
     */
    boolean isInParty(@NotNull Player player);

    /**
     * Checks if a player is currently in a party by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player is in a party
     */
    boolean isInParty(@NotNull UUID playerId);

    /**
     * Checks if a player is the leader of their party.
     *
     * @param player The player
     * @return {@code true} if the player is a party leader
     */
    boolean isPartyLeader(@NotNull Player player);

    /**
     * Creates a new party with the given player as the leader.
     *
     * @param leader The player who will be the party leader
     * @return Optional containing the created party, or empty if the player is already in a party
     */
    @NotNull
    Optional<ZentrixParty> createParty(@NotNull Player leader);

    /**
     * Disbands the party the given player leads.
     * <p>
     * Only the party leader can disband. All members are removed
     * and notified.
     * </p>
     *
     * @param leader The party leader
     * @return {@code true} if the party was disbanded, {@code false} if the
     *         player is not a party leader
     */
    boolean disbandParty(@NotNull Player leader);

    /**
     * Sends a party invite from the leader to a target player.
     *
     * @param leader The party leader sending the invite
     * @param target The target player to invite
     * @return {@code true} if the invite was sent successfully
     */
    boolean invitePlayer(@NotNull Player leader, @NotNull Player target);

    /**
     * Removes the given player from their party.
     * <p>
     * If the player is the leader, leadership is transferred
     * to another member. If the player is the last member,
     * the party is disbanded.
     * </p>
     *
     * @param player The player leaving the party
     */
    void leaveParty(@NotNull Player player);

    /**
     * Kicks a member from the party.
     * <p>
     * Only the party leader can kick members.
     * </p>
     *
     * @param leader   The party leader performing the kick
     * @param targetId The UUID of the member to kick
     * @return {@code true} if the member was kicked, {@code false} if the
     *         leader doesn't have permission or the target is not in the party
     */
    boolean kickMember(@NotNull Player leader, @NotNull UUID targetId);

    /**
     * Promotes a party member to leader.
     * <p>
     * Only the current leader can promote another member.
     * </p>
     *
     * @param leader   The current party leader
     * @param targetId The UUID of the member to promote
     * @return {@code true} if the promotion was successful
     */
    boolean promoteLeader(@NotNull Player leader, @NotNull UUID targetId);

    /**
     * Gets the maximum party size allowed.
     *
     * @return The maximum party size
     */
    int getMaxPartySize();
}
