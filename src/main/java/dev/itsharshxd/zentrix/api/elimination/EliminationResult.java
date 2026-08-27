package dev.itsharshxd.zentrix.api.elimination;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What an elimination request actually did.
 *
 * <p>An elimination is best-effort per player: a player who already left, was already a spectator or
 * was never in the match is skipped rather than failing the whole batch, so the counts below are the
 * authoritative record of what happened.
 *
 * @param eliminatedPlayers the players removed from the match, in the order they were removed
 * @param eliminatedTeams   the teams that lost their last member as a result
 * @param skipped           the players that were asked for but were not eliminable
 * @param reason            why nothing happened, when nothing did
 * @since 1.6.0
 */
public record EliminationResult(
        @NotNull List<UUID> eliminatedPlayers,
        @NotNull List<String> eliminatedTeams,
        @NotNull List<UUID> skipped,
        @Nullable String reason) {

    public EliminationResult {
        eliminatedPlayers = eliminatedPlayers == null ? List.of() : List.copyOf(eliminatedPlayers);
        eliminatedTeams = eliminatedTeams == null ? List.of() : List.copyOf(eliminatedTeams);
        skipped = skipped == null ? List.of() : List.copyOf(skipped);
    }

    /** Nothing was eliminated, and this is why. */
    @NotNull
    public static EliminationResult failed(@NotNull String reason) {
        return new EliminationResult(List.of(), List.of(), List.of(), reason);
    }

    @NotNull
    public static EliminationResult of(
            @NotNull Collection<UUID> players,
            @NotNull Collection<String> teams,
            @NotNull Collection<UUID> skipped) {
        return new EliminationResult(
                List.copyOf(players), List.copyOf(teams), List.copyOf(skipped), null);
    }

    /** Whether at least one player was eliminated. */
    public boolean isSuccess() {
        return !eliminatedPlayers.isEmpty();
    }

    public int playerCount() {
        return eliminatedPlayers.size();
    }

    public int teamCount() {
        return eliminatedTeams.size();
    }
}
