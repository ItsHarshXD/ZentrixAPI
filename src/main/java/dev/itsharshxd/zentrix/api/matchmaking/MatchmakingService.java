package dev.itsharshxd.zentrix.api.matchmaking;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/** Atomic group matchmaking across waiting, pending, and newly created runtime games. */
public interface MatchmakingService {

    @NotNull CompletableFuture<MatchmakingResult> matchmake(@NotNull MatchmakingRequest request);

    static MatchmakingService unsupported() {
        return request -> CompletableFuture.completedFuture(new MatchmakingResult(
                MatchmakingStatus.UNSUPPORTED,
                List.of(),
                request.getOnlineTargets().stream().map(player -> player.getUniqueId()).toList(),
                null,
                request.getSourceArenaName().orElse(null),
                SourceDisposition.NONE,
                "MatchmakingService requires Zentrix API 1.4.0"));
    }
}
