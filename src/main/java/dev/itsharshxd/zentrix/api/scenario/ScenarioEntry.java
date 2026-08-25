package dev.itsharshxd.zentrix.api.scenario;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One entry of a {@link ScenarioSettingType#STRUCTURED_LIST} setting.
 *
 * <p>An entry is an immutable set of values keyed by the field keys of its {@link ScenarioEntrySchema}.
 * Every accessor falls back to the field's declared default, so reading a field a stored entry left out
 * yields the same value the scenario asked for rather than nothing at all.
 *
 * <p>Instances are cheap and immutable: {@link #with(String, Object)} returns a new entry, which is how
 * the management GUI edits one field at a time without ever mutating what a running match is using.
 *
 * @since 1.8.0
 */
public final class ScenarioEntry {

    private final ScenarioEntrySchema schema;
    private final Map<String, Object> values;

    /**
     * An entry of {@code schema} holding {@code values}.
     *
     * <p>Values whose keys the schema does not declare are dropped, so an entry can never carry a field
     * a later version of the scenario stopped declaring.
     */
    public ScenarioEntry(@NotNull ScenarioEntrySchema schema, @NotNull Map<String, Object> values) {
        this.schema = Objects.requireNonNull(schema, "schema");

        // Iterating the schema rather than the values keeps the field order the scenario declared,
        // which is the order the GUI renders and the order the value is written back to YAML in.
        Map<String, Object> resolved = new LinkedHashMap<>();
        for (ScenarioEntryField field : schema.fields()) {
            Object value = values == null ? null : values.get(field.key());
            if (value != null) {
                resolved.put(field.key(), value);
            }
        }
        this.values = Collections.unmodifiableMap(resolved);
    }

    @NotNull
    public ScenarioEntrySchema schema() {
        return schema;
    }

    /** The stored value of one field, without falling back to its default. */
    @NotNull
    public Optional<Object> raw(@NotNull String key) {
        return Optional.ofNullable(values.get(key));
    }

    /** Whether this entry stores a value of its own for the field. */
    public boolean has(@NotNull String key) {
        return values.containsKey(key);
    }

    /** The value of one field, falling back to the field's default. */
    @Nullable
    public Object get(@NotNull String key) {
        Object value = values.get(key);
        if (value != null) {
            return value;
        }
        return schema.field(key).map(ScenarioEntryField::defaultValue).orElse(null);
    }

    @NotNull
    public String getString(@NotNull String key) {
        Object value = get(key);
        return value == null ? "" : String.valueOf(value);
    }

    public int getInt(@NotNull String key) {
        Object value = get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return value == null ? 0 : Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException notANumber) {
            return 0;
        }
    }

    public double getDouble(@NotNull String key) {
        Object value = get(key);
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return value == null ? 0.0D : Double.parseDouble(String.valueOf(value).trim());
        } catch (NumberFormatException notANumber) {
            return 0.0D;
        }
    }

    public boolean getBoolean(@NotNull String key) {
        Object value = get(key);
        if (value instanceof Boolean bool) {
            return bool;
        }
        return value != null && Boolean.parseBoolean(String.valueOf(value));
    }

    /** The field read as a Bukkit material, empty when the stored name does not resolve. */
    @NotNull
    public Optional<Material> getMaterial(@NotNull String key) {
        String name = getString(key);
        return name.isBlank() ? Optional.empty() : Optional.ofNullable(Material.matchMaterial(name));
    }

    /** Every stored value, keyed by field key, in declaration order. */
    @NotNull
    public Map<String, Object> asMap() {
        return values;
    }

    /** A copy of this entry with one field changed; a null value clears the field. */
    @NotNull
    public ScenarioEntry with(@NotNull String key, @Nullable Object value) {
        Map<String, Object> changed = new LinkedHashMap<>(values);
        if (value == null) {
            changed.remove(key);
        } else {
            changed.put(key, value);
        }
        return new ScenarioEntry(schema, changed);
    }

    /** Whether every required field of the schema carries a value. */
    public boolean isComplete() {
        return schema.isComplete(this);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ScenarioEntry entry && values.equals(entry.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        return "ScenarioEntry" + values;
    }
}
