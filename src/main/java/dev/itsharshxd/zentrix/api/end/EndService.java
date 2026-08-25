package dev.itsharshxd.zentrix.api.end;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * Per-game End status and access control. Status queries read Bukkit world
 * state and should run on the main thread. {@link #setAccess} is safe to call
 * from any thread and completes after the request is applied or queued.
 *
 * @since 1.6.0
 */
public interface EndService {
    @NotNull EndStatus getStatus(@NotNull ZentrixGame game);
    @NotNull Optional<World> getWorld(@NotNull ZentrixGame game);
    @NotNull Optional<String> getWorldName(@NotNull ZentrixGame game);
    default boolean isConfigured(@NotNull ZentrixGame game) { return getStatus(game).configured(); }
    default boolean isPreparing(@NotNull ZentrixGame game) { return getStatus(game).isPreparing(); }
    default boolean isPrepared(@NotNull ZentrixGame game) { return getStatus(game).isPrepared(); }
    default boolean isAccessEnabled(@NotNull ZentrixGame game) { return getStatus(game).accessEnabled(); }
    @NotNull CompletableFuture<EndToggleResult> setAccess(
        @NotNull ZentrixGame game, @NotNull EndToggleRequest request);

    /**
     * The Ender Dragon of this match's runtime End, and only that one.
     *
     * <p>Resolved through the End's own dragon fight, so a dragon that merely happens to be flying
     * in the world — a transport dragon, one another plugin spawned, one belonging to a different
     * match — is never returned. That makes this the check an objective built on the dragon needs.
     *
     * <p>Empty when the match has no End, when the copy is not prepared yet, or when the dragon is
     * dead or not currently loaded.
     *
     * @since 1.9.0
     */
    @NotNull Optional<org.bukkit.entity.EnderDragon> getDragon(@NotNull ZentrixGame game);

    /**
     * Whether this match's runtime End is sealed.
     *
     * @since 1.9.0
     */
    boolean isLockedDown(@NotNull ZentrixGame game);

    /**
     * Seals or unseals this match's runtime End.
     *
     * <p>A sealed End refuses new arrivals the way closed access does, and additionally keeps the
     * players already inside from leaving: the exit portal puts them back on the island instead of
     * returning them to the arena, and a portal out is refused. Gateways keep working, because they
     * never leave the End in the first place.
     *
     * <p>This is what a scenario that moves the final fight into the End needs, and it is undone
     * automatically when the match is cleaned up.
     *
     * @return true when the match has a runtime End the flag could be applied to
     * @since 1.9.0
     */
    boolean setLockedDown(@NotNull ZentrixGame game, boolean locked);
}
