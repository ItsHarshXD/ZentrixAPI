package dev.itsharshxd.zentrix.api.customgame;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A player-hosted custom game, read at one moment. A snapshot: it does not follow later changes.
 *
 * <p>Everything the host decided is frozen at publication. A password-protected game reports only
 * that it is protected; the password itself is never exposed.
 *
 * @param id                the custom game's stable ID
 * @param hostId            the host
 * @param hostName          the host's name when they published it
 * @param gameTypeName      the game type it is played as
 * @param sourceArenaName   the arena its worlds are built from
 * @param addonSource       whether an addon's arena source supplied that arena
 * @param scenarioIds       the scenarios it runs, in order
 * @param state             where it is in its life
 * @param scheduledAt       when its match starts, in epoch milliseconds
 * @param publishedAt       when it was published, in epoch milliseconds
 * @param passwordProtected whether players need a password to join
 * @param joinable          whether players may join right now
 * @param playerCount       how many players are in its lobby or match now
 * @param remainingCapacity how many more players fit
 * @param runtimeId         the runtime ID of its lobby or match while one exists, or null
 * @since 1.8.0
 */
public record CustomGameInfo(
        @NotNull UUID id,
        @NotNull UUID hostId,
        @NotNull String hostName,
        @NotNull String gameTypeName,
        @NotNull String sourceArenaName,
        boolean addonSource,
        @NotNull List<String> scenarioIds,
        @NotNull CustomGameState state,
        long scheduledAt,
        long publishedAt,
        boolean passwordProtected,
        boolean joinable,
        int playerCount,
        int remainingCapacity,
        @Nullable String runtimeId) {

    public CustomGameInfo {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(hostId, "hostId");
        Objects.requireNonNull(hostName, "hostName");
        Objects.requireNonNull(gameTypeName, "gameTypeName");
        Objects.requireNonNull(sourceArenaName, "sourceArenaName");
        scenarioIds = List.copyOf(scenarioIds);
        Objects.requireNonNull(state, "state");
    }

    /** The runtime ID of its lobby or match, while one exists. */
    @NotNull
    public Optional<String> getRuntimeId() {
        return Optional.ofNullable(runtimeId);
    }

    /** Milliseconds until its match starts as of this snapshot, never negative. */
    public long millisUntilStart() {
        return Math.max(0L, scheduledAt - System.currentTimeMillis());
    }
}
