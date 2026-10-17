package dev.itsharshxd.zentrix.api.deathmatch;

/**
 * Where a match is with its deathmatch.
 *
 * @since 1.8.0
 */
public enum DeathmatchState {
    /** No deathmatch was requested yet, or the match is not running. */
    NOT_STARTED,
    /**
     * A deathmatch was requested while No Deathmatch is active, so nothing was copied. It starts
     * normally if that scenario is disabled or isolated during the match.
     */
    SUPPRESSED,
    /** The deathmatch arena is being prepared and players are about to be moved. */
    PREPARING,
    /** The match is in its deathmatch. */
    ACTIVE
}
