package dev.itsharshxd.zentrix.api.profile;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One finished match in a player's recent match history, as Zentrix stores it.
 *
 * <p>A profile keeps its most recent matches, newest first. A match's placement can be decided
 * after the player's own part in it ended - a team still fighting, or a player who may rejoin -
 * so an entry read early may show no placement yet and a settled one later.
 *
 * @param matchId     identifies the match; stable across reads of the same history
 * @param endedAt     when the player's match ended, in epoch milliseconds
 * @param teamMode    whether the match was played in teams rather than solo
 * @param arena       the arena name, or {@code null} when unknown
 * @param gameType    the game type name, or {@code null} when unknown
 * @param outcome     how the match ended for the player
 * @param placement   solo or team placement, or {@code 0} while it is unknown
 * @param kills       kills credited in the match
 * @param assists     assists credited in the match
 * @param damageDealt damage dealt in the match
 * @param survivalSeconds seconds survived in the match, or {@code -1} when unknown
 * @since 1.8.0
 */
public record RecentMatch(
        @Nullable String matchId,
        long endedAt,
        boolean teamMode,
        @Nullable String arena,
        @Nullable String gameType,
        @NotNull MatchOutcome outcome,
        int placement,
        int kills,
        int assists,
        double damageDealt,
        long survivalSeconds) {

    public RecentMatch {
        Objects.requireNonNull(outcome, "outcome");
    }

    /** The placement, or empty while it is not decided. */
    @NotNull
    public OptionalInt placementIfKnown() {
        return placement > 0 ? OptionalInt.of(placement) : OptionalInt.empty();
    }

    /** Seconds survived, or empty when not recorded. */
    @NotNull
    public OptionalLong survivalIfKnown() {
        return survivalSeconds >= 0 ? OptionalLong.of(survivalSeconds) : OptionalLong.empty();
    }

    /** The arena, when recorded. */
    @NotNull
    public Optional<String> arenaIfKnown() {
        return Optional.ofNullable(arena);
    }

    /** The game type, when recorded. */
    @NotNull
    public Optional<String> gameTypeIfKnown() {
        return Optional.ofNullable(gameType);
    }

    /** How a match ended for one player. */
    public enum MatchOutcome {
        /** The player's side won, including a squad member eliminated before the end. */
        WIN,
        /** The player was eliminated and their side did not win. */
        LOSS,
        /** The player left the match before it ended. */
        LEFT
    }
}
