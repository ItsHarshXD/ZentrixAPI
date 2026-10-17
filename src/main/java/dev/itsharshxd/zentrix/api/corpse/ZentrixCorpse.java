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

    /**
     * Why the owner left the match, for a corpse that still holds their return.
     *
     * @return the leave reason, or empty once the corpse holds no return
     * @since 1.8.0
     */
    default @NotNull Optional<CorpseLeaveReason> getLeaveReason() { return Optional.empty(); }

    /**
     * Whether the owner can still return to the match through this corpse. Even then, a rejoin
     * can be refused for reasons outside the corpse; ask
     * {@link dev.itsharshxd.zentrix.api.rejoin.RejoinService#getStatus} for the full answer.
     *
     * @return {@code true} while the corpse holds its owner's return
     * @since 1.8.0
     */
    default boolean isReturnable() { return false; }
}
