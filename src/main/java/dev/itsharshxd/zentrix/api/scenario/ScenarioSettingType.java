package dev.itsharshxd.zentrix.api.scenario;

/**
 * The value kinds a scenario setting can declare.
 *
 * <p>The type decides how a value is validated, how the management GUI edits it, and how it is
 * written back to {@code scenarios.yml}.
 *
 * @since 1.6.0
 */
public enum ScenarioSettingType {

    /** A toggle, edited by clicking. */
    BOOLEAN,

    /** A whole number bounded by the setting's minimum and maximum. */
    INTEGER,

    /** A fractional number bounded by the setting's minimum and maximum. */
    DECIMAL,

    /** Free text, captured from chat. */
    STRING,

    /** One of the setting's allowed values, cycled by clicking. */
    ENUM,

    /** A list of free-text values, edited through chat. */
    STRING_LIST,

    /** A single Bukkit {@code Material} name. */
    MATERIAL,

    /** A list of Bukkit {@code Material} names. */
    MATERIAL_LIST,

    /**
     * A list of entries whose fields the scenario declares itself, through a
     * {@link ScenarioEntrySchema}.
     *
     * <p>One field makes a plain list, two make a list of pairs, more make whatever the scenario
     * needs. Zentrix validates every field against its declaration, edits entries in a dedicated
     * management screen, writes them to {@code scenarios.yml} as a list of maps, and freezes them
     * into a match's settings snapshot like any other value.
     *
     * @since 1.6.0
     */
    STRUCTURED_LIST
}
