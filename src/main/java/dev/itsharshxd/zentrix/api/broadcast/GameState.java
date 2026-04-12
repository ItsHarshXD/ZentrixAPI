package dev.itsharshxd.zentrix.api.broadcast;

/**
 * Enum representing game states for broadcast filtering.
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public enum GameState {
    /**
     * The lobby state before any game is active.
     */
    LOBBY,

    /**
     * The waiting state while players are joining.
     */
    WAITING,

    /**
     * The playing state during an active game.
     */
    PLAYING
}
