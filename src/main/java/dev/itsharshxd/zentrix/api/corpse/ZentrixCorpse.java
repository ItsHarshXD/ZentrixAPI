package dev.itsharshxd.zentrix.api.corpse;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/** Read-only view of a tracked, persistent corpse. */
public interface ZentrixCorpse {
    @NotNull UUID getPlayerId();
    @NotNull String getPlayerName();
    @NotNull UUID getEntityId();
    @NotNull ZentrixGame getGame();
    @NotNull Location getSpawnLocation();
    @NotNull Optional<Location> getCurrentLocation();
    @NotNull Optional<Entity> getEntity();
    long getExpiryTimeMillis();
    long getSecondsUntilExpiry();
    boolean isExpired();
    @NotNull ItemStack[] getStoredContents();
    @NotNull ItemStack[] getVisualArmor();
    @NotNull Optional<ItemStack> getVisualMainHand();
    @NotNull Optional<ItemStack> getVisualOffHand();
    boolean isLootDropped();
    boolean isWarningSent();
}
