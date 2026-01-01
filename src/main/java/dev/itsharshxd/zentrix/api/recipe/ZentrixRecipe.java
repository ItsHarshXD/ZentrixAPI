package dev.itsharshxd.zentrix.api.recipe;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a custom recipe registered with Zentrix.
 * <p>
 * This interface provides read-only access to recipe information.
 * To create new recipes, use {@link RecipeBuilder}.
 * </p>
 *
 * <h2>IMPORTANT: Craft Limits are GLOBAL per World!</h2>
 * <p>
 * Craft limits restrict how many TOTAL times a recipe can be crafted
 * in a world/game, <b>not per player</b>:
 * <ul>
 *   <li><b>One-time:</b> Only 1 player total can craft it per world</li>
 *   <li><b>Craft limit (e.g., 9):</b> Only 9 total crafts per world (any players)</li>
 *   <li><b>Unlimited:</b> Any number of players can craft unlimited times</li>
 * </ul>
 * </p>
 *
 * <h2>Recipe Types</h2>
 * <ul>
 *   <li>{@link RecipeType#SHAPED} - Ingredients must be in specific positions</li>
 *   <li>{@link RecipeType#SHAPELESS} - Ingredients can be in any position</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
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
     * Recipe type enumeration.
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
     * <b>IMPORTANT:</b> One-time means only 1 player TOTAL can craft this
     * recipe per world/game, not once per player!
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
     * Gets the GLOBAL craft limit for this recipe.
     * <p>
     * <b>IMPORTANT:</b> The craft limit is GLOBAL per world/game, not per player!
     * This restricts how many TOTAL times the recipe can be crafted
     * in a world by ANY players combined.
     * </p>
     * <p>
     * Example: If craft limit is 9, only 9 total crafts are allowed
     * in the world, regardless of which players craft it.
     * </p>
     *
     * @return The GLOBAL craft limit, or -1 if unlimited
     */
    int getCraftLimit();

    /**
     * Checks if this recipe has a GLOBAL craft limit.
     * <p>
     * <b>Note:</b> Craft limits are GLOBAL per world, not per player!
     * </p>
     *
     * @return true if this recipe has a GLOBAL craft limit (> 0)
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
