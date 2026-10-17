package dev.itsharshxd.zentrix.api.profile;

/**
 * One mode's share of a profile: solo or team matches.
 *
 * <p>Modes are counted from the first match Zentrix recorded with modes, so the lifetime totals in
 * {@link PlayerStats} can include earlier matches neither mode holds.
 *
 * @param matchesPlayed matches played in this mode
 * @param wins          matches won in this mode
 * @param kills         kills in this mode
 * @param deaths        deaths in this mode
 * @since 1.8.0
 */
public record ModeStats(int matchesPlayed, int wins, int kills, int deaths) {

    /** No matches in this mode. */
    public static final ModeStats EMPTY = new ModeStats(0, 0, 0, 0);

    /** Matches played but not won. */
    public int losses() {
        return Math.max(0, matchesPlayed - wins);
    }

    /** Win rate as a percentage with two decimals, or 0 without matches. */
    public double winRate() {
        return matchesPlayed <= 0 ? 0 : Math.round((double) wins / matchesPlayed * 10000.0) / 100.0;
    }

    /** Kills per death with two decimals; the kill count when there are no deaths. */
    public double kdRatio() {
        return deaths == 0 ? kills : Math.round((double) kills / deaths * 100.0) / 100.0;
    }
}
