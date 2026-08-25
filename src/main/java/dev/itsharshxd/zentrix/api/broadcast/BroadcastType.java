package dev.itsharshxd.zentrix.api.broadcast;

/**
 * Broadcast output format.
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public enum BroadcastType {
    /**
     * A chat message broadcast sent to all players.
     */
    CHAT,

    /**
     * A title (with optional subtitle) broadcast displayed on screen.
     */
    TITLE,

    /**
     * An action bar message displayed above the hotbar.
     */
    ACTIONBAR
}
