package dev.itsharshxd.zentrix.api.scenario;

/**
 * How a match decides which scenarios it runs.
 *
 * <p>The mode is resolved per match through the normal override chain, so one arena can vote while
 * another runs a fixed set.
 *
 * @since 1.7.0
 */
public enum ScenarioSelectionMode {

    /**
     * An administrator picked the scenarios, globally or per arena. The set is known before the
     * lobby even opens.
     */
    ADMIN_SET,

    /**
     * Players vote in the waiting lobby. The match cannot start until the vote is resolved and the
     * winners are locked in.
     */
    PLAYER_VOTING,

    /**
     * Zentrix draws a configurable number of scenarios at random from the allowed pool when the
     * match is created.
     */
    AUTOMATIC,

    /**
     * The scenario system is switched off for the match. No set is drawn, no vote is opened and no
     * scenario activates, whatever the rest of the configuration says.
     *
     * <p>Resolved through the same override chain as every other mode, so switching the global mode
     * off still leaves an arena free to run scenarios through its own enabled override.
     *
     * @since 1.9.0
     */
    DISABLED
}
