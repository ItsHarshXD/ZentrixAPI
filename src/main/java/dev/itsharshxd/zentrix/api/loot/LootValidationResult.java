package dev.itsharshxd.zentrix.api.loot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/** Immutable validation report for a detached loot configuration. */
public final class LootValidationResult {

    private final List<LootValidationIssue> issues;
    private final Map<String, List<LootValidationIssue>> entryIssues;

    public LootValidationResult(@NotNull List<LootValidationIssue> issues) {
        this.issues = List.copyOf(issues);
        this.entryIssues = this.issues.stream()
                .filter(issue -> issue.entryId().isPresent())
                .collect(Collectors.groupingBy(
                        issue -> issue.entryId().orElseThrow(),
                        LinkedHashMap::new,
                        Collectors.toList()));
    }

    @NotNull public List<LootValidationIssue> issues() { return issues; }
    @NotNull public List<LootValidationIssue> globalIssues() {
        return issues.stream().filter(LootValidationIssue::global).toList();
    }
    @NotNull public List<LootValidationIssue> issuesFor(@NotNull String entryId) {
        return entryIssues.getOrDefault(entryId, List.of());
    }
    @NotNull public Set<String> invalidEntryIds() { return Set.copyOf(entryIssues.keySet()); }
    public boolean isEntryValid(@NotNull String entryId) { return !entryIssues.containsKey(entryId); }
    public boolean canActivate() { return globalIssues().isEmpty(); }
    public boolean canSave(@NotNull LootPoolConfiguration configuration) {
        return canActivate() && configuration.getEntries().stream()
                .noneMatch(entry -> entry.isEnabled() && !isEntryValid(entry.getId()));
    }
}
