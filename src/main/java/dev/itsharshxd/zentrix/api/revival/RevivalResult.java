package dev.itsharshxd.zentrix.api.revival;

/** Outcome of an attempted teammate revival. */
public enum RevivalResult {
    SUCCESS,
    DISABLED,
    INVALID_GAME,
    INVALID_REVIVER,
    TARGET_NOT_ELIGIBLE,
    NO_SAFE_LOCATION,
    CANCELLED,
    FAILED
}
