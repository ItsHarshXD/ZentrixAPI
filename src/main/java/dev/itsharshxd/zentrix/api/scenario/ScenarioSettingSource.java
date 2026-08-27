package dev.itsharshxd.zentrix.api.scenario;

/**
 * Where a resolved scenario setting value came from.
 *
 * <p>The order here is the resolution order: the first source that supplies a value wins.
 *
 * @since 1.6.0
 */
public enum ScenarioSettingSource {

    /** A temporary override applied to one match, normally from the waiting lobby or an addon. */
    LOBBY_OVERRIDE,

    /** An override stored on the source arena. */
    ARENA_OVERRIDE,

    /** The global value in {@code scenarios.yml}. */
    GLOBAL,

    /** The scenario's own declared default; nothing was configured. */
    DEFAULT
}
