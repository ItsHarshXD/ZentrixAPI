package dev.itsharshxd.zentrix.api.scenario;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One field of a structured entry.
 *
 * <p>A scenario declares the fields of its entries once, in a {@link ScenarioEntrySchema}. Zentrix
 * uses the declaration to validate what an administrator types, to render one row per field in the
 * entry editor, and to fill in the value of an optional field that was left out.
 *
 * <p>A field is either required or optional. A required field has no default: an entry missing it is
 * rejected as a whole, which is what keeps a half-filled entry out of {@code scenarios.yml}. An
 * optional field falls back to its default whenever no value is stored, exactly like a
 * {@link ScenarioSetting} does.
 *
 * @param key           the field key, unique within its schema
 * @param type          how the value is validated and edited
 * @param defaultValue  the value used while nothing is stored; null for a required field
 * @param required      whether an entry without this field is rejected
 * @param minimum       the lowest accepted number, for {@link ScenarioFieldType#INTEGER} and
 *                      {@link ScenarioFieldType#DECIMAL}
 * @param maximum       the highest accepted number, for the same two types
 * @param allowedValues the accepted values of a {@link ScenarioFieldType#ENUM} field
 * @param displayName   the label shown in the GUI; falls back to the key when blank
 * @param description   the lines shown under the label in the GUI
 * @since 1.8.0
 */
public record ScenarioEntryField(
        @NotNull String key,
        @NotNull ScenarioFieldType type,
        @Nullable Object defaultValue,
        boolean required,
        double minimum,
        double maximum,
        @NotNull Set<String> allowedValues,
        @NotNull String displayName,
        @NotNull List<String> description) {

    public ScenarioEntryField {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("An entry field needs a key");
        }
        key = key.trim();
        if (type == null) {
            throw new IllegalArgumentException("Entry field '" + key + "' needs a type");
        }
        allowedValues = allowedValues == null
                ? Set.of()
                : Collections.unmodifiableSet(new LinkedHashSet<>(allowedValues));
        displayName = displayName == null || displayName.isBlank() ? key : displayName;
        description = description == null ? List.of() : List.copyOf(description);

        if (type == ScenarioFieldType.ENUM && allowedValues.isEmpty()) {
            throw new IllegalArgumentException(
                    "Entry field '" + key + "' is an ENUM and needs allowed values");
        }
        if (minimum > maximum) {
            throw new IllegalArgumentException(
                    "Entry field '" + key + "' has a minimum above its maximum");
        }
        if (!required && defaultValue == null) {
            throw new IllegalArgumentException(
                    "Optional entry field '" + key + "' needs a default value");
        }
    }

    /** A required field of any type. */
    @NotNull
    public static ScenarioEntryField required(@NotNull String key, @NotNull ScenarioFieldType type) {
        return builder(key, type).build();
    }

    /** A required item identifier, resolvable through vanilla, ItemsAdder, Nexo or {@code items.yml}. */
    @NotNull
    public static ScenarioEntryField item(@NotNull String key) {
        return required(key, ScenarioFieldType.ITEM);
    }

    /** A required block identifier, resolvable through vanilla, ItemsAdder or Nexo. */
    @NotNull
    public static ScenarioEntryField block(@NotNull String key) {
        return required(key, ScenarioFieldType.BLOCK);
    }

    /** An optional whole number between {@code minimum} and {@code maximum}, inclusive. */
    @NotNull
    public static ScenarioEntryField integer(
            @NotNull String key, int defaultValue, int minimum, int maximum) {
        return builder(key, ScenarioFieldType.INTEGER)
                .optional(defaultValue)
                .range(minimum, maximum)
                .build();
    }

    /** An optional toggle. */
    @NotNull
    public static ScenarioEntryField bool(@NotNull String key, boolean defaultValue) {
        return builder(key, ScenarioFieldType.BOOLEAN).optional(defaultValue).build();
    }

    /** An optional choice out of a fixed set. */
    @NotNull
    public static ScenarioEntryField choice(
            @NotNull String key, @NotNull String defaultValue, @NotNull String... choices) {
        return builder(key, ScenarioFieldType.ENUM)
                .optional(defaultValue)
                .allowed(choices)
                .build();
    }

    @NotNull
    public static Builder builder(@NotNull String key, @NotNull ScenarioFieldType type) {
        return new Builder(key, type);
    }

    /** Fluent builder for {@link ScenarioEntryField}. Fields are required unless made optional. */
    public static final class Builder {

        private final String key;
        private final ScenarioFieldType type;
        private Object defaultValue;
        private boolean required = true;
        private double minimum = Integer.MIN_VALUE;
        private double maximum = Integer.MAX_VALUE;
        private final Set<String> allowedValues = new LinkedHashSet<>();
        private String displayName = "";
        private List<String> description = List.of();

        private Builder(String key, ScenarioFieldType type) {
            this.key = key;
            this.type = type;
        }

        /** Makes the field optional, falling back to {@code defaultValue} when nothing is stored. */
        @NotNull
        public Builder optional(@NotNull Object defaultValue) {
            this.required = false;
            this.defaultValue = defaultValue;
            return this;
        }

        /** Bounds for a numeric field. Ignored by every other type. */
        @NotNull
        public Builder range(double minimum, double maximum) {
            this.minimum = minimum;
            this.maximum = maximum;
            return this;
        }

        /** The accepted values of a {@link ScenarioFieldType#ENUM} field. */
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

        @NotNull
        public ScenarioEntryField build() {
            return new ScenarioEntryField(
                    key, type, defaultValue, required, minimum, maximum,
                    allowedValues, displayName, description);
        }
    }
}
