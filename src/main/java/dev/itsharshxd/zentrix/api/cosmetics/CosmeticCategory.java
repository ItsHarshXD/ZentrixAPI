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
    GLIDERS("gliders"),
    /**
     * The battle class the player starts every match with, unless they pick another in the
     * lobby. IDs are the class IDs of {@code content/classes.yml}, such as {@code warrior}.
     *
     * <p>A class is unlocked by owning it or by holding its permission; a class without a
     * permission is open to everyone. Unlike other categories, a class can be selected without
     * being owned, since its permission may unlock it: the selection is only a preference, and a
     * class that is locked when a match starts is replaced by the server's default class.
     *
     * @since 1.9.0
     */
    CLASSES("classes"),
    /**
     * The design of the cage around the player's Cornucopia podium. Not part of the Free edition.
     *
     * @since 1.11.0
     */
    CAGE_DESIGNS("cage-designs");

    private final String id;

    CosmeticCategory(String id) {
        this.id = id;
    }

    /** The ID used in commands, such as {@code victory-effects}. */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * The key this category has among every cosmetic type, such as {@code zentrix:gliders}.
     *
     * @since 1.11.0
     */
    @NotNull
    public CosmeticTypeKey key() {
        return CosmeticTypeKey.of(this);
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
