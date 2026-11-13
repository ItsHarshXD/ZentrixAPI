package dev.itsharshxd.zentrix.api.gui.background;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What a {@link MenuBackgroundResolver} decided for one menu.
 *
 * <p>Glyphs are written as in {@code guis.yml}: a name from {@code backgrounds.glyphs}, the
 * characters themselves (a unicode escape written as text works too), or {@code none}. They get the
 * shared {@code backgrounds.color} and {@code backgrounds.reset}, exactly like a rule's glyphs, so
 * an add-on can ship textures that server owners place with their own pack's spacing.
 *
 * @since 1.15.0
 */
public final class MenuBackgroundChoice {

    private enum Kind { KEEP, NONE, GLYPHS }

    private static final MenuBackgroundChoice KEEP = new MenuBackgroundChoice(Kind.KEEP, null, null);
    private static final MenuBackgroundChoice NONE = new MenuBackgroundChoice(Kind.NONE, null, null);

    private final Kind kind;
    private final String glyphs;
    private final Boolean nameShown;

    private MenuBackgroundChoice(Kind kind, String glyphs, Boolean nameShown) {
        this.kind = kind;
        this.glyphs = glyphs;
        this.nameShown = nameShown;
    }

    /** Leaves the decision to the next resolver, and in the end to {@code guis.yml}. */
    @NotNull
    public static MenuBackgroundChoice keep() {
        return KEEP;
    }

    /** No texture: the menu shows its plain name. */
    @NotNull
    public static MenuBackgroundChoice none() {
        return NONE;
    }

    /**
     * A texture of the resolver's own, with the menu name shown or hidden as
     * {@code backgrounds.text} says.
     *
     * @param glyphs a palette name, the characters themselves, or {@code none}
     */
    @NotNull
    public static MenuBackgroundChoice glyphs(@NotNull String glyphs) {
        return new MenuBackgroundChoice(Kind.GLYPHS, Objects.requireNonNull(glyphs, "glyphs"), null);
    }

    /**
     * A texture of the resolver's own.
     *
     * @param glyphs    a palette name, the characters themselves, or {@code none}
     * @param nameShown whether the menu name follows the texture
     */
    @NotNull
    public static MenuBackgroundChoice glyphs(@NotNull String glyphs, boolean nameShown) {
        return new MenuBackgroundChoice(Kind.GLYPHS, Objects.requireNonNull(glyphs, "glyphs"), nameShown);
    }

    /** Whether this leaves the decision to others. */
    public boolean isKeep() {
        return kind == Kind.KEEP;
    }

    /** Whether this removes the texture. */
    public boolean isNone() {
        return kind == Kind.NONE;
    }

    /** The glyphs of a texture of the resolver's own. */
    @NotNull
    public Optional<String> getGlyphs() {
        return Optional.ofNullable(glyphs);
    }

    /**
     * Whether the menu name follows the texture: true or false when the resolver said, null to
     * follow {@code backgrounds.text}.
     */
    @Nullable
    public Boolean getNameShown() {
        return nameShown;
    }

    @Override
    public String toString() {
        return switch (kind) {
            case KEEP -> "keep";
            case NONE -> "none";
            case GLYPHS -> "glyphs(" + glyphs + (nameShown == null ? "" : ", name " + (nameShown ? "shown" : "hidden")) + ")";
        };
    }
}
