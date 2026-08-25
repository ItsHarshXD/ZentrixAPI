package dev.itsharshxd.zentrix.api.arena;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Registers source arenas installed by an external addon without a full Zentrix reload. */
public interface ArenaSourceService {

    @NotNull CompletableFuture<ArenaSourceResult> registerSource(@NotNull String sourceArenaName);

    /**
     * Registers a source arena whose world folders live under a custom directory in
     * the server root. Implementations that do not support custom source directories
     * fall back to the default root-world lookup.
     */
    @NotNull default CompletableFuture<ArenaSourceResult> registerSource(
            @NotNull String sourceArenaName,
            @NotNull String sourceWorldDirectoryName) {
        return registerSource(sourceArenaName);
    }

    @NotNull CompletableFuture<ArenaSourceResult> unregisterSource(@NotNull String sourceArenaName);

    @NotNull CompletableFuture<Boolean> isSourceRegistered(@NotNull String sourceArenaName);

    /**
     * Returns whether Zentrix still needs this source.
     *
     * <p>True while a runtime copy is being created from it, and while a published custom game
     * holds it for a match that has not started yet. A provider must not reclaim, unregister or
     * delete a busy source.
     */
    @NotNull default CompletableFuture<Boolean> isSourceBusy(@NotNull String sourceArenaName) {
        return CompletableFuture.completedFuture(false);
    }

    /**
     * Installs the provider Zentrix asks when it needs a source arena it cannot pick itself.
     *
     * <p>Replaces any provider registered before, so an addon that registers on enable and
     * unregisters on disable never leaves a stale one behind. Zentrix drops the provider
     * automatically when its owning plugin is disabled.
     *
     * @param owner    the plugin that owns the provider
     * @param provider the provider, or null to remove the one {@code owner} registered
     * @return false when the request was rejected, which older Zentrix versions always do
     * @since 1.11.0
     */
    default boolean registerProvider(
            @NotNull org.bukkit.plugin.Plugin owner, @Nullable ArenaSourceProvider provider) {
        return false;
    }

    /**
     * Whether a source provider is installed, and therefore whether Zentrix may ask for a source
     * instead of picking one this server configured itself.
     *
     * @since 1.11.0
     */
    default boolean hasProvider() {
        return false;
    }

    /**
     * Asks the installed provider for a source arena.
     *
     * <p>Completes with an empty result when no provider is installed or the provider has nothing
     * to give. A provider may complete the future later than it answers; callers that keep a player
     * waiting on it should apply their own deadline.
     *
     * <p>Safe to call from any thread: the provider itself is always asked on the main thread, as
     * {@link ArenaSourceProvider} promises it. The returned future is completed on whichever thread
     * the provider completes its own, so a caller that touches the Bukkit API in a continuation
     * still has to get itself back onto the main thread.
     *
     * @since 1.11.0
     */
    @NotNull default CompletableFuture<java.util.Optional<ArenaSourceLease>> requestSource(
            @NotNull ArenaSourceRequest request) {
        return CompletableFuture.completedFuture(java.util.Optional.empty());
    }

    static ArenaSourceService unsupported() {
        return UnsupportedHolder.INSTANCE;
    }

    final class UnsupportedHolder {
        private static final ArenaSourceService INSTANCE = new ArenaSourceService() {
            @Override
            public CompletableFuture<ArenaSourceResult> registerSource(String sourceArenaName) {
                return result(sourceArenaName);
            }

            @Override
            public CompletableFuture<ArenaSourceResult> unregisterSource(String sourceArenaName) {
                return result(sourceArenaName);
            }

            @Override
            public CompletableFuture<Boolean> isSourceRegistered(String sourceArenaName) {
                return CompletableFuture.completedFuture(false);
            }

            private CompletableFuture<ArenaSourceResult> result(String sourceArenaName) {
                return CompletableFuture.completedFuture(new ArenaSourceResult(
                        ArenaSourceStatus.UNSUPPORTED, sourceArenaName,
                        "ArenaSourceService requires Zentrix API 1.4.0"));
            }
        };

        private UnsupportedHolder() {}
    }
}
