package dev.itsharshxd.zentrix.api.team;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One side competing for the win in a match: a team when the match is played in teams, otherwise a
 * single player.
 *
 * <p>Win conditions and placements count sides, not teams. A solo match has no teams at all, so
 * {@link dev.itsharshxd.zentrix.api.game.ZentrixGame#getTeams()} is empty there while its sides are
 * its players. A snapshot: it does not change as the match goes on.
 *
 * @param id           the team id for a team side, or the player's UUID as text for a solo side
 * @param kind         whether this side is a team or a single player
 * @param teamId       the team id for a team side; {@code null} for a solo side
 * @param members      every member the side had when this snapshot was taken, alive or not
 * @param aliveMembers the members still playing
 * @param eliminated   whether the side is out of the match
 * @since 1.8.0
 */
public record CompetingSide(
        @NotNull String id,
        @NotNull Kind kind,
        @Nullable String teamId,
        @NotNull Set<UUID> members,
        @NotNull Set<UUID> aliveMembers,
        boolean eliminated) {

    public CompetingSide {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(kind, "kind");
        members = members == null ? Set.of() : Set.copyOf(members);
        aliveMembers = aliveMembers == null ? Set.of() : Set.copyOf(aliveMembers);
    }

    /** Whether the side is still competing. */
    public boolean isAlive() {
        return !eliminated && !aliveMembers.isEmpty();
    }

    /** The team id, for a team side. */
    @NotNull
    public Optional<String> teamIdIfTeam() {
        return Optional.ofNullable(teamId);
    }

    /** What a side is made of. */
    public enum Kind {
        /** A runtime team. */
        TEAM,
        /** One player in a match played without teams. */
        SOLO
    }
}
