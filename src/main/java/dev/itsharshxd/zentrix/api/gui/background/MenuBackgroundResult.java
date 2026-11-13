package dev.itsharshxd.zentrix.api.gui.background;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The texture one menu wears for one viewer, and how it was chosen.
 *
 * <p>The menu's title is the {@link #texture() texture} followed by the menu name, unless the
 * name is hidden. Variant and layer numbers count the rules of the menu's {@code background}
 * block in {@code guis.yml} from 0, in the order written; when an add-on
 * {@link MenuBackgroundResolver resolver} chose the texture they describe what the rules would
 * have chosen.
 *
 * @param chosenBy     the name of the plugin whose resolver chose the texture, or null when the
 *                     rules in {@code guis.yml} did
 * @param rulesApplied whether the menu has rules in {@code guis.yml} and their {@code when}
 *                     allowed them to run
 * @param variant      the variant the rules chose, or -1 when none matched
 * @param layers       the layers the rules added, in drawing order
 * @param texture      the glyphs in front of the menu name, coloured and followed by their reset;
 *                     empty when the menu wears no texture
 * @param nameShown    whether the menu name follows the texture
 * @param title        the full title: the texture and, when shown, the name
 * @since 1.15.0
 */
public record MenuBackgroundResult(
        @Nullable String chosenBy,
        boolean rulesApplied,
        int variant,
        @NotNull List<Integer> layers,
        @NotNull String texture,
        boolean nameShown,
        @NotNull String title) {

    public MenuBackgroundResult {
        layers = List.copyOf(Objects.requireNonNull(layers, "layers"));
        Objects.requireNonNull(texture, "texture");
        Objects.requireNonNull(title, "title");
    }

    /** Whether the menu wears a texture at all. */
    public boolean hasTexture() {
        return !texture.isEmpty();
    }

    /** Whether an add-on resolver, rather than {@code guis.yml}, chose the texture. */
    public boolean isChosenByAddon() {
        return chosenBy != null;
    }

    /** The plugin whose resolver chose the texture, when one did. */
    @NotNull
    public Optional<String> chooser() {
        return Optional.ofNullable(chosenBy);
    }
}
