package dev.itsharshxd.zentrix.api.customgame;

/**
 * Where a player-hosted custom game is in its life.
 *
 * <p>Only {@link #SCHEDULED} and {@link #WAITING} games survive a restart: a match that already
 * started cannot be picked up again, and a finished one has nothing left to restore.
 *
 * @since 1.8.0
 */
public enum CustomGameState {
    /** Published, with no lobby of its own right now: its worlds are still being built. */
    SCHEDULED,
    /** Published, its lobby is standing, and players may walk in until its start time. */
    WAITING,
    /** Its match is running. */
    ACTIVE,
    /** Its match finished. */
    COMPLETED,
    /** Called off by its host, an administrator, or because it could not start. */
    CANCELLED;

    /** Whether the game is still waiting to start. */
    public boolean isUpcoming() {
        return this == SCHEDULED || this == WAITING;
    }

    /** Whether the game is over, one way or another. */
    public boolean isFinished() {
        return this == COMPLETED || this == CANCELLED;
    }
}
