package dev.itsharshxd.zentrix.api.rejoin;

import dev.itsharshxd.zentrix.api.corpse.ZentrixCorpse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Corpse-backed rejoin: a player who left a running match can return to it through the corpse
 * they left behind, with their inventory, experience, ender chest and team.
 *
 * <p>This is the same operation {@code /rejoin} performs, with the same rules from
 * {@code settings.yml} rejoin-settings. A return can be refused before it commits by cancelling
 * {@link dev.itsharshxd.zentrix.api.events.player.PlayerJoinGameEvent}, whose
 * {@code isRejoin()} is true for it; a committed one is reported by
 * {@link dev.itsharshxd.zentrix.api.events.player.PlayerRejoinGameEvent}, and the corpse is then
 * removed with {@link dev.itsharshxd.zentrix.api.corpse.CorpseRemovalReason#REJOINED}.
 *
 * @since 1.8.0
 */
public interface RejoinService {

    /**
     * Whether a player can rejoin now. Main thread only.
     *
     * @param player the player
     * @return their status
     */
    @NotNull
    RejoinStatus getStatus(@NotNull Player player);

    /**
     * The corpse that holds a player's return, online or not.
     *
     * @param playerId the player
     * @return the corpse, or empty when they hold no return
     */
    @NotNull
    Optional<ZentrixCorpse> getReturnableCorpse(@NotNull UUID playerId);

    /**
     * Returns a player to the match they left, exactly as {@code /rejoin} does - its messages to
     * the player and the match included. Main thread only.
     *
     * <p>The corpse's chunk may have to be loaded first, so the future can complete a few ticks
     * later, on the main thread. It always completes: with {@link RejoinEligibility#ELIGIBLE} once
     * the player is back, otherwise with why they are not. A failed return changes nothing, and
     * the corpse stays for another attempt.
     *
     * @param player the returning player
     * @return the outcome
     */
    @NotNull
    CompletableFuture<RejoinEligibility> rejoin(@NotNull Player player);
}
