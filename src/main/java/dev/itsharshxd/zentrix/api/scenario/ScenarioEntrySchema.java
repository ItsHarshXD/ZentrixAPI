package dev.itsharshxd.zentrix.api.scenario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * The shape of every entry in a {@link ScenarioSettingType#STRUCTURED_LIST} setting.
 *
 * <p>A schema is an ordered list of {@link ScenarioEntryField}s. That keeps the setting type
 * domain-independent: one field describes a plain list of values, two describe a list of pairs, and
 * more describe whatever the scenario needs. Zentrix never interprets a field's meaning; it only
 * validates it, renders it, stores it, and hands it back.
 *
 * <p>Field order is the order an administrator sees in the entry editor, so declare the field that
 * identifies an entry first: the GUI uses {@link #primary()} as the headline of every entry it lists.
 *
 * @since 1.8.0
 */
public final class ScenarioEntrySchema {

    private final List<ScenarioEntryField> fields;
    private final Map<String, ScenarioEntryField> byKey;

    private ScenarioEntrySchema(List<ScenarioEntryField> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("An entry schema needs at least one field");
        }

        Map<String, ScenarioEntryField> resolved = new LinkedHashMap<>();
        List<ScenarioEntryField> ordered = new ArrayList<>(fields.size());
        for (ScenarioEntryField field : fields) {
            if (field == null) {
                throw new IllegalArgumentException("An entry schema cannot contain a null field");
            }
            if (resolved.putIfAbsent(field.key(), field) != null) {
                throw new IllegalArgumentException(
                        "Entry schema declares the field '" + field.key() + "' twice");
            }
            ordered.add(field);
        }

        this.fields = List.copyOf(ordered);
        this.byKey = Collections.unmodifiableMap(resolved);
    }

    /** A schema made of the given fields, in the order they are listed. */
    @NotNull
    public static ScenarioEntrySchema of(@NotNull ScenarioEntryField... fields) {
        return new ScenarioEntrySchema(List.of(fields));
    }

    /** A schema made of the given fields, in the order they are listed. */
    @NotNull
    public static ScenarioEntrySchema of(@NotNull List<ScenarioEntryField> fields) {
        return new ScenarioEntrySchema(fields);
    }

    /** Every field, in declaration order. */
    @NotNull
    public List<ScenarioEntryField> fields() {
        return fields;
    }

    /** Every field keyed by its own key, in declaration order. */
    @NotNull
    public Map<String, ScenarioEntryField> byKey() {
        return byKey;
    }

    @NotNull
    public Optional<ScenarioEntryField> field(@NotNull String key) {
        return Optional.ofNullable(byKey.get(key));
    }

    public boolean has(@NotNull String key) {
        return byKey.containsKey(key);
    }

    /** The first declared field, which the GUIs use as the headline of an entry. */
    @NotNull
    public ScenarioEntryField primary() {
        return fields.getFirst();
    }

    public int size() {
        return fields.size();
    }

    /** The keys of the fields an entry cannot leave out. */
    @NotNull
    public List<String> requiredKeys() {
        return fields.stream().filter(ScenarioEntryField::required).map(ScenarioEntryField::key).toList();
    }

    /** An entry holding only the defaults of the optional fields, as a starting point for an editor. */
    @NotNull
    public ScenarioEntry emptyEntry() {
        Map<String, Object> values = new LinkedHashMap<>();
        for (ScenarioEntryField field : fields) {
            if (!field.required() && field.defaultValue() != null) {
                values.put(field.key(), field.defaultValue());
            }
        }
        return new ScenarioEntry(this, values);
    }

    /** Whether every required field of an entry carries a value. */
    public boolean isComplete(@NotNull ScenarioEntry entry) {
        for (ScenarioEntryField field : fields) {
            if (field.required() && entry.raw(field.key()).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ScenarioEntrySchema" + byKey.keySet();
    }
}
