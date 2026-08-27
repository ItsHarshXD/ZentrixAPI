package dev.itsharshxd.zentrix.api.revival;

import java.util.Arrays;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Immutable view of one player elimination retained for teammate revival. */
public record EliminationSnapshot(
        @NotNull UUID playerId,
        @NotNull String teamId,
        @Nullable ItemStack[] lostLoot) {

    public EliminationSnapshot {
        lostLoot = copy(lostLoot);
    }

    @Override
    public ItemStack[] lostLoot() {
        return copy(lostLoot);
    }

    private static ItemStack[] copy(ItemStack[] contents) {
        return contents == null ? null : Arrays.stream(contents)
                .map(item -> item == null ? null : item.clone())
                .toArray(ItemStack[]::new);
    }
}
