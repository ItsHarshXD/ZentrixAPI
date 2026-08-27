package dev.itsharshxd.zentrix.api.arena;

/** Why Zentrix is asking a provider for a source arena. */
public enum ArenaSourcePurpose {

    /** A player is publishing a custom hosted game and needs a source of its own. */
    CUSTOM_GAME,

    /**
     * Players queued through matchmaking and no game they can join exists yet.
     *
     * <p>Asked only after Zentrix has looked at every waiting game, every game still being created,
     * and every source arena this server configured itself. A provider that has nothing ready may
     * return a future it completes later — Zentrix keeps the matchmaking title up while it waits —
     * but should complete it empty rather than leave it hanging once waiting stops being useful.
     *
     * @since 1.6.0
     */
    MATCHMAKING
}
