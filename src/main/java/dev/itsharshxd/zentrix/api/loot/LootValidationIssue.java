package dev.itsharshxd.zentrix.api.loot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/** One locale-addressable loot validation problem. */
public record LootValidationIssue(
        @NotNull String localeKey,
        @NotNull Optional<String> entryId,
        boolean global,
        @NotNull Map<String, Object> placeholders) {

    public LootValidationIssue {
        entryId = entryId == null ? Optional.empty() : entryId;
        placeholders = placeholders == null
                ? Map.of()
                : Map.copyOf(new LinkedHashMap<>(placeholders));
    }
}
