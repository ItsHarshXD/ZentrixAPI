package dev.itsharshxd.zentrix.api.cosmetics;

/**
 * A moment of a match at which Zentrix triggers an addon's cosmetic type, through
 * {@link CosmeticHandler#onTrigger}. A type only hears the moments its
 * {@link CosmeticTypeDefinition.Builder#triggers definition} lists. Spectators trigger nothing but
 * their own {@link #DEATH} and {@link #GAME_LEAVE}.
 *
 * @since 1.11.0
 */
public enum CosmeticMoment {
    /** The player joined a match's waiting lobby, or rejoined a running match. */
    GAME_JOIN,
    /** The match started with the player in it. */
    GAME_START,
    /** The player opened their glider, from the dragon bus or a sky drop. */
    GLIDE,
    /** The player killed another player; {@link CosmeticTrigger#other()} is the victim. */
    KILL,
    /**
     * The player died in the match; {@link CosmeticTrigger#other()} is the killer, when there is
     * one, and {@link CosmeticTrigger#location()} where they died.
     */
    DEATH,
    /** The player's side won the match. */
    VICTORY,
    /**
     * The match is over for the player: they left it, or it ended. Heard once by every player who
     * joined or started the match while online, eliminated ones included, so anything kept up since
     * {@link #GAME_JOIN} or {@link #GAME_START} can be undone. Players who left the server may miss
     * it; {@link CosmeticHandler#onUnregister()} and the addon's own quit handling cover them.
     */
    GAME_LEAVE
}
