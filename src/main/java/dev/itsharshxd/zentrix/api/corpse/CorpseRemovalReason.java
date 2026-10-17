package dev.itsharshxd.zentrix.api.corpse;

/**
 * Terminal reason a corpse left tracking.
 *
 * <p>New constants may be added in minor releases; handle unknown values with a default branch.
 */
public enum CorpseRemovalReason {
    KILLED,
    EXPIRED,
    GAME_CLEANUP,
    WORLD_CLEANUP,
    API,
    INVALID,
    /**
     * Its owner rejoined the match through it and took its contents back. Reported as
     * {@link #API} before 1.8.0.
     *
     * @since 1.8.0
     */
    REJOINED
}
