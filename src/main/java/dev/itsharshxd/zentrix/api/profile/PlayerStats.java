package dev.itsharshxd.zentrix.api.profile;

/**
 * Represents a player's lifetime statistics in Zentrix.
 * <p>
 * This interface provides access to persistent statistics that are
 * accumulated across all games a player has participated in.
 * </p>
 *
 * <h2>Example Usage</h2>
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
    // Core Statistics
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
    // Damage Statistics
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
    // Record Statistics
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
    // Win Streak Statistics
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
    // Calculated Statistics
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
    default double getDamageRatio() {
        double taken = getDamageTaken();
        if (taken == 0) {
            return getDamageDealt();
        }
        return Math.round((getDamageDealt() / taken) * 100.0) / 100.0;
    }
}
