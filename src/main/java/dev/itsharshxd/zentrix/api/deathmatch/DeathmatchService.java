package dev.itsharshxd.zentrix.api.deathmatch;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Deathmatch state and nonblocking startup. {@link #start} may be called from
 * any thread; Bukkit lifecycle events are dispatched synchronously.
 *
 * @since 1.3.0
 */
public interface DeathmatchService {
    boolean isPreparing(@NotNull ZentrixGame game);
    boolean isActive(@NotNull ZentrixGame game);
    @NotNull Optional<World> getWorld(@NotNull ZentrixGame game);
    @NotNull Optional<String> getWorldName(@NotNull ZentrixGame game);
    @NotNull CompletableFuture<DeathmatchStartResult> start(@NotNull ZentrixGame game);

    /**
     * Where a match is with its deathmatch, including a deathmatch No Deathmatch suppressed.
     *
     * @since 1.8.0
     */
    @NotNull
    default DeathmatchState getState(@NotNull ZentrixGame game) {
        if (isActive(game)) return DeathmatchState.ACTIVE;
        return isPreparing(game) ? DeathmatchState.PREPARING : DeathmatchState.NOT_STARTED;
    }

    /**
     * The deathmatch configuration of {@code settings.yml}.
     *
     * @since 1.8.0
     */
    @NotNull
    default DeathmatchSettingsSnapshot getSettings() {
        throw new UnsupportedOperationException("getSettings requires Zentrix API 1.8.0");
    }

    /**
     * Whether a player is held in place by the freeze that follows arriving in the deathmatch.
     *
     * @since 1.8.0
     */
    default boolean isFrozen(@NotNull org.bukkit.entity.Player player) {
        throw new UnsupportedOperationException("isFrozen requires Zentrix API 1.8.0");
    }

    /**
     * Releases a player from the deathmatch arrival freeze early. Main thread only.
     *
     * @return whether the player had been frozen
     * @since 1.8.0
     */
    default boolean unfreeze(@NotNull org.bukkit.entity.Player player) {
        throw new UnsupportedOperationException("unfreeze requires Zentrix API 1.8.0");
    }
}
