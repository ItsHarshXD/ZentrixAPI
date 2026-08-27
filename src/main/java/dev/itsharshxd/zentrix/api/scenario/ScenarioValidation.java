package dev.itsharshxd.zentrix.api.scenario;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * The verdict on one combination of scenarios.
 *
 * <p>Validation is what makes combinations deterministic rather than surprising: it resolves
 * dependencies, rejects conflicting pairs, drops scenarios whose required plugins are missing, and
 * reports what it changed. Zentrix runs it before locking a match's scenarios in, and the
 * management GUI runs it while an administrator edits a set.
 *
 * @param valid     whether the combination can be used as-is, without any issue above
 *                  {@link Severity#INFO}
 * @param resolved  the scenarios that would actually run, in activation order: descending priority,
 *                  then ID
 * @param issues    everything validation found, in the order it was found
 * @since 1.6.0
 */
public record ScenarioValidation(
        boolean valid,
        @NotNull List<String> resolved,
        @NotNull List<Issue> issues) {

    public ScenarioValidation {
        resolved = List.copyOf(resolved);
        issues = List.copyOf(issues);
    }

    /** How badly an issue affects the combination. */
    public enum Severity {
        /** Something was adjusted silently, for example a dependency that was pulled in. */
        INFO,
        /** The combination still works, but a scenario was dropped from it. */
        WARNING,
        /** The combination cannot be used. */
        ERROR
    }

    /** What validation found wrong. */
    public enum Kind {
        /** A scenario ID is not registered. */
        UNKNOWN_SCENARIO,
        /** A scenario is registered but switched off. */
        DISABLED_SCENARIO,
        /** A required scenario was added to the set. */
        DEPENDENCY_ADDED,
        /** A required scenario is not registered at all. */
        DEPENDENCY_MISSING,
        /** Two scenarios in the set declared each other incompatible. */
        CONFLICT,
        /**
         * One scenario in the set conflicts with a
         * {@link ScenarioCapability} another one provides.
         */
        CAPABILITY_CONFLICT,
        /** A scenario needs a plugin that is not enabled. */
        MISSING_PLUGIN,
        /** A dependency chain loops back on itself. */
        CIRCULAR_DEPENDENCY,
        /** More scenarios were selected than the configuration allows. */
        LIMIT_EXCEEDED
    }

    /**
     * One finding.
     *
     * @param kind       what was found
     * @param severity   how badly it affects the combination
     * @param scenarioId the scenario the finding is about
     * @param related    the other scenarios or plugins involved, if any
     * @param message    a human-readable explanation
     */
    public record Issue(
            @NotNull Kind kind,
            @NotNull Severity severity,
            @NotNull String scenarioId,
            @NotNull Set<String> related,
            @NotNull String message) {

        public Issue {
            related = Set.copyOf(related);
        }
    }

    /** Whether anything at all was reported. */
    public boolean hasIssues() {
        return !issues.isEmpty();
    }

    /** The findings that make the combination unusable. */
    @NotNull
    public List<Issue> errors() {
        return issues.stream().filter(issue -> issue.severity() == Severity.ERROR).toList();
    }
}
