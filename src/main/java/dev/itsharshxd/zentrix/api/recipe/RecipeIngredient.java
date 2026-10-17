package dev.itsharshxd.zentrix.api.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One ingredient of a recipe, with every item it accepts.
 *
 * <p>{@link ZentrixRecipe#getIngredients()} shows one representative item per ingredient; this
 * keeps them all. The items are copies.
 *
 * @param key     the pattern character of a shaped crafting ingredient; {@code null} otherwise
 * @param matching how the recipe compares an offered item with {@link #options()}
 * @param options the items the ingredient accepts; for {@link Matching#MATERIAL}, one item per
 *                accepted material
 * @since 1.8.0
 */
public record RecipeIngredient(
        @Nullable Character key, @NotNull Matching matching, @NotNull List<ItemStack> options) {

    public RecipeIngredient {
        Objects.requireNonNull(matching, "matching");
        List<ItemStack> copies = new ArrayList<>();
        if (options != null) {
            for (ItemStack option : options) {
                if (option != null) {
                    copies.add(option.clone());
                }
            }
        }
        options = List.copyOf(copies);
    }

    /** The pattern character, for a shaped crafting ingredient. */
    @NotNull
    public Optional<Character> keyIfShaped() {
        return Optional.ofNullable(key);
    }

    /** Fresh copies of the accepted items, safe to change. */
    @NotNull
    @Override
    public List<ItemStack> options() {
        List<ItemStack> copies = new ArrayList<>(options.size());
        for (ItemStack option : options) {
            copies.add(option.clone());
        }
        return copies;
    }

    /** How an offered item is compared with an ingredient's items. */
    public enum Matching {
        /** It must be one of the items, the way Zentrix's recipe editors save them. */
        ITEM,
        /** Only its material has to be one of the items' materials. */
        MATERIAL,
        /** Decided by another plugin's ingredient; {@link #options()} shows what it displays as. */
        OTHER
    }
}
