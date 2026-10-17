package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A kind of cosmetic players own and choose from.
 *
 * @since 1.8.0
 */
public enum CosmeticCategory {
    /** The celebration played when the player wins. */
    VICTORY_EFFECTS("victory-effects"),
    /** The glider the player flies into the map with. */
    GLIDERS("gliders");

    private final String id;

    CosmeticCategory(String id) {
        this.id = id;
    }

    /** The ID used in commands, such as {@code victory-effects}. */
    @NotNull
    public String getId() {
        return id;
    }

    /** The category an ID names, ignoring case. */
    @NotNull
    public static Optional<CosmeticCategory> fromId(@Nullable String id) {
        if (id == null) {
            return Optional.empty();
        }
        String lower = id.toLowerCase(Locale.ROOT);
        for (CosmeticCategory category : values()) {
            if (category.id.equals(lower)) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }
}
