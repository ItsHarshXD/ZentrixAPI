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
}
