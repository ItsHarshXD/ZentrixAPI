package dev.itsharshxd.zentrix.api.scenario;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One configurable value of a scenario.
 *
 * <p>A scenario declares its settings once, in its {@link ScenarioDescriptor}. Zentrix uses the
 * declaration to validate values, to render an editor in the management GUI, and to supply the
 * default whenever no administrator has configured the setting.
 *
 * <p>Declaring a setting never writes anything to disk. A value only reaches {@code scenarios.yml}
 * once an administrator edits it, which keeps a dynamically registered scenario out of the main
 * configuration until somebody deliberately configures it.
 *
 * @param key           the setting key, unique within its scenario
 * @param type          how the value is validated and edited
 * @param defaultValue  the value used until an administrator configures one; never null
 * @param minimum       the lowest accepted number, for {@link ScenarioSettingType#INTEGER} and
 *                      {@link ScenarioSettingType#DECIMAL}
 * @param maximum       the highest accepted number, for the same two types
 * @param allowedValues the accepted values of an {@link ScenarioSettingType#ENUM} setting
 * @param displayName   the label shown in the GUI; falls back to the key when blank
 * @param description   the lines shown under the label in the GUI
 * @param entrySchema   the shape of one entry of a {@link ScenarioSettingType#STRUCTURED_LIST}
 *                      setting, and null for every other type
 * @since 1.6.0
 */
public record ScenarioSetting(
        @NotNull String key,
        @NotNull ScenarioSettingType type,
        @NotNull Object defaultValue,
        double minimum,
        double maximum,
        @NotNull Set<String> allowedValues,
        @NotNull String displayName,
        @NotNull List<String> description,
        @Nullable ScenarioEntrySchema entrySchema) {

    public ScenarioSetting {
        key = require(key, "key");
        if (type == null) {
            throw new IllegalArgumentException("A scenario setting needs a type");
        }
        if (defaultValue == null) {
            throw new IllegalArgumentException("Scenario setting '" + key + "' needs a default value");
        }
        allowedValues = allowedValues == null
                ? Set.of()
                : Collections.unmodifiableSet(new LinkedHashSet<>(allowedValues));
        displayName = displayName == null || displayName.isBlank() ? key : displayName;
        description = description == null ? List.of() : List.copyOf(description);

        if (type == ScenarioSettingType.ENUM && allowedValues.isEmpty()) {
            throw new IllegalArgumentException(
                    "Scenario setting '" + key + "' is an ENUM and needs allowed values");
        }
        if (minimum > maximum) {
            throw new IllegalArgumentException(
                    "Scenario setting '" + key + "' has a minimum above its maximum");
        }
        if (type == ScenarioSettingType.STRUCTURED_LIST && entrySchema == null) {
            throw new IllegalArgumentException("Scenario setting '" + key
                    + "' is a STRUCTURED_LIST and needs an entry schema");
        }
        // A schema on any other type would be silently unreachable, and a setting whose declaration
        // says one thing while its type says another is worth refusing outright.
        if (type != ScenarioSettingType.STRUCTURED_LIST && entrySchema != null) {
            throw new IllegalArgumentException("Scenario setting '" + key + "' declares an entry"
                    + " schema but is a " + type + " rather than a STRUCTURED_LIST");
        }
    }

    /**
     * The pre-1.6.0 constructor, kept so anything compiled against an earlier API still links.
     *
     * <p>Declares no entry schema, which is exactly right: {@link ScenarioSettingType#STRUCTURED_LIST}
     * did not exist before the schema did.
     */
    public ScenarioSetting(
            @NotNull String key,
            @NotNull ScenarioSettingType type,
            @NotNull Object defaultValue,
            double minimum,
            double maximum,
            @NotNull Set<String> allowedValues,
            @NotNull String displayName,
            @NotNull List<String> description) {
        this(key, type, defaultValue, minimum, maximum, allowedValues, displayName, description, null);
    }

    /** A toggle. */
    @NotNull
    public static ScenarioSetting bool(@NotNull String key, boolean defaultValue) {
        return builder(key, ScenarioSettingType.BOOLEAN, defaultValue).build();
    }

    /** A whole number between {@code minimum} and {@code maximum}, inclusive. */
    @NotNull
    public static ScenarioSetting integer(
            @NotNull String key, int defaultValue, int minimum, int maximum) {
        return builder(key, ScenarioSettingType.INTEGER, defaultValue)
                .range(minimum, maximum)
                .build();
    }

    /** A fractional number between {@code minimum} and {@code maximum}, inclusive. */
    @NotNull
    public static ScenarioSetting decimal(
            @NotNull String key, double defaultValue, double minimum, double maximum) {
        return builder(key, ScenarioSettingType.DECIMAL, defaultValue)
                .range(minimum, maximum)
                .build();
    }

    /** Free text. */
    @NotNull
    public static ScenarioSetting text(@NotNull String key, @NotNull String defaultValue) {
        return builder(key, ScenarioSettingType.STRING, defaultValue).build();
    }

    /** One of a fixed set of choices. */
    @NotNull
    public static ScenarioSetting choice(
            @NotNull String key, @NotNull String defaultValue, @NotNull String... choices) {
        return builder(key, ScenarioSettingType.ENUM, defaultValue)
                .allowed(choices)
                .build();
    }

    /** A list of Bukkit material names. */
    @NotNull
    public static ScenarioSetting materials(@NotNull String key, @NotNull List<String> defaultValue) {
        return builder(key, ScenarioSettingType.MATERIAL_LIST, List.copyOf(defaultValue)).build();
    }

    /**
     * A list of entries shaped by {@code schema}.
     *
     * <p>The schema decides what one entry holds: a single field for a plain list, two for a list of
     * pairs, more for anything else the scenario needs. Zentrix stays unaware of what the fields
     * mean, so the same setting type serves any scenario.
     *
     * @param schema       the fields every entry of this setting carries
     * @param defaultValue the entries used until an administrator configures the setting
     */
    @NotNull
    public static ScenarioSetting entries(
            @NotNull String key,
            @NotNull ScenarioEntrySchema schema,
            @NotNull List<ScenarioEntry> defaultValue) {
        return builder(key, ScenarioSettingType.STRUCTURED_LIST, List.copyOf(defaultValue))
                .schema(schema)
                .build();
    }

    /** A list of entries shaped by {@code schema}, empty until an administrator adds one. */
    @NotNull
    public static ScenarioSetting entries(
            @NotNull String key, @NotNull ScenarioEntrySchema schema) {
        return entries(key, schema, List.of());
    }

    @NotNull
    public static Builder builder(
            @NotNull String key, @NotNull ScenarioSettingType type, @NotNull Object defaultValue) {
        return new Builder(key, type, defaultValue);
    }

    /** Whether this setting accepts numeric bounds. */
    public boolean isNumeric() {
        return type == ScenarioSettingType.INTEGER || type == ScenarioSettingType.DECIMAL;
    }

    /** Whether this setting holds a list of entries shaped by {@link #entrySchema()}. */
    public boolean isStructured() {
        return type == ScenarioSettingType.STRUCTURED_LIST && entrySchema != null;
    }

    /**
     * The declared entry defaults of a {@link ScenarioSettingType#STRUCTURED_LIST} setting.
     *
     * <p>Empty for every other type, and for a structured setting whose default was declared in a
     * shape this version no longer understands, so a caller never has to guard the declaration.
     */
    @NotNull
    public List<ScenarioEntry> defaultEntries() {
        if (!isStructured() || !(defaultValue instanceof java.util.Collection<?> values)) {
            return List.of();
        }
        List<ScenarioEntry> entries = new java.util.ArrayList<>(values.size());
        for (Object value : values) {
            if (value instanceof ScenarioEntry entry) {
                entries.add(entry);
            } else if (value instanceof java.util.Map<?, ?> map) {
                java.util.Map<String, Object> fields = new java.util.LinkedHashMap<>();
                map.forEach((field, stored) -> fields.put(String.valueOf(field), stored));
                entries.add(new ScenarioEntry(entrySchema, fields));
            }
        }
        return List.copyOf(entries);
    }

    private static String require(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("A scenario setting needs a " + field);
        }
        return value.trim();
    }

    /** Fluent builder for {@link ScenarioSetting}. */
    public static final class Builder {

        private final String key;
        private final ScenarioSettingType type;
        private final Object defaultValue;
        private double minimum = Integer.MIN_VALUE;
        private double maximum = Integer.MAX_VALUE;
        private final Set<String> allowedValues = new LinkedHashSet<>();
        private String displayName = "";
        private List<String> description = List.of();
        private ScenarioEntrySchema entrySchema;

        private Builder(String key, ScenarioSettingType type, Object defaultValue) {
            this.key = key;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        /** Bounds for a numeric setting. Ignored by every other type. */
        @NotNull
        public Builder range(double minimum, double maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
            return this;
        }

        /** The accepted values of an {@link ScenarioSettingType#ENUM} setting. */
        @NotNull
        public Builder allowed(@NotNull String... values) {
            Collections.addAll(allowedValues, values);
            return this;
        }

        /** The label shown in the management GUI. */
        @NotNull
        public Builder displayName(@Nullable String displayName) {
            this.displayName = displayName == null ? "" : displayName;
            return this;
        }

        /** The description lines shown in the management GUI. */
        @NotNull
        public Builder description(@NotNull String... lines) {
            this.description = List.of(lines);
            return this;
        }

        /** The shape of one entry, required by a {@link ScenarioSettingType#STRUCTURED_LIST}. */
        @NotNull
        public Builder schema(@Nullable ScenarioEntrySchema entrySchema) {
            this.entrySchema = entrySchema;
            return this;
        }

        @NotNull
        public ScenarioSetting build() {
            return new ScenarioSetting(
                    key, type, defaultValue, minimum, maximum, allowedValues, displayName, description,
                    entrySchema);
        }
    }
}
