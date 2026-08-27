package dev.itsharshxd.zentrix.api.loot;

import java.util.List;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/** Discovery, preview, and import of local JSON and Minecraft chest loot tables. @since 1.6.0 */
public interface LootTableCatalogService {

    @NotNull List<LootTableReference> list(@NotNull LootTableCategory category);

    /**
     * Previews a table. Minecraft tables use the supplied location as their loot context; local
     * JSON tables ignore it. Native table sampling and all Bukkit access must run on the main
     * thread.
     */
    @NotNull LootTablePreview preview(
            @NotNull LootTableReference reference,
            @NotNull Location context);

    /** Imports a fresh copy of every preview entry into the selected pool. */
    @NotNull LootImportResult importTable(
            @NotNull LootTableReference reference,
            @NotNull Location context,
            @NotNull LootPool target);
}
