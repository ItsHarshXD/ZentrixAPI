package dev.itsharshxd.zentrix.api.scenario;

import dev.itsharshxd.zentrix.api.arena.ArenaSourceLease;
import dev.itsharshxd.zentrix.api.scenario.vote.ScenarioNoVoteRule;
import dev.itsharshxd.zentrix.api.scenario.vote.ScenarioTieRule;
import dev.itsharshxd.zentrix.api.scenario.vote.ScenarioVoteVisibility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A set of scenario overrides an addon hands to Zentrix for one source arena.
 *
 * <p>This is the same shape as an arena's section of {@code scenarios.yml}, expressed as a value
 * instead of as a file: how a match picks its scenarios, the administrator set, the automatic and
 * voting rules, and — per scenario — whether it is switched on, which game types it is kept out of,
 * and what its settings are. It is read ahead of both the stored arena overrides and the global
 * configuration, and it is never written to disk.
 *
 * <p>A profile is attached to an {@link ArenaSourceLease}, so the arena a match is created from
 * carries the rules that match will run under. Anything the profile does not mention falls through
 * to the ordinary configuration, which is what makes an {@linkplain #isEmpty() empty} profile mean
 * "use the server's own settings" rather than "run nothing".
 *
 * <p>Values are stored under the same keys the configuration file uses, so a profile built by the
 * builder and one loaded from an addon's own YAML are the same thing. Every value is validated when
 * the match reads it: a setting whose value no longer fits what the scenario declares is skipped
 * exactly as a stored one would be, and a scenario that is not registered is ignored without its
 * values being lost.
 *
 * @since 1.6.0
 */
public final class ScenarioProfile {

    private static final ScenarioProfile EMPTY = new ScenarioProfile(Map.of());

    /** Where a match's selection mode lives. */
    public static final String SELECTION_MODE = "selection.mode";
    /** Where the administrator set lives. */
    public static final String ADMIN_SET = "selection.admin-set";
    /** How many scenarios an automatic match draws. */
    public static final String AUTOMATIC_COUNT = "automatic.count";
    /** What an automatic match may draw from; empty means every selectable scenario. */
    public static final String AUTOMATIC_POOL = "automatic.pool";
    /** The prefix every voting rule sits under. */
    public static final String VOTING_PREFIX = "voting.";
    /** The prefix every per-scenario section sits under. */
    public static final String SCENARIOS_PREFIX = "scenarios.";

    private final Map<String, Object> values;

    private ScenarioProfile(Map<String, Object> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    /** A profile that overrides nothing, so every match reads the server's own configuration. */
    @NotNull
    public static ScenarioProfile empty() {
        return EMPTY;
    }

    /**
     * A profile built from values already keyed by configuration path.
     *
     * <p>Useful for an addon that stores its profile in a YAML file of its own: the paths are the
     * ones {@code scenarios.yml} uses under an arena, so the file can be read straight into a
     * profile. Blank paths and null values are dropped.
     */
    @NotNull
    public static ScenarioProfile of(@Nullable Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            return EMPTY;
        }
        Map<String, Object> copied = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String path = normalizePath(entry.getKey());
            if (path != null && entry.getValue() != null) {
                copied.put(path, storable(entry.getValue()));
            }
        }
        return copied.isEmpty() ? EMPTY : new ScenarioProfile(copied);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /** Whether this profile overrides nothing at all. */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /** Every override, keyed by the configuration path it applies to. */
    @NotNull
    public Map<String, Object> values() {
        return values;
    }

    /** One override, empty when this profile has no opinion about that path. */
    @NotNull
    public Optional<Object> value(@NotNull String path) {
        String normalized = normalizePath(path);
        return normalized == null ? Optional.empty() : Optional.ofNullable(values.get(normalized));
    }

    /** The selection mode this profile imposes, empty when it leaves the decision alone. */
    @NotNull
    public Optional<ScenarioSelectionMode> selectionMode() {
        return value(SELECTION_MODE)
                .map(String::valueOf)
                .map(raw -> {
                    try {
                        return ScenarioSelectionMode.valueOf(
                                raw.trim().toUpperCase(Locale.ROOT).replace('-', '_'));
                    } catch (IllegalArgumentException invalid) {
                        return null;
                    }
                });
    }

    /** The scenario IDs this profile mentions anywhere, for diagnostics. */
    @NotNull
    public Set<String> configuredScenarioIds() {
        Set<String> ids = new LinkedHashSet<>();
        for (String path : values.keySet()) {
            if (!path.startsWith(SCENARIOS_PREFIX)) {
                continue;
            }
            String remainder = path.substring(SCENARIOS_PREFIX.length());
            int separator = remainder.indexOf('.');
            if (separator > 0) {
                ids.add(remainder.substring(0, separator));
            }
        }
        return Collections.unmodifiableSet(ids);
    }

    @Override
    public String toString() {
        return "ScenarioProfile[" + values.size() + " overrides]";
    }

    /**
     * The configuration path a value belongs at, or null when it is unusable.
     *
     * <p>Lower-cased, because every path this maps onto is: a scenario ID is lower-case by
     * declaration, and so are the fixed keys around it.
     */
    @Nullable
    private static String normalizePath(@Nullable String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        String trimmed = path.trim().toLowerCase(Locale.ROOT);
        return trimmed.startsWith(".") || trimmed.endsWith(".") ? null : trimmed;
    }

    /**
     * The shape a value is kept in.
     *
     * <p>Collections become lists, which is the one shape a configured list is ever read back as,
     * so a profile built from a {@link Set} behaves like one loaded from YAML.
     */
    private static Object storable(Object value) {
        return value instanceof Collection<?> collection && !(value instanceof List<?>)
                ? List.copyOf(collection)
                : value;
    }

    /** Fluent builder for {@link ScenarioProfile}. */
    public static final class Builder {

        private final Map<String, Object> values = new LinkedHashMap<>();

        private Builder() {
        }

        /** How matches on this arena pick their scenarios. */
        @NotNull
        public Builder selectionMode(@Nullable ScenarioSelectionMode mode) {
            return raw(SELECTION_MODE, mode == null ? null : mode.name());
        }

        /** The scenarios an administrator chose, used by the administrator-set mode. */
        @NotNull
        public Builder adminSet(@Nullable Collection<String> scenarioIds) {
            return raw(ADMIN_SET, ids(scenarioIds));
        }

        /** How many scenarios an automatic match draws. */
        @NotNull
        public Builder automaticCount(int count) {
            return raw(AUTOMATIC_COUNT, Math.max(0, Math.min(64, count)));
        }

        /** What an automatic match draws from; an empty list means every selectable scenario. */
        @NotNull
        public Builder automaticPool(@Nullable Collection<String> scenarioIds) {
            return raw(AUTOMATIC_POOL, ids(scenarioIds));
        }

        /** How long a lobby votes, in seconds. */
        @NotNull
        public Builder votingDuration(int seconds) {
            return voting("duration", Math.max(1, Math.min(3600, seconds)));
        }

        @NotNull
        public Builder votesPerPlayer(int votes) {
            return voting("votes-per-player", Math.max(1, Math.min(64, votes)));
        }

        @NotNull
        public Builder voteWinnerCount(int winners) {
            return voting("winner-count", Math.max(1, Math.min(64, winners)));
        }

        @NotNull
        public Builder allowVoteChanges(boolean allowed) {
            return voting("allow-changes", allowed);
        }

        @NotNull
        public Builder voteVisibility(@Nullable ScenarioVoteVisibility visibility) {
            return voting("result-visibility", visibility == null ? null : visibility.name());
        }

        @NotNull
        public Builder tieRule(@Nullable ScenarioTieRule rule) {
            return voting("tie-rule", rule == null ? null : rule.name());
        }

        @NotNull
        public Builder noVoteRule(@Nullable ScenarioNoVoteRule rule) {
            return voting("no-vote-rule", rule == null ? null : rule.name());
        }

        /** Which scenarios may be voted for; an empty list means every eligible scenario. */
        @NotNull
        public Builder votingEligible(@Nullable Collection<String> scenarioIds) {
            return voting("eligible", ids(scenarioIds));
        }

        @NotNull
        public Builder closeVoteWhenEveryoneVoted(boolean close) {
            return voting("close-when-everyone-voted", close);
        }

        @NotNull
        public Builder announceVoteResults(boolean announce) {
            return voting("announce-results", announce);
        }

        /** Switches one scenario on or off for matches on this arena. */
        @NotNull
        public Builder scenarioEnabled(@NotNull String scenarioId, boolean enabled) {
            return raw(SCENARIOS_PREFIX + scenarioId + ".enabled", enabled);
        }

        /** The {@code game-types.yml} names one scenario is kept out of. */
        @NotNull
        public Builder blockedGameTypes(
                @NotNull String scenarioId, @Nullable Collection<String> gameTypes) {
            return raw(SCENARIOS_PREFIX + scenarioId + ".blocked-game-types", ids(gameTypes));
        }

        /** One scenario setting's value, in the shape the setting declares. */
        @NotNull
        public Builder setting(
                @NotNull String scenarioId, @NotNull String key, @Nullable Object value) {
            return raw(SCENARIOS_PREFIX + scenarioId + ".settings." + key, value);
        }

        /**
         * One override at an explicit configuration path.
         *
         * <p>The escape hatch for anything the typed methods do not cover, and the natural way to
         * copy a profile out of an addon's own YAML file. A null value removes the override.
         */
        @NotNull
        public Builder raw(@NotNull String path, @Nullable Object value) {
            String normalized = normalizePath(path);
            if (normalized == null) {
                return this;
            }
            if (value == null) {
                values.remove(normalized);
            } else {
                values.put(normalized, storable(value));
            }
            return this;
        }

        @NotNull
        public ScenarioProfile build() {
            return values.isEmpty() ? EMPTY : new ScenarioProfile(values);
        }

        private Builder voting(String key, @Nullable Object value) {
            return raw(VOTING_PREFIX + key, value);
        }

        /** Scenario and game-type IDs, lower-cased and de-duplicated, as a list YAML reads back. */
        @Nullable
        private static List<String> ids(@Nullable Collection<String> values) {
            if (values == null) {
                return null;
            }
            Set<String> normalized = new LinkedHashSet<>();
            for (String value : values) {
                if (value != null && !value.isBlank()) {
                    normalized.add(value.trim().toLowerCase(Locale.ROOT));
                }
            }
            return new ArrayList<>(normalized);
        }
    }
}
