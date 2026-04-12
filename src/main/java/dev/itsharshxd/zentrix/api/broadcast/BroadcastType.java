package dev.itsharshxd.zentrix.api.broadcast;

/**
 * Enum representing the type of broadcast message.
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
