package dev.itsharshxd.zentrix.api.game;

/** Outcome of a command or API force-start request. */
public enum ForceStartResult {
    STARTED,
    GAME_NOT_FOUND,
    NO_PLAYERS,
    NOT_ENOUGH_TEAMS,
    TOO_MANY_PLAYERS,
    MISSING_GAME_TYPE,
    ALREADY_PLAYING,
    CANNOT_START_STATE,
    CANCELLED,
    UNSUPPORTED
}
