package dev.itsharshxd.zentrix.api.world;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Map;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Read-only runtime-world ownership and shared-scope resolution.
 * Methods accepting Bukkit {@link Player} or {@link World} objects should be
 * called on the server main thread. Returned maps are immutable snapshots.
 *
 * @since 1.3.0
 */
public interface RuntimeWorldService {
    @NotNull Optional<ZentrixGame> resolveGame(@NotNull Player player);
    @NotNull Optional<ZentrixGame> resolveGame(@NotNull World world);
    @NotNull Optional<ZentrixGame> resolveGame(@NotNull String worldName);
    @NotNull Optional<GameWorldType> getWorldType(@NotNull ZentrixGame game, @NotNull World world);
    @NotNull Optional<GameWorldType> getWorldType(@NotNull ZentrixGame game, @NotNull String worldName);
    @NotNull Optional<World> getWorld(@NotNull ZentrixGame game, @NotNull GameWorldType type);
    @NotNull Optional<String> getWorldName(@NotNull ZentrixGame game, @NotNull GameWorldType type);
    @NotNull Map<GameWorldType, String> getKnownWorldNames(@NotNull ZentrixGame game);
    boolean belongsToGame(@NotNull ZentrixGame game, @NotNull World world);
    boolean belongsToGame(@NotNull ZentrixGame game, @NotNull String worldName);
    @NotNull String getRuntimeScopeKey(@NotNull Player player);
    @NotNull String getRuntimeScopeKey(@NotNull String worldName);
}
