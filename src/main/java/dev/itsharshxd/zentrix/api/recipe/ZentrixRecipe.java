package dev.itsharshxd.zentrix.api.recipe;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom recipe registered with Zentrix.
 * <p>
 * Use {@link RecipeBuilder} to create recipes; this interface exposes their
 * configuration.
 * </p>
 *
 * <h2>Craft limits</h2>
 * <p>
 * Craft limits restrict the total number of crafts in a world or game, <b>not
 * per player</b>:
 * <ul>
 *   <li><b>One-time:</b> One player total can craft it per world</li>
 *   <li><b>Craft limit (e.g., 9):</b> Nine total crafts per world</li>
 *   <li><b>Unlimited:</b> Players can craft it without a limit</li>
 * </ul>
 * </p>
 *
 * <h2>Recipe types</h2>
 * <ul>
 *   <li>{@link RecipeType#SHAPED} - Ingredients must be in specific positions</li>
 *   <li>{@link RecipeType#SHAPELESS} - Ingredients can be in any position</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * RecipeService recipeService = api.getRecipeService();
 * Optional<ZentrixRecipe> recipe = recipeService.getRecipe("my-recipe");
 *
 * recipe.ifPresent(r -> {
 *     System.out.println("Recipe: " + r.getId());
 *     System.out.println("Result: " + r.getResult().getType());
 *     System.out.println("Type: " + r.getType());
 *
 *     if (r.isOneTime()) {
 *         System.out.println("This recipe can only be crafted once per player!");
 *     }
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.1
 * @see RecipeService
 * @see RecipeBuilder
 */
public interface ZentrixRecipe {
    /**
     * Available recipe types.
     */
    enum RecipeType {
        /**
         * Shaped recipe - ingredients must be in specific grid positions.
         */
        SHAPED,

        /**
         * Shapeless recipe - ingredients can be placed anywhere.
         */
        SHAPELESS,
    }

    /**
     * Gets the unique identifier of this recipe.
     * <p>
     * Recipe IDs are lowercase and contain only alphanumeric characters,
     * hyphens, and underscores.
     * </p>
     *
     * @return The recipe ID (e.g., "diamond-sword-upgrade")
     */
    @NotNull
    String getId();

    /**
     * Gets the result item that this recipe produces.
     * <p>
     * The returned ItemStack is a clone and can be safely modified.
     * </p>
     *
     * @return A clone of the result ItemStack
     */
    @NotNull
    ItemStack getResult();

    /**
     * Gets the type of this recipe.
     *
     * @return The recipe type (SHAPED or SHAPELESS)
     */
    @NotNull
    RecipeType getType();

    /**
     * Checks if this is a shaped recipe.
     *
     * @return true if this is a shaped recipe
     */
    default boolean isShaped() {
        return getType() == RecipeType.SHAPED;
    }

    /**
     * Checks if this is a shapeless recipe.
     *
     * @return true if this is a shapeless recipe
     */
    default boolean isShapeless() {
        return getType() == RecipeType.SHAPELESS;
    }

    /**
     * Gets the ingredients required for this recipe.
     * <p>
     * For shaped recipes, the list represents the 3x3 crafting grid
     * in row-major order (indices 0-2 are top row, 3-5 middle, 6-8 bottom).
     * Empty slots are represented as null or AIR items.
     * </p>
     * <p>
     * For shapeless recipes, the list contains only the required ingredients
     * without position information.
     * </p>
     * <p>
     * The returned ItemStacks are clones and can be safely modified.
     * </p>
     *
     * @return List of ingredient ItemStacks
     */
    @NotNull
    List<ItemStack> getIngredients();

    /**
     * Gets the crafting pattern for shaped recipes.
     * <p>
     * Returns a 3-element array where each string represents a row
     * of the crafting grid. Characters in the pattern map to ingredients.
     * </p>
     * <p>
     * For shapeless recipes, this returns an empty array.
     * </p>
     *
     * @return The pattern array, or empty array for shapeless recipes
     */
    @NotNull
    String[] getPattern();

    /**
     * Checks if this recipe is a one-time recipe.
     * <p>
     * <b>Note:</b> One-time means one player total can craft this recipe per
     * world or game, not once per player.
     * </p>
     * <p>
     * Example: If Player A crafts a one-time recipe, Player B cannot
     * craft it in the same world/game.
     * </p>
     *
     * @return true if this is a one-time recipe (1 total craft per world)
     */
    boolean isOneTime();

    /**
     * Gets the global craft limit for this recipe.
     * <p>
     * <b>Note:</b> The craft limit is global per world or game, not per player.
     * It restricts the total number of crafts by all players in that world.
     * </p>
     * <p>
     * Example: A craft limit of 9 allows nine total crafts
     * in the world, regardless of which players craft it.
     * </p>
     *
     * @return The global craft limit, or -1 if unlimited
     */
    int getCraftLimit();

    /**
     * Checks if this recipe has a global craft limit.
     * <p>
     * <b>Note:</b> Craft limits apply globally per world, not per player.
     * </p>
     *
     * @return true if this recipe has a global craft limit (> 0)
     */
    default boolean hasCraftLimit() {
        return getCraftLimit() > 0;
    }

    /**
     * Gets the name of the player who created this recipe.
     *
     * @return The creator's name, or empty if unknown
     */
    @NotNull
    Optional<String> getCreator();

    /**
     * Gets the creation timestamp of this recipe.
     *
     * @return The creation time as ISO-8601 string, or empty if unknown
     */
    @NotNull
    Optional<String> getCreationTime();

    /**
     * Checks if this recipe has a custom metadata field.
     *
     * @param key The field key
     * @return true if the field exists
     */
    boolean hasCustomField(@NotNull String key);

    /**
     * Gets a custom metadata field value.
     * <p>
     * Custom fields can store additional recipe metadata.
     * </p>
     *
     * @param key The field key
     * @return The field value, or null if not found
     */
    @Nullable
    Object getCustomField(@NotNull String key);

    /**
     * Gets a custom metadata field value with type casting.
     *
     * @param key  The field key
     * @param type The expected type class
     * @param <T>  The expected type
     * @return The field value cast to the type, or null if not found or wrong type
     */
    @Nullable
    <T> T getCustomField(@NotNull String key, @NotNull Class<T> type);

    /**
     * Gets all custom metadata fields.
     * <p>
     * The returned map is a copy and modifications won't affect the recipe.
     * </p>
     *
     * @return Map of all custom fields
     */
    @NotNull
    Map<String, Object> getCustomFields();

    /**
     * Gets the number of items produced by this recipe.
     *
     * @return The result amount (usually matches result ItemStack amount)
     */
    default int getResultAmount() {
        return getResult().getAmount();
    }
}
