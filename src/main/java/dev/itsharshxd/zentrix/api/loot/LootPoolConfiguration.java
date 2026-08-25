package dev.itsharshxd.zentrix.api.loot;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Mutable, detached editor snapshot of one loot pool. */
public final class LootPoolConfiguration {

    private final List<LootEntry> entries;
    private final EnumSet<LootContainerType> eligibleContainerTypes;

    public LootPoolConfiguration(@NotNull List<LootEntry> entries) {
        this(entries, EnumSet.allOf(LootContainerType.class));
    }

    public LootPoolConfiguration(
            @NotNull List<LootEntry> entries,
            @NotNull Set<LootContainerType> eligibleContainerTypes) {
        this.entries = new ArrayList<>();
        entries.stream().filter(java.util.Objects::nonNull)
                .map(LootEntry::copy).forEach(this.entries::add);
        this.eligibleContainerTypes = eligibleContainerTypes.isEmpty()
                ? EnumSet.noneOf(LootContainerType.class)
                : EnumSet.copyOf(eligibleContainerTypes);
    }

    @NotNull public static LootPoolConfiguration empty() {
        return new LootPoolConfiguration(List.of());
    }

    @NotNull public LootPoolConfiguration copy() {
        return new LootPoolConfiguration(entries, eligibleContainerTypes);
    }

    /** Mutable editor list owned only by this detached configuration. */
    @NotNull public List<LootEntry> getEntries() { return entries; }

    @Nullable
    public LootEntry findEntry(@NotNull String id) {
        return entries.stream().filter(entry -> entry.getId().equals(id)).findFirst().orElse(null);
    }

    @NotNull
    public EnumSet<LootContainerType> getEligibleContainerTypes() {
        return eligibleContainerTypes.isEmpty()
                ? EnumSet.noneOf(LootContainerType.class)
                : EnumSet.copyOf(eligibleContainerTypes);
    }

    public void setContainerTypeEligible(@NotNull LootContainerType type, boolean eligible) {
        if (eligible) eligibleContainerTypes.add(type); else eligibleContainerTypes.remove(type);
    }

    public boolean isContainerTypeEligible(@NotNull LootContainerType type) {
        return eligibleContainerTypes.contains(type);
    }
}
