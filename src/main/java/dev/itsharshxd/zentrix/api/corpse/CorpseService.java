package dev.itsharshxd.zentrix.api.corpse;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** Corpse spawning, persistent inventory, lookup, drop, death, and removal operations. @since 1.6.0 */
public interface CorpseService {

    @NotNull CorpseSettingsSnapshot getSettings();
    @NotNull Optional<CorpseDropPolicy> getDropPolicyOverride();
    void setDropPolicyOverride(@NotNull CorpseDropPolicy policy);
    void clearDropPolicyOverride();

    @NotNull CompletableFuture<Optional<ZentrixCorpse>> spawnIfEligible(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull CorpseLeaveReason reason);

    @NotNull CompletableFuture<ZentrixCorpse> spawn(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull CorpseSpawnRequest request);

    @NotNull Optional<ZentrixCorpse> getByEntityId(@NotNull UUID entityId);
    @NotNull Optional<ZentrixCorpse> get(@NotNull Entity entity);
    @NotNull List<ZentrixCorpse> getCorpses(@NotNull ZentrixGame game);
    boolean isCorpse(@NotNull Entity entity);
    int getTotalCorpseCount();

    boolean dropStoredItems(@NotNull ZentrixCorpse corpse);
    boolean dropStoredItems(@NotNull ZentrixCorpse corpse, @NotNull Location location);
    void handleDeath(@NotNull ZentrixCorpse corpse, @NotNull Location location);
    void remove(
            @NotNull ZentrixCorpse corpse,
            @NotNull CorpseRemovalReason reason,
            boolean removeEntity);
}
