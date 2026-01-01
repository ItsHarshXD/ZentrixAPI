package dev.itsharshxd.zentrix.api.recipe;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Service for managing custom Zentrix recipes.
 * <p>
 * This service allows addon developers to create, register, and manage
 * custom crafting recipes that integrate with the Zentrix game system.
 * </p>
 *
 * <h2>Features</h2>
 * <ul>
 *   <li>Register shaped and shapeless recipes</li>
 *   <li>Set craft limits (GLOBAL per world/game, not per player!)</li>
 *   <li>Query existing recipes</li>
 *   <li>Remove recipes programmatically</li>
 *   <li>Check global craft availability</li>
 *   <li>Batch operations for multiple recipes</li>
 * </ul>
 *
 * <h2>IMPORTANT: Craft Limits are GLOBAL per World!</h2>
 * <p>
 * Craft limits restrict how many TOTAL times a recipe can be crafted
 * in a world/game, not per player:
 * <ul>
 *   <li><b>One-time ({@code oneTime()}):</b> Only 1 player total can craft it per world</li>
 *   <li><b>Craft limit ({@code craftLimit(9)}):</b> Only 9 total crafts per world (any players)</li>
 *   <li><b>Unlimited:</b> Any number of players can craft unlimited times</li>
 * </ul>
 * </p>
 *
 * <h2>Creating and Registering a Recipe</h2>
 * <pre>{@code
 * RecipeService recipeService = api.getRecipeService();
 *
 * // Create a shaped recipe
 * RecipeBuilder builder = new RecipeBuilder()
 *     .id("super-pickaxe")
 *     .shaped()
 *     .result(new ItemStack(Material.DIAMOND_PICKAXE))
 *     .pattern("DDD", " S ", " S ")
 *     .ingredient('D', new ItemStack(Material.DIAMOND))
 *     .ingredient('S', new ItemStack(Material.STICK))
 *     .craftLimit(1);
 *
 * // Register it
 * boolean success = recipeService.registerRecipe(builder);
 * }</pre>
 *
 * <h2>Querying Recipes</h2>
 * <pre>{@code
 * // Get all recipes
 * Collection<ZentrixRecipe> recipes = recipeService.getAllRecipes();
 *
 * // Find a specific recipe
 * Optional<ZentrixRecipe> recipe = recipeService.getRecipe("super-pickaxe");
 *
 * // Check if recipe exists
 * boolean exists = recipeService.recipeExists("super-pickaxe");
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.1
 * @see RecipeBuilder
 * @see ZentrixRecipe
 */
public interface RecipeService {
    // ==========================================
    // Recipe Registration
    // ==========================================

    /**
     * Registers a new recipe from a builder.
     * <p>
     * The builder must have all required fields set (ID, result, and ingredients).
     * If a recipe with the same ID already exists, registration will fail.
     * </p>
     *
     * @param builder The recipe builder with configuration
     * @return true if registered successfully, false if failed (e.g., duplicate ID)
     * @throws IllegalArgumentException if the builder is invalid
     */
    boolean registerRecipe(@NotNull RecipeBuilder builder);

    /**
     * Registers a new recipe and saves it to file asynchronously.
     * <p>
     * This method performs the same registration as {@link #registerRecipe(RecipeBuilder)}
     * but also persists the recipe to disk so it survives server restarts.
     * </p>
     * <p>
     * The returned CompletableFuture completes only after both registration AND
     * file saving have completed (or failed).
     * </p>
     *
     * @param builder The recipe builder with configuration
     * @return CompletableFuture that completes with true if successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    @NotNull
    CompletableFuture<Boolean> registerRecipeAsync(
        @NotNull RecipeBuilder builder
    );

    /**
     * Registers multiple recipes at once.
     * <p>
     * This is more efficient than calling {@link #registerRecipe(RecipeBuilder)}
     * multiple times, especially for addons with many recipes.
     * </p>
     *
     * @param builders The recipe builders to register
     * @return Number of recipes successfully registered
     * @throws IllegalArgumentException if any builder is invalid
     */
    int registerRecipes(@NotNull Collection<RecipeBuilder> builders);

    /**
     * Registers multiple recipes and saves them to file asynchronously.
     *
     * @param builders The recipe builders to register
     * @return CompletableFuture that completes with the number of successful registrations
     */
    @NotNull
    CompletableFuture<Integer> registerRecipesAsync(
        @NotNull Collection<RecipeBuilder> builders
    );

    /**
     * Unregisters a recipe by its ID.
     * <p>
     * This removes the recipe from the server but does NOT delete the file.
     * Use {@link #unregisterRecipeAndDelete(String)} to also remove from disk.
     * </p>
     *
     * @param recipeId The recipe ID to unregister
     * @return true if the recipe was found and unregistered
     */
    boolean unregisterRecipe(@NotNull String recipeId);

    /**
     * Unregisters a recipe and deletes it from disk.
     * <p>
     * This completely removes the recipe, including the saved file.
     * The returned CompletableFuture completes only after both unregistration
     * AND file deletion have completed (or failed).
     * </p>
     *
     * @param recipeId The recipe ID to unregister and delete
     * @return CompletableFuture that completes with true if successful
     */
    @NotNull
    CompletableFuture<Boolean> unregisterRecipeAndDelete(
        @NotNull String recipeId
    );

    /**
     * Unregisters multiple recipes by their IDs.
     *
     * @param recipeIds The recipe IDs to unregister
     * @return Number of recipes successfully unregistered
     */
    int unregisterRecipes(@NotNull Collection<String> recipeIds);

    /**
     * Unregisters multiple recipes and deletes them from disk.
     *
     * @param recipeIds The recipe IDs to unregister and delete
     * @return CompletableFuture that completes with the number of successful deletions
     */
    @NotNull
    CompletableFuture<Integer> unregisterRecipesAndDelete(
        @NotNull Collection<String> recipeIds
    );

    // ==========================================
    // Recipe Modification
    // ==========================================

    /**
     * Updates an existing recipe with new settings.
     * <p>
     * This replaces the existing recipe while preserving craft tracking data.
     * The builder's ID must match an existing recipe.
     * </p>
     *
     * @param builder The recipe builder with updated configuration
     * @return true if the recipe was updated successfully
     * @throws IllegalArgumentException if the builder is invalid or recipe doesn't exist
     */
    boolean updateRecipe(@NotNull RecipeBuilder builder);

    /**
     * Updates an existing recipe and saves changes to file asynchronously.
     *
     * @param builder The recipe builder with updated configuration
     * @return CompletableFuture that completes with true if successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    @NotNull
    CompletableFuture<Boolean> updateRecipeAsync(
        @NotNull RecipeBuilder builder
    );

    // ==========================================
    // Recipe Queries
    // ==========================================

    /**
     * Gets a recipe by its ID.
     *
     * @param recipeId The recipe ID
     * @return Optional containing the recipe, or empty if not found
     */
    @NotNull
    Optional<ZentrixRecipe> getRecipe(@NotNull String recipeId);

    /**
     * Gets all registered recipes.
     * <p>
     * The returned collection is a snapshot and modifications won't
     * affect the actual recipe registry.
     * </p>
     *
     * @return Collection of all registered recipes
     */
    @NotNull
    Collection<ZentrixRecipe> getAllRecipes();

    /**
     * Checks if a recipe with the given ID exists.
     *
     * @param recipeId The recipe ID to check
     * @return true if a recipe with this ID exists
     */
    boolean recipeExists(@NotNull String recipeId);

    /**
     * Gets the total number of registered recipes.
     *
     * @return The recipe count
     */
    int getRecipeCount();

    /**
     * Finds recipes by their result item type.
     * <p>
     * Returns all recipes that produce an item of the specified type.
     * </p>
     *
     * @param result The result item to search for (type is compared)
     * @return Collection of matching recipes
     */
    @NotNull
    Collection<ZentrixRecipe> findRecipesByResult(@NotNull ItemStack result);

    /**
     * Finds recipes created by a specific player.
     *
     * @param creatorName The creator's name
     * @return Collection of recipes created by this player
     */
    @NotNull
    Collection<ZentrixRecipe> findRecipesByCreator(@NotNull String creatorName);

    /**
     * Gets all one-time recipes.
     *
     * @return Collection of one-time recipes
     */
    @NotNull
    Collection<ZentrixRecipe> getOneTimeRecipes();

    /**
     * Gets all recipes with craft limits.
     *
     * @return Collection of recipes that have craft limits
     */
    @NotNull
    Collection<ZentrixRecipe> getLimitedRecipes();

    // ==========================================
    // Craft Limit Checking (GLOBAL per world!)
    // ==========================================

    /**
     * Checks if a recipe can still be crafted in the player's current world.
     * <p>
     * <b>IMPORTANT:</b> Craft limits are GLOBAL per world, not per player!
     * <ul>
     *   <li>One-time recipes: Only 1 player total can craft per world</li>
     *   <li>Limited recipes: Only N total crafts allowed per world</li>
     * </ul>
     * </p>
     *
     * @param player   The player (used to determine world)
     * @param recipeId The recipe ID
     * @return true if the GLOBAL limit hasn't been reached
     */
    boolean canPlayerCraft(@NotNull Player player, @NotNull String recipeId);

    /**
     * Checks if a recipe can still be crafted in the player's current world.
     * <p>
     * <b>IMPORTANT:</b> Craft limits are GLOBAL per world, not per player!
     * </p>
     * <p>
     * <b>Note:</b> If the player is offline (not found by UUID), this method
     * returns false because the world cannot be determined. This does NOT mean
     * the recipe cannot be crafted - use {@link #canCraftInWorld(String, String)}
     * if you know the world name.
     * </p>
     *
     * @param playerId The player's UUID (used to determine world)
     * @param recipeId The recipe ID
     * @return true if the GLOBAL limit hasn't been reached, false if limit reached OR player offline
     */
    boolean canPlayerCraft(@NotNull UUID playerId, @NotNull String recipeId);

    /**
     * Checks if a recipe can still be crafted in a specific world.
     * <p>
     * <b>IMPORTANT:</b> Craft limits are GLOBAL per world, not per player!
     * </p>
     *
     * @param worldName The world name to check
     * @param recipeId  The recipe ID
     * @return true if the GLOBAL limit hasn't been reached
     */
    boolean canCraftInWorld(
        @NotNull String worldName,
        @NotNull String recipeId
    );

    /**
     * Gets how many times a specific player has crafted a recipe in the current game.
     * <p>
     * <b>Note:</b> This is for statistics only! Craft limits are checked globally,
     * not per player. Use {@link #getRemainingCrafts(Player, String)} to check
     * if the recipe can still be crafted.
     * </p>
     *
     * @param player   The player
     * @param recipeId The recipe ID
     * @return The number of times THIS player crafted it (for stats)
     */
    int getPlayerCraftCount(@NotNull Player player, @NotNull String recipeId);

    /**
     * Gets how many times a specific player has crafted a recipe in the current game.
     * <p>
     * <b>Note:</b> This is for statistics only! Craft limits are checked globally.
     * Returns 0 if the player is offline.
     * </p>
     *
     * @param playerId The player's UUID
     * @param recipeId The recipe ID
     * @return The number of times THIS player crafted it (for stats), 0 if player offline
     */
    int getPlayerCraftCount(@NotNull UUID playerId, @NotNull String recipeId);

    /**
     * Gets the GLOBAL craft count for a recipe in a specific world.
     * <p>
     * This returns the TOTAL number of times ANY player has crafted this
     * recipe in the specified world.
     * </p>
     *
     * @param worldName The world name
     * @param recipeId  The recipe ID
     * @return Total crafts in this world by all players
     */
    int getGlobalCraftCount(
        @NotNull String worldName,
        @NotNull String recipeId
    );

    /**
     * Gets the remaining GLOBAL crafts for a recipe in the player's world.
     * <p>
     * <b>IMPORTANT:</b> This returns the GLOBAL remaining count, not per-player!
     * <ul>
     *   <li>One-time: Returns 1 if not crafted, 0 if someone crafted it</li>
     *   <li>Limited (e.g., 9): Returns 9 minus total crafts by ALL players</li>
     *   <li>Unlimited: Returns -1</li>
     * </ul>
     * </p>
     *
     * @param player   The player (used to determine world)
     * @param recipeId The recipe ID
     * @return GLOBAL remaining crafts, -1 if unlimited, 0 if limit reached
     */
    int getRemainingCrafts(@NotNull Player player, @NotNull String recipeId);

    /**
     * Gets the remaining GLOBAL crafts for a recipe in the player's world.
     * <p>
     * <b>IMPORTANT:</b> This returns the GLOBAL remaining count, not per-player!
     * </p>
     * <p>
     * <b>Note:</b> If the player is offline, returns -1 (treated as unlimited/unknown).
     * Use {@link #getRemainingCraftsInWorld(String, String)} if you know the world name.
     * </p>
     *
     * @param playerId The player's UUID (used to determine world)
     * @param recipeId The recipe ID
     * @return GLOBAL remaining crafts, -1 if unlimited or player offline
     */
    int getRemainingCrafts(@NotNull UUID playerId, @NotNull String recipeId);

    /**
     * Gets the remaining GLOBAL crafts for a recipe in a specific world.
     *
     * @param worldName The world name
     * @param recipeId  The recipe ID
     * @return GLOBAL remaining crafts, -1 if unlimited
     */
    int getRemainingCraftsInWorld(
        @NotNull String worldName,
        @NotNull String recipeId
    );

    /**
     * Checks if a player has ever crafted a specific recipe (for first-time bonus).
     * <p>
     * <b>Note:</b> This is for the first-time crafting bonus tracking,
     * not for one-time recipe limits. One-time limits are checked globally.
     * </p>
     *
     * @param playerId The player's UUID
     * @param recipeId The recipe ID
     * @return true if this player has crafted this recipe at least once
     */
    boolean hasPlayerEverCrafted(
        @NotNull UUID playerId,
        @NotNull String recipeId
    );

    /**
     * @deprecated Use {@link #hasPlayerEverCrafted(UUID, String)} instead.
     *             The old name was misleading as it suggested relation to one-time recipes.
     */
    @Deprecated(since = "1.0.2", forRemoval = true)
    default boolean hasPlayerCraftedOneTime(
        @NotNull UUID playerId,
        @NotNull String recipeId
    ) {
        return hasPlayerEverCrafted(playerId, recipeId);
    }

    // ==========================================
    // Recipe Information
    // ==========================================

    /**
     * Gets the IDs of all registered recipes.
     *
     * @return Collection of recipe IDs
     */
    @NotNull
    Collection<String> getRecipeIds();

    /**
     * Gets recipes added by a specific addon.
     * <p>
     * Recipes track their source addon via a custom field.
     * </p>
     *
     * @param addonId The addon ID
     * @return Collection of recipes from this addon
     */
    @NotNull
    Collection<ZentrixRecipe> getRecipesByAddon(@NotNull String addonId);

    // ==========================================
    // Persistence
    // ==========================================

    /**
     * Checks if a recipe is persisted to disk.
     *
     * @param recipeId The recipe ID
     * @return true if the recipe file exists on disk
     */
    boolean isRecipePersisted(@NotNull String recipeId);

    /**
     * Saves an existing recipe to disk.
     * <p>
     * Use this to persist a recipe that was registered without saving,
     * or to update the saved version after modifications.
     * </p>
     *
     * @param recipeId The recipe ID to save
     * @return CompletableFuture that completes with true if saved successfully
     */
    @NotNull
    CompletableFuture<Boolean> saveRecipe(@NotNull String recipeId);

    // ==========================================
    // Utility Methods
    // ==========================================

    /**
     * Reloads all recipes from disk.
     * <p>
     * This clears the recipe registry and reloads from files.
     * </p>
     */
    void reloadRecipes();

    /**
     * Checks if the recipe system is available and ready.
     *
     * @return true if recipes can be registered and queried
     */
    boolean isAvailable();

    /**
     * Creates a new RecipeBuilder instance.
     * <p>
     * Convenience method for creating builders.
     * </p>
     *
     * @return A new RecipeBuilder
     */
    @NotNull
    default RecipeBuilder createBuilder() {
        return new RecipeBuilder();
    }

    /**
     * Creates a new RecipeBuilder with the specified ID.
     *
     * @param id The recipe ID
     * @return A new RecipeBuilder with the ID set
     */
    @NotNull
    default RecipeBuilder createBuilder(@NotNull String id) {
        return new RecipeBuilder().id(id);
    }

    // ==========================================
    // Cleanup Methods
    // ==========================================

    /**
     * Cleans up craft tracking data for a specific world.
     * <p>
     * This should be called when a game world is deleted to prevent memory leaks.
     * </p>
     *
     * @param worldName The world name to cleanup
     */
    void cleanupWorld(@NotNull String worldName);

    /**
     * Cleans up craft tracking data for a specific player.
     * <p>
     * This can be called when a player leaves the server permanently
     * to free memory.
     * </p>
     *
     * @param playerId The player's UUID to cleanup
     */
    void cleanupPlayer(@NotNull UUID playerId);
}
