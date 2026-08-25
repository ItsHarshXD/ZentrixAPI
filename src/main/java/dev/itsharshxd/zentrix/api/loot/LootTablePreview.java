package dev.itsharshxd.zentrix.api.loot;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/** Parsed or sampled entries from one local or native loot table. */
public record LootTablePreview(
        @NotNull List<LootEntry> entries,
        @NotNull Optional<String> failureDetail) {

    public LootTablePreview {
        entries = entries.stream().map(LootEntry::copy).toList();
        failureDetail = failureDetail == null ? Optional.empty() : failureDetail;
    }

    @Override public List<LootEntry> entries() {
        return entries.stream().map(LootEntry::copy).toList();
    }

    public boolean success() { return failureDetail.isEmpty(); }
}
