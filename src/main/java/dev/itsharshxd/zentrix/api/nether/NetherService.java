package dev.itsharshxd.zentrix.api.nether;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Per-game Nether status and access control. Status queries read Bukkit world
 * state and should run on the main thread. {@link #setAccess} is safe to call
 * from any thread and completes after the request is applied or queued.
 *
 * @since 1.3.0
 */
public interface NetherService {
    @NotNull NetherStatus getStatus(@NotNull ZentrixGame game);
    @NotNull Optional<World> getWorld(@NotNull ZentrixGame game);
    @NotNull Optional<String> getWorldName(@NotNull ZentrixGame game);
    default boolean isConfigured(@NotNull ZentrixGame game) { return getStatus(game).configured(); }
    default boolean isPreparing(@NotNull ZentrixGame game) { return getStatus(game).isPreparing(); }
    default boolean isPrepared(@NotNull ZentrixGame game) { return getStatus(game).isPrepared(); }
    default boolean isAccessEnabled(@NotNull ZentrixGame game) { return getStatus(game).accessEnabled(); }
    @NotNull CompletableFuture<NetherToggleResult> setAccess(
        @NotNull ZentrixGame game, @NotNull NetherToggleRequest request);
}
