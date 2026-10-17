package dev.itsharshxd.zentrix.api.profile;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A player's lifetime statistics in Zentrix.
 * <p>
 * The values cover every game the player has participated in.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * PlayerStats stats = ZentrixProvider.get()
 *     .getProfileService()
 *     .getCachedStats(player.getUniqueId());
 *
 * int wins = stats.getWins();
 * int kills = stats.getKills();
 * double kd = stats.getKDRatio();
 * double winRate = stats.getWinRate();
 *
 * player.sendMessage("Stats - Wins: " + wins + ", K/D: " + kd);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface PlayerStats {

    // ==========================================
    // Core statistics
    // ==========================================

    /**
     * Gets the total number of games won.
     *
     * @return Total wins
     */
    int getWins();

    /**
     * Gets the total number of matches played.
     *
     * @return Total matches played
     */
    int getMatchesPlayed();

    /**
     * Gets the total number of kills across all games.
     *
     * @return Total kills
     */
    int getKills();

    /**
     * Gets the total number of deaths across all games.
     *
     * @return Total deaths
     */
    int getDeaths();

    // ==========================================
    // Damage statistics
    // ==========================================

    /**
     * Gets the total damage dealt across all games.
     *
     * @return Total damage dealt
     */
    double getDamageDealt();

    /**
     * Gets the total damage taken across all games.
     *
     * @return Total damage taken
     */
    double getDamageTaken();

    // ==========================================
    // Record statistics
    // ==========================================

    /**
     * Gets the highest number of kills achieved in a single game.
     *
     * @return Highest kills in one game
     */
    int getHighestKillGame();

    /**
     * Gets the highest kill streak achieved in a single game.
     *
     * @return Highest kill streak
     */
    int getHighestKillStreak();

    /**
     * Gets the total survival time across all games, in seconds.
     *
     * @return Total survival time in seconds
     */
    double getTotalSurvivalTime();

    /**
     * Gets the longest survival time in a single game, in seconds.
     *
     * @return Longest survival time in seconds
     */
    double getLongestSurvivalTime();

    // ==========================================
    // Win streak statistics
    // ==========================================

    /**
     * Gets the current consecutive win streak.
     * <p>
     * This resets to 0 when the player loses a game.
     * </p>
     *
     * @return Current win streak
     */
    int getCurrentWinStreak();

    /**
     * Gets the highest consecutive win streak ever achieved.
     *
     * @return Highest win streak
     */
    int getHighestWinStreak();

    // ==========================================
    // Calculated statistics
    // ==========================================

    /**
     * Calculates the kill/death ratio.
     * <p>
     * If deaths is 0, returns the kill count as the ratio.
     * </p>
     *
     * @return K/D ratio rounded to 2 decimal places
     */
    double getKDRatio();

    /**
     * Calculates the win rate as a percentage.
     * <p>
     * Returns 0 if no matches have been played.
     * </p>
     *
     * @return Win rate percentage (0-100)
     */
    double getWinRate();

    /**
     * Calculates the average kills per game.
     * <p>
     * Returns 0 if no matches have been played.
     * </p>
     *
     * @return Average kills per game
     */
    double getAverageKills();

    /**
     * Calculates the average survival time per game, in seconds.
     * <p>
     * Returns 0 if no matches have been played.
     * </p>
     *
     * @return Average survival time in seconds
     */
    double getAverageSurvivalTime();

    /**
     * Gets the number of losses (matches played minus wins).
     *
     * @return Total losses
     */
    default int getLosses() {
        return Math.max(0, getMatchesPlayed() - getWins());
    }

    /**
     * Checks if the player has any recorded statistics.
     * <p>
     * Returns true if the player has played at least one match.
     * </p>
     *
     * @return {@code true} if the player has played any games
     */
    default boolean hasPlayed() {
        return getMatchesPlayed() > 0;
    }

    /**
     * Gets the damage ratio (dealt / taken).
     * <p>
     * Returns damage dealt if no damage has been taken.
     * </p>
     *
     * @return Damage ratio
     */
    // ==========================================
    // Placement, assist, revival and ranged statistics (since 1.8.0)
    // ==========================================

    /**
     * Matches finished in the top three.
     *
     * @return Top-three finishes
     * @since 1.8.0
     */
    default int getTopThreeFinishes() {
        return 0;
    }

    /**
     * Matches whose placement was recorded. Matches played before Zentrix tracked placements are
     * not counted, so rates based on placements are not diluted by them.
     *
     * @return Matches with a recorded placement
     * @since 1.8.0
     */
    default int getPlacedMatches() {
        return 0;
    }

    /**
     * Share of placed matches finished in the top three, as a percentage with two decimals.
     *
     * @return Top-three rate, or 0 without a placed match
     * @since 1.8.0
     */
    default double getTopThreeRate() {
        int placed = getPlacedMatches();
        return placed <= 0 ? 0 : Math.round((double) getTopThreeFinishes() / placed * 10000.0) / 100.0;
    }

    /**
     * Lifetime assists.
     *
     * @return Assists
     * @since 1.8.0
     */
    default int getAssists() {
        return 0;
    }

    /**
     * Assists per match played, with two decimals.
     *
     * @return Average assists, or 0 without matches
     * @since 1.8.0
     */
    default double getAverageAssists() {
        int matches = getMatchesPlayed();
        return matches <= 0 ? 0 : Math.round((double) getAssists() / matches * 100.0) / 100.0;
    }

    /**
     * Teammates this player revived.
     *
     * @return Teammates revived
     * @since 1.8.0
     */
    default int getTeammatesRevived() {
        return 0;
    }

    /**
     * Kills whose killing blow was a projectile.
     *
     * @return Ranged kills
     * @since 1.8.0
     */
    default int getRangedKills() {
        return 0;
    }

    /**
     * Longest ranged kill, in blocks from where the shot was fired.
     *
     * @return The distance, or 0 without a ranged kill
     * @since 1.8.0
     */
    default double getLongestRangedKill() {
        return 0;
    }

    /**
     * Share of kills that were ranged, as a percentage with two decimals.
     *
     * @return Ranged kill share, or 0 without kills
     * @since 1.8.0
     */
    default double getRangedKillShare() {
        int kills = getKills();
        return kills <= 0 ? 0
                : Math.round((double) Math.min(getRangedKills(), kills) / kills * 10000.0) / 100.0;
    }

    /**
     * Most damage dealt in a single match.
     *
     * @return Highest damage in one match
     * @since 1.8.0
     */
    default double getHighestDamageGame() {
        return 0;
    }

    /**
     * Damage taken per match played, with two decimals.
     *
     * @return Average damage taken, or 0 without matches
     * @since 1.8.0
     */
    default double getAverageDamageTaken() {
        int matches = getMatchesPlayed();
        return matches <= 0 ? 0 : Math.round(getDamageTaken() / matches * 100.0) / 100.0;
    }

    // ==========================================
    // Modes and history (since 1.8.0)
    // ==========================================

    /**
     * Solo matches only.
     *
     * @return The solo share of this profile
     * @since 1.8.0
     */
    @NotNull
    default ModeStats getSoloStats() {
        return ModeStats.EMPTY;
    }

    /**
     * Team matches only.
     *
     * @return The team share of this profile
     * @since 1.8.0
     */
    @NotNull
    default ModeStats getTeamStats() {
        return ModeStats.EMPTY;
    }

    /**
     * The player's most recent matches, newest first, as many as Zentrix keeps.
     *
     * @return An unmodifiable list (never null, may be empty)
     * @since 1.8.0
     */
    @NotNull
    default List<RecentMatch> getRecentMatches() {
        return List.of();
    }

    default double getDamageRatio() {
        double taken = getDamageTaken();
        if (taken == 0) {
            return getDamageDealt();
        }
        return Math.round((getDamageDealt() / taken) * 100.0) / 100.0;
    }
}
