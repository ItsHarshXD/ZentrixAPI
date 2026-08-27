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
}
