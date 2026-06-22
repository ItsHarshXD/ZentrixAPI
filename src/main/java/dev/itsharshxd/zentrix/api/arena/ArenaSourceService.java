package dev.itsharshxd.zentrix.api.arena;

import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/** Registers source arenas installed by an external addon without a full Zentrix reload. */
public interface ArenaSourceService {

    @NotNull CompletableFuture<ArenaSourceResult> registerSource(@NotNull String sourceArenaName);

    @NotNull CompletableFuture<ArenaSourceResult> unregisterSource(@NotNull String sourceArenaName);

    @NotNull CompletableFuture<Boolean> isSourceRegistered(@NotNull String sourceArenaName);

    /** Returns whether a runtime copy is currently being created from this source. */
    @NotNull default CompletableFuture<Boolean> isSourceBusy(@NotNull String sourceArenaName) {
        return CompletableFuture.completedFuture(false);
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
