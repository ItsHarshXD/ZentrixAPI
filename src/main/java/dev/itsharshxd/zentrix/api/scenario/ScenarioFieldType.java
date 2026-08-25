package dev.itsharshxd.zentrix.api.scenario;

/**
 * The value kinds one field of a structured entry can hold.
 *
 * <p>A field type decides how a single value inside a
 * {@link ScenarioSettingType#STRUCTURED_LIST} entry is validated, how the management GUI edits it,
 * and how it is written back to {@code scenarios.yml}. It is deliberately the same idea as
 * {@link ScenarioSettingType}, minus the list kinds: an entry field always holds one value, because
 * the entry itself is what the list is made of.
 *
 * @since 1.8.0
 */
public enum ScenarioFieldType {

    /** Free text, captured from chat. */
    STRING,

    /** A whole number bounded by the field's minimum and maximum. */
    INTEGER,

    /** A fractional number bounded by the field's minimum and maximum. */
    DECIMAL,

    /** A toggle, edited by clicking. */
    BOOLEAN,

    /** One of the field's allowed values, cycled by clicking. */
    ENUM,

    /** A single Bukkit {@code Material} name. */
    MATERIAL,

    /**
     * An item, written as an identifier Zentrix can resolve into an {@code ItemStack}.
     *
     * <p>Accepted forms are a vanilla material name, {@code custom:<items.yml key>},
     * {@code itemsadder:<namespace:id>} and {@code nexo:<id>}. An identifier belonging to a plugin
     * that is not installed is rejected when it is entered, so a stored value always resolves.
     */
    ITEM,

    /**
     * A placed block, written as an identifier Zentrix can match against a block in the world.
     *
     * <p>Accepted forms are a vanilla block material name, {@code itemsadder:<namespace:id>} and
     * {@code nexo:<id>}. Item-only material names are rejected, because nothing in a world could
     * ever match them.
     */
    BLOCK;

    /** Whether this field accepts the numeric bounds of its declaration. */
    public boolean isNumeric() {
        return this == INTEGER || this == DECIMAL;
    }

    /** Whether values of this field are typed in chat rather than clicked. */
    public boolean isTyped() {
        return this == STRING || this == MATERIAL || this == ITEM || this == BLOCK;
    }
}
