package dev.itsharshxd.zentrix.api.scenario;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Everything Zentrix knows about a scenario before it ever runs: how to name it, how to show it,
 * how it combines with other scenarios, and what it can be configured with.
 *
 * <p>A descriptor is immutable and is supplied once by {@link Scenario#descriptor()}. Built-in and
 * third-party scenarios use exactly the same descriptor, so a registered addon scenario appears in
 * the commands, GUIs, voting pools, automatic-selection pools, validation and API lookups on equal
 * footing with the ones Zentrix ships.
 *
 * <p>There is no fixed category list. {@link #tags()} is free-form and exists purely so a scenario
 * can group or filter itself however its author likes.
 *
 * @since 1.7.0
 */
public final class ScenarioDescriptor {

    private final String id;
    private final String displayName;
    private final List<String> description;
    private final List<String> announcement;
    private final Material icon;
    private final String version;
    private final List<String> authors;
    private final int priority;
    private final Set<String> dependencies;
    private final Set<String> conflicts;
    private final Set<ScenarioCapability> capabilities;
    private final Set<ScenarioCapability> capabilityConflicts;
    private final Set<String> requiredPlugins;
    private final Set<String> tags;
    private final Map<String, ScenarioSetting> settings;
    private final boolean votable;
    private final boolean enabledByDefault;

    private ScenarioDescriptor(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName.isBlank() ? builder.id : builder.displayName;
        this.description = List.copyOf(builder.description);
        this.announcement = List.copyOf(builder.announcement);
        this.icon = builder.icon;
        this.version = builder.version;
        this.authors = List.copyOf(builder.authors);
        this.priority = builder.priority;
        this.dependencies = Collections.unmodifiableSet(new LinkedHashSet<>(builder.dependencies));
        this.conflicts = Collections.unmodifiableSet(new LinkedHashSet<>(builder.conflicts));
        this.capabilities = Collections.unmodifiableSet(new LinkedHashSet<>(builder.capabilities));
        this.capabilityConflicts =
                Collections.unmodifiableSet(new LinkedHashSet<>(builder.capabilityConflicts));
        this.requiredPlugins = Collections.unmodifiableSet(new LinkedHashSet<>(builder.requiredPlugins));
        this.tags = Collections.unmodifiableSet(new LinkedHashSet<>(builder.tags));
        this.settings = Collections.unmodifiableMap(new LinkedHashMap<>(builder.settings));
        this.votable = builder.votable;
        this.enabledByDefault = builder.enabledByDefault;
    }

    /** The stable lower-case identifier used in configuration, commands and API lookups. */
    @NotNull
    public String id() {
        return id;
    }

    /** The name shown to players and administrators. */
    @NotNull
    public String displayName() {
        return displayName;
    }

    /** The description lines shown in the GUIs. */
    @NotNull
    public List<String> description() {
        return description;
    }

    /**
     * The lines announced to a match once its players are in the arena.
     *
     * <p>Zentrix sends one announcement per scenario a match runs, spaced out by the delay
     * configured in {@code scenarios.yml}. A server owner may override these lines under
     * {@code scenarios.announcements.scenarios.<id>} in the locale file, which is why this is only
     * the wording the scenario itself suggests. An empty list falls back to the locale's shared
     * announcement layout, built from {@link #displayName()} and {@link #description()}.
     *
     * <p>Lines carry the usual Zentrix formatting: {@code &c} and {@code &#RRGGBB} colors and
     * {@code <center>...</center>} tags.
     */
    @NotNull
    public List<String> announcement() {
        return announcement;
    }

    /** The icon used wherever the scenario is listed. */
    @NotNull
    public Material icon() {
        return icon;
    }

    /** The scenario's own version string, for addon diagnostics. */
    @NotNull
    public String version() {
        return version;
    }

    @NotNull
    public List<String> authors() {
        return authors;
    }

    /**
     * The activation and hook order of this scenario.
     *
     * <p>A higher priority activates earlier and sees gameplay hooks first, which is what makes a
     * combination of scenarios deterministic. Scenarios sharing a priority are ordered by ID.
     */
    public int priority() {
        return priority;
    }

    /** IDs of scenarios that must be active alongside this one. */
    @NotNull
    public Set<String> dependencies() {
        return dependencies;
    }

    /** IDs of scenarios that must never be active alongside this one. */
    @NotNull
    public Set<String> conflicts() {
        return conflicts;
    }

    /**
     * The gameplay areas this scenario takes charge of.
     *
     * <p>Anything declared here makes the scenario incompatible with every scenario that conflicts
     * with the same capability, whether or not the two know about each other.
     */
    @NotNull
    public Set<ScenarioCapability> capabilities() {
        return capabilities;
    }

    /**
     * The gameplay areas this scenario cannot share a match with.
     *
     * <p>Every scenario {@linkplain #capabilities() providing} one of these conflicts with this one.
     * Declaring the same capability in both sets forms a mutual-exclusion group: any two members
     * conflict, and the group stays open to scenarios written later.
     */
    @NotNull
    public Set<ScenarioCapability> capabilityConflicts() {
        return capabilityConflicts;
    }

    /** Whether this scenario declared it takes charge of a capability. */
    public boolean provides(@Nullable ScenarioCapability capability) {
        return capability != null && capabilities.contains(capability);
    }

    /** Names of Bukkit plugins that must be enabled for this scenario to be selectable. */
    @NotNull
    public Set<String> requiredPlugins() {
        return requiredPlugins;
    }

    /** Free-form labels; Zentrix imposes no category system of its own. */
    @NotNull
    public Set<String> tags() {
        return tags;
    }

    /** The scenario's configurable values, keyed by setting key, in declaration order. */
    @NotNull
    public Map<String, ScenarioSetting> settings() {
        return settings;
    }

    @NotNull
    public java.util.Optional<ScenarioSetting> setting(@NotNull String key) {
        return java.util.Optional.ofNullable(settings.get(key));
    }

    /** Whether players may vote for this scenario. */
    public boolean votable() {
        return votable;
    }

    /**
     * Whether the scenario counts as enabled until an administrator says otherwise.
     *
     * <p>Built-in scenarios ship enabled. A dynamically registered scenario normally leaves this
     * false, so it stays inert until somebody configures it.
     */
    public boolean enabledByDefault() {
        return enabledByDefault;
    }

    @NotNull
    public static Builder builder(@NotNull String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        return "ScenarioDescriptor[" + id + " v" + version + "]";
    }

    /** Fluent builder for {@link ScenarioDescriptor}. */
    public static final class Builder {

        private final String id;
        private String displayName = "";
        private List<String> description = List.of();
        private List<String> announcement = List.of();
        private Material icon = Material.PAPER;
        private String version = "1.0.0";
        private List<String> authors = List.of();
        private int priority = 0;
        private final Set<String> dependencies = new LinkedHashSet<>();
        private final Set<String> conflicts = new LinkedHashSet<>();
        private final Set<ScenarioCapability> capabilities = new LinkedHashSet<>();
        private final Set<ScenarioCapability> capabilityConflicts = new LinkedHashSet<>();
        private final Set<String> requiredPlugins = new LinkedHashSet<>();
        private final Set<String> tags = new LinkedHashSet<>();
        private final Map<String, ScenarioSetting> settings = new LinkedHashMap<>();
        private boolean votable = true;
        private boolean enabledByDefault = false;

        private Builder(String id) {
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("A scenario needs an id");
            }
            String normalized = id.trim().toLowerCase(Locale.ROOT);
            if (!normalized.matches("[a-z0-9][a-z0-9_-]{0,63}")) {
                throw new IllegalArgumentException("Invalid scenario id '" + id
                        + "': use 1-64 lower-case letters, digits, '-' or '_'");
            }
            this.id = normalized;
        }

        @NotNull
        public Builder displayName(@Nullable String displayName) {
            this.displayName = displayName == null ? "" : displayName;
            return this;
        }

        @NotNull
        public Builder description(@NotNull String... lines) {
            this.description = List.of(lines);
            return this;
        }

        /**
         * The lines this scenario suggests announcing when a match running it starts.
         *
         * <p>Optional. Leaving it out lets the locale's shared announcement layout describe the
         * scenario from its name and description, and a server owner can override whatever is
         * given here in the locale file either way.
         */
        @NotNull
        public Builder announcement(@NotNull String... lines) {
            this.announcement = List.of(lines);
            return this;
        }

        @NotNull
        public Builder icon(@NotNull Material icon) {
            this.icon = icon;
            return this;
        }

        @NotNull
        public Builder version(@NotNull String version) {
            this.version = version;
            return this;
        }

        @NotNull
        public Builder authors(@NotNull String... authors) {
            this.authors = List.of(authors);
            return this;
        }

        /** Higher activates and receives gameplay hooks earlier. */
        @NotNull
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        @NotNull
        public Builder dependsOn(@NotNull String... scenarioIds) {
            addNormalized(dependencies, scenarioIds);
            return this;
        }

        @NotNull
        public Builder conflictsWith(@NotNull String... scenarioIds) {
            addNormalized(conflicts, scenarioIds);
            return this;
        }

        /**
         * Declares the gameplay areas this scenario takes charge of.
         *
         * <p>Use this instead of naming every scenario that might one day get in the way: a
         * scenario conflicting with the capability keeps clear of this one without either side
         * knowing the other.
         */
        @NotNull
        public Builder provides(@NotNull ScenarioCapability... values) {
            Collections.addAll(capabilities, requireEach(values));
            return this;
        }

        /** Declares provided capabilities by identifier, for configuration-driven scenarios. */
        @NotNull
        public Builder provides(@NotNull String... capabilityIds) {
            addParsed(capabilities, capabilityIds);
            return this;
        }

        /**
         * Declares the gameplay areas this scenario cannot share a match with.
         *
         * <p>Passing a capability this scenario also {@linkplain #provides(ScenarioCapability...)
         * provides} makes it mutually exclusive with every other scenario providing the same one.
         */
        @NotNull
        public Builder conflictsWithCapability(@NotNull ScenarioCapability... values) {
            Collections.addAll(capabilityConflicts, requireEach(values));
            return this;
        }

        /** Declares conflicting capabilities by identifier. */
        @NotNull
        public Builder conflictsWithCapability(@NotNull String... capabilityIds) {
            addParsed(capabilityConflicts, capabilityIds);
            return this;
        }

        @NotNull
        public Builder requiresPlugin(@NotNull String... pluginNames) {
            Collections.addAll(requiredPlugins, pluginNames);
            return this;
        }

        @NotNull
        public Builder tags(@NotNull String... tags) {
            addNormalized(this.tags, tags);
            return this;
        }

        @NotNull
        public Builder setting(@NotNull ScenarioSetting setting) {
            settings.put(setting.key(), setting);
            return this;
        }

        @NotNull
        public Builder settings(@NotNull ScenarioSetting... values) {
            for (ScenarioSetting setting : values) {
                setting(setting);
            }
            return this;
        }

        /** Whether players may vote for this scenario. Defaults to true. */
        @NotNull
        public Builder votable(boolean votable) {
            this.votable = votable;
            return this;
        }

        /** Whether the scenario is enabled before an administrator configures it. */
        @NotNull
        public Builder enabledByDefault(boolean enabledByDefault) {
            this.enabledByDefault = enabledByDefault;
            return this;
        }

        @NotNull
        public ScenarioDescriptor build() {
            if (dependencies.contains(id)) {
                throw new IllegalArgumentException("Scenario '" + id + "' cannot depend on itself");
            }
            if (conflicts.contains(id)) {
                throw new IllegalArgumentException("Scenario '" + id + "' cannot conflict with itself");
            }
            for (String dependency : dependencies) {
                if (conflicts.contains(dependency)) {
                    throw new IllegalArgumentException("Scenario '" + id + "' both depends on and"
                            + " conflicts with '" + dependency + "'");
                }
            }
            return new ScenarioDescriptor(this);
        }

        private static ScenarioCapability[] requireEach(ScenarioCapability[] values) {
            for (ScenarioCapability capability : values) {
                if (capability == null) {
                    throw new IllegalArgumentException("A scenario capability must not be null");
                }
            }
            return values;
        }

        private static void addParsed(Set<ScenarioCapability> target, String[] capabilityIds) {
            for (String id : capabilityIds) {
                if (id != null && !id.isBlank()) {
                    target.add(ScenarioCapability.of(id));
                }
            }
        }

        private static void addNormalized(Set<String> target, String[] values) {
            for (String value : values) {
                if (value != null && !value.isBlank()) {
                    target.add(value.trim().toLowerCase(Locale.ROOT));
                }
            }
        }
    }
}
