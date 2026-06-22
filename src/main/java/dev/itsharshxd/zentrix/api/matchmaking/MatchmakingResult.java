package dev.itsharshxd.zentrix.api.matchmaking;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Immutable result returned after all admission or creation work has completed. */
public final class MatchmakingResult {
    private final MatchmakingStatus status;
    private final List<UUID> acceptedPlayerIds;
    private final List<UUID> rejectedPlayerIds;
    private final ZentrixGame runtimeGame;
    private final String sourceArenaName;
    private final SourceDisposition sourceDisposition;
    private final String detail;

    public MatchmakingResult(
            @NotNull MatchmakingStatus status,
            @NotNull Collection<UUID> acceptedPlayerIds,
            @NotNull Collection<UUID> rejectedPlayerIds,
            @Nullable ZentrixGame runtimeGame,
            @Nullable String sourceArenaName,
            @NotNull SourceDisposition sourceDisposition,
            @Nullable String detail) {
        this.status = status;
        this.acceptedPlayerIds = List.copyOf(acceptedPlayerIds);
        this.rejectedPlayerIds = List.copyOf(rejectedPlayerIds);
        this.runtimeGame = runtimeGame;
        this.sourceArenaName = sourceArenaName;
        this.sourceDisposition = sourceDisposition;
        this.detail = detail == null ? "" : detail;
    }

    @NotNull public MatchmakingStatus getStatus() { return status; }
    @NotNull public List<UUID> getAcceptedPlayerIds() { return acceptedPlayerIds; }
    @NotNull public List<UUID> getRejectedPlayerIds() { return rejectedPlayerIds; }
    @NotNull public Optional<ZentrixGame> getRuntimeGame() { return Optional.ofNullable(runtimeGame); }
    @NotNull public Optional<String> getSourceArenaName() { return Optional.ofNullable(sourceArenaName); }
    @NotNull public SourceDisposition getSourceDisposition() { return sourceDisposition; }
    @NotNull public String getDetail() { return detail; }
    public boolean isSuccess() { return status == MatchmakingStatus.SUCCESS || status == MatchmakingStatus.PARTIAL_SUCCESS; }
}
