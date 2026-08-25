package dev.itsharshxd.zentrix.api.loot;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/** Cornucopia pool persistence plus eager prepared-arena population. @since 1.6.0 */
public interface CornucopiaLootService {

    @NotNull LootPoolConfiguration getActiveConfiguration();
    @NotNull LootPoolConfiguration getDiskConfiguration();
    @NotNull LootValidationResult validate(@NotNull LootPoolConfiguration configuration);
    @NotNull LootEntry createEntry(@NotNull ItemStack item);
    void replaceEntryItem(@NotNull LootEntry entry, @NotNull ItemStack item);
    @NotNull LootSaveResult save(@NotNull LootPoolConfiguration configuration);
    @NotNull LootImportResult importEntries(@NotNull List<LootEntry> entries);
    @NotNull LootReloadResult reload();
    @NotNull LootGenerationResult preview(@NotNull LootPoolConfiguration configuration);
    @NotNull LootRecoveryState getRecoveryState();
    @NotNull LootReloadResult restoreRecoverySnapshot();

    /** Eligible physical containers currently inside the prepared Cornucopia. */
    @NotNull List<LootContainerSnapshot> getEligibleContainers(@NotNull ZentrixGame game);

    /** Clears and eagerly repopulates the prepared Cornucopia using the active pool. */
    @NotNull Optional<LootGenerationResult> populate(@NotNull ZentrixGame game);
}
