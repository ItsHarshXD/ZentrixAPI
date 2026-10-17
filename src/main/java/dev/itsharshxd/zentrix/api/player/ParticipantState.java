package dev.itsharshxd.zentrix.api.player;

/**
 * Where a player stands in a match, told apart more finely than {@link ZentrixPlayer#isAlive()}
 * and {@link ZentrixPlayer#isSpectating()} can.
 *
 * @since 1.8.0
 */
public enum ParticipantState {
    /** In a match that has not started yet. */
    WAITING,
    /** Playing a running match. */
    ALIVE,
    /** Was playing this match, was eliminated, and is watching it now. */
    ELIMINATED,
    /** Watching a match without having played in it. */
    SPECTATING,
    /** Left a running match and can still return to it through their corpse. */
    DISCONNECTED_RETURNABLE,
    /** Not part of any match. */
    NOT_PARTICIPATING
}
