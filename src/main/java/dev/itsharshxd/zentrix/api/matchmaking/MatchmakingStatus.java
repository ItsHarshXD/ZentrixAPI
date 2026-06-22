package dev.itsharshxd.zentrix.api.matchmaking;

/** Terminal status of an atomic matchmaking request. */
public enum MatchmakingStatus {
    SUCCESS,
    PARTIAL_SUCCESS,
    SOURCE_REQUIRED,
    INVALID_GAME_TYPE,
    INVALID_SOURCE,
    CAPACITY_FAILURE,
    ALREADY_IN_GAME,
    JOIN_ALREADY_IN_PROGRESS,
    DEATHMATCH_MISCONFIGURED,
    CANCELLED_EVENT,
    CREATION_FAILURE,
    NO_ONLINE_TARGETS,
    UNSUPPORTED,
    INTERNAL_ERROR
}
