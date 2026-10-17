package dev.itsharshxd.zentrix.api.rejoin;

/**
 * Whether a player can return to the match they left through their corpse, and if not, why.
 *
 * @since 1.8.0
 */
public enum RejoinEligibility {
    /** They can rejoin now. */
    ELIGIBLE,
    /** They are already in a match. */
    ALREADY_IN_GAME,
    /** Another join or rejoin of theirs is still being processed. */
    BUSY,
    /** They are spectating. */
    SPECTATING,
    /** They left no corpse that holds a return. */
    NO_CORPSE,
    /** Their corpse was killed while they were away. */
    CORPSE_KILLED,
    /** Their corpse expired. */
    CORPSE_EXPIRED,
    /** Their corpse's loot was already dropped. */
    CORPSE_EMPTY,
    /** Their corpse could not be found where it last stood. */
    CORPSE_MISSING,
    /** The match ended or is being decided. */
    MATCH_ENDED,
    /** Leaving that way - such as with {@code /leave} - forfeits the return, by configuration. */
    LEFT_BY_COMMAND,
    /** They already rejoined this match once and the configuration allows only one return. */
    ALREADY_REJOINED,
    /** Their team no longer exists. */
    TEAM_GONE,
    /** Their team was eliminated and the configuration does not bring it back. */
    TEAM_ELIMINATED,
    /** A {@code PlayerJoinGameEvent} listener refused the return. */
    CANCELLED,
    /** The return failed, for example because the teleport was refused; nothing changed. */
    FAILED,
    /** Rejoining is not available on this server. */
    UNAVAILABLE;

    /** Whether the player can rejoin. */
    public boolean isEligible() {
        return this == ELIGIBLE;
    }
}
