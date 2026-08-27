package dev.itsharshxd.zentrix.api.gui;

/** Outcome of resolving and opening a built-in menu. */
public enum MenuOpenResult {
    OPENED,
    UNKNOWN_MENU,
    MISSING_CONTEXT,
    INVALID_CONTEXT,
    UNAVAILABLE,
    FAILED
}
