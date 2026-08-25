package dev.itsharshxd.zentrix.api.chat;

/**
 * Chat channels available to players.
 * <p>
 * Players can switch channels to communicate with different groups:
 * <ul>
 *   <li>{@link #GLOBAL} - All players can see messages</li>
 *   <li>{@link #TEAM} - Only teammates in the same game can see messages</li>
 *   <li>{@link #PARTY} - Only party members can see messages</li>
 * </ul>
 * </p>
 *
 * @author ItsHarshXD
 * @since 1.2.0
 */
public enum ChatChannel {

    /**
     * Global chat channel. Messages are visible to all players.
     */
    GLOBAL("global"),

    /**
     * Team chat channel. Messages are visible only to teammates in the same game.
     */
    TEAM("team"),

    /**
     * Party chat channel. Messages are visible only to party members.
     */
    PARTY("party");

    private final String id;

    ChatChannel(String id) {
        this.id = id;
    }

    /**
     * Gets the string identifier for this channel.
     *
     * @return The channel ID (e.g., "global", "team", "party")
     */
    public String getId() {
        return id;
    }

    /**
     * Gets a ChatChannel by its ID (case-insensitive).
     *
     * @param id The channel ID
     * @return The ChatChannel, or {@link #GLOBAL} if not found or null
     */
    public static ChatChannel fromId(String id) {
        if (id == null) return GLOBAL;
        for (ChatChannel channel : values()) {
            if (channel.id.equalsIgnoreCase(id)) {
                return channel;
            }
        }
        return GLOBAL;
    }
}
