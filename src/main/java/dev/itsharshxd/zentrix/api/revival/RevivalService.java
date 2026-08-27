package dev.itsharshxd.zentrix.api.revival;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Teammate revival state and operations. Methods touching players or locations must be called on
 * the Bukkit main thread. An API override takes precedence over every later phase toggle until
 * {@link #clearOverride(ZentrixGame)} is called.
 *
 * @since 1.6.0
 */
public interface RevivalService {

    boolean isAllowed(@NotNull ZentrixGame game);

    @NotNull Optional<Boolean> getOverride(@NotNull ZentrixGame game);

    void setOverride(@NotNull ZentrixGame game, boolean allowed);

    void clearOverride(@NotNull ZentrixGame game);

    void recordElimination(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @Nullable ItemStack[] lostLoot);

    void clearElimination(@NotNull UUID playerId);

    @NotNull Optional<EliminationSnapshot> getElimination(
            @NotNull ZentrixGame game,
            @NotNull UUID playerId);

    @NotNull List<Player> getEligibleTeammates(
            @NotNull ZentrixGame game,
            @NotNull Player reviver);

    boolean isReviverEligible(@NotNull ZentrixGame game, @NotNull Player reviver);

    boolean isTargetEligible(
            @NotNull ZentrixGame game,
            @NotNull Player reviver,
            @NotNull Player target);

    @NotNull Optional<Location> findSafeLocation(@NotNull Player reviver);

    @NotNull RevivalResult revive(
            @NotNull ZentrixGame game,
            @NotNull Player reviver,
            @NotNull Player target,
            @NotNull RevivalOptions options);

    @NotNull
    default RevivalResult revive(
            @NotNull ZentrixGame game,
            @NotNull Player reviver,
            @NotNull Player target) {
        return revive(game, reviver, target, RevivalOptions.defaults());
    }

    /**
     * Hands a player back whatever {@code arena-management.revival} allows, without reviving them.
     *
     * <p>This is the restoration half of a revival on its own, for anything that returns a player
     * to a match by its own route — a scenario that takes a death over, a life system, a role that
     * respawns. The player is expected to be in the match already and to have an empty inventory,
     * exactly as a death leaves them.
     *
     * <p>The configured options decide what happens, so a mechanic built on this stays in step with
     * the server's revival settings instead of inventing restoration rules of its own.
     * {@link RevivalOptions#restoreClassItems()} and {@link RevivalOptions#restoreLostLoot()}
     * override the configuration for one call when a caller genuinely needs to;
     * {@link RevivalOptions#destination()} is ignored here, because nobody is being moved.
     *
     * <p>The lost loot is restored slot by slot where it fits and added or dropped where it does
     * not. The class kit is granted afterwards and only fills what is still free, so the two never
     * fight over the same slots.
     *
     * @param game     the match the player belongs to
     * @param player   the player to restore, already in the match
     * @param lostLoot the inventory they died with, slot by slot; null when nothing was captured,
     *                 in which case there is no loot to hand back
     * @param options  per-call overrides; {@link RevivalOptions#defaults()} follows the
     *                 configuration exactly
     * @return what was handed back
     * @since 1.6.0
     */
    @NotNull
    RevivalRestoration restoreLoadout(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @Nullable ItemStack[] lostLoot,
            @NotNull RevivalOptions options);

    /** Restores a player exactly as {@code arena-management.revival} is configured. */
    @NotNull
    default RevivalRestoration restoreLoadout(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @Nullable ItemStack[] lostLoot) {
        return restoreLoadout(game, player, lostLoot, RevivalOptions.defaults());
    }
}
