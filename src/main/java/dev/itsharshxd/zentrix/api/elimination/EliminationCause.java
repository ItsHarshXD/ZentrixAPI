package dev.itsharshxd.zentrix.api.elimination;

/**
 * Why a player was eliminated by something other than an ordinary death.
 *
 * <p>Zentrix uses this only for diagnostics and for the leave reason reported to addons; it never
 * changes what the elimination does, which {@link EliminationOptions} decides on its own.
 *
 * @since 1.6.0
 */
public enum EliminationCause {

    /** A scenario's own rules ended this player's match — an objective, a countdown, a wager. */
    SCENARIO,

    /** An administrator or a command removed the player from the match. */
    ADMINISTRATIVE,

    /** An addon eliminated the player for a reason of its own. */
    PLUGIN
}
