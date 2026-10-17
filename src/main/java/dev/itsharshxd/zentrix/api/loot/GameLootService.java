package dev.itsharshxd.zentrix.api.loot;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Independent main, Nether, End, and deathmatch pools with per-match frozen state. World,
 * inventory, and generation operations must run on the Bukkit main thread.
 *
 * @since 1.6.0
 */
public interface GameLootService {

    @NotNull LootPoolConfiguration getActiveConfiguration(@NotNull GameLootPool pool);
    @NotNull LootPoolConfiguration getDiskConfiguration(@NotNull GameLootPool pool);
    @NotNull LootValidationResult validate(
            @NotNull GameLootPool pool,
            @NotNull LootPoolConfiguration configuration);
    @NotNull LootEntry createEntry(@NotNull ItemStack item);
    void replaceEntryItem(@NotNull LootEntry entry, @NotNull ItemStack item);
    @NotNull LootSaveResult save(
            @NotNull GameLootPool pool,
            @NotNull LootPoolConfiguration configuration);
    @NotNull LootImportResult importEntries(
            @NotNull GameLootPool pool,
            @NotNull List<LootEntry> entries);
    @NotNull LootReloadResult reload(@NotNull GameLootPool pool);
    boolean reloadAll();
    @NotNull LootGenerationResult preview(
            @NotNull GameLootPool pool,
            @NotNull LootPoolConfiguration configuration);
    @NotNull LootRecoveryState getRecoveryState(@NotNull GameLootPool pool);
    @NotNull LootReloadResult restoreRecoverySnapshot(@NotNull GameLootPool pool);

    /** Freezes all four active configurations for a match if it has no snapshot yet. */
    void prepareGame(@NotNull ZentrixGame game);
    @NotNull Optional<LootPoolConfiguration> getFrozenConfiguration(
            @NotNull ZentrixGame game,
            @NotNull GameLootPool pool);
    @NotNull Map<String, Integer> getAppearanceCounters(
            @NotNull ZentrixGame game,
            @NotNull GameLootPool pool);

    @NotNull Optional<GameLootResolution> resolve(@NotNull World world);
    @NotNull Optional<LootContainerSnapshot> resolveContainer(@NotNull Block block);
    @NotNull Optional<LootContainerSnapshot> resolveContainer(@NotNull Inventory inventory);
    void markPlayerPlaced(@NotNull Block block);
    boolean isProcessed(@NotNull ZentrixGame game, @NotNull Block block);

    /** Lazily generates an eligible original container for an active participant. */
    @NotNull Optional<LootGenerationResult> generateOnOpen(
            @NotNull Player player,
            @NotNull Inventory inventory);

    /** Consumes a destroyed section and optionally generates its loot as world drops. */
    @NotNull Optional<LootGenerationResult> handleDestruction(
            @NotNull Block block,
            boolean generateDrops);

    /**
     * Starts a new refill cycle for every pool of a running match, exactly like a phase's
     * {@code refill} action with scope {@code all}.
     * <p>
     * Refills are lazy: each container is filled again the next time a participant opens it. A
     * replacing refill clears a container's loot slots first; otherwise new loot tops it up.
     * Fires {@link dev.itsharshxd.zentrix.api.events.loot.LootRefillEvent}. Main thread only; a
     * match without prepared loot is left alone.
     * </p>
     *
     * @param replace whether containers are cleared before being filled again
     * @since 1.8.0
     */
    default void refill(@NotNull ZentrixGame game, boolean replace) {
        throw new UnsupportedOperationException("refill requires Zentrix API 1.8.0");
    }

    /**
     * The refill cycle a match's pool is in: 0 until the first refill, then one more per refill.
     *
     * @return the cycle; empty when the match has no state for that pool
     * @since 1.8.0
     */
    @NotNull
    default java.util.OptionalLong getRefillCycle(@NotNull ZentrixGame game, @NotNull LootPool pool) {
        throw new UnsupportedOperationException("getRefillCycle requires Zentrix API 1.8.0");
    }
}
