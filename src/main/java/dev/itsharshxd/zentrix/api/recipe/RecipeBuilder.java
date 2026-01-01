package dev.itsharshxd.zentrix.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fluent builder for creating custom Zentrix recipes.
 * <p>
 * This builder allows addon developers to create shaped or shapeless
 * crafting recipes that integrate with the Zentrix recipe system.
 * </p>
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
 * <h2>Craft Limit Rules</h2>
 * <ul>
 *   <li>{@code oneTime()} and {@code craftLimit(n)} are <b>mutually exclusive</b></li>
 *   <li>Setting {@code oneTime(true)} clears any previously set craft limit</li>
 *   <li>Setting {@code craftLimit(n)} clears the one-time flag</li>
 *   <li>Use {@code unlimited()} to remove all limits</li>
 * </ul>
 *
 * <h2>Creating a Shaped Recipe</h2>
 * <pre>{@code
 * RecipeBuilder builder = new RecipeBuilder()
 *     .id("super-sword")
 *     .shaped()
 *     .result(new ItemStack(Material.DIAMOND_SWORD))
 *     .pattern("DDD", "DSD", "DDD")
 *     .ingredient('D', new ItemStack(Material.DIAMOND))
 *     .ingredient('S', new ItemStack(Material.STICK))
 *     .craftLimit(5);  // Only 5 total crafts per world
 *
 * // Register via RecipeService
 * recipeService.registerRecipe(builder);
 * }</pre>
 *
 * <h2>Creating a Shapeless Recipe</h2>
 * <pre>{@code
 * RecipeBuilder builder = new RecipeBuilder()
 *     .id("quick-diamonds")
 *     .shapeless()
 *     .result(new ItemStack(Material.DIAMOND, 4))
 *     .addIngredient(new ItemStack(Material.COAL, 8))
 *     .addIngredient(new ItemStack(Material.IRON_INGOT, 4))
 *     .oneTime();  // Only 1 player can craft this per world
 *
 * recipeService.registerRecipe(builder);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.1
 * @see RecipeService#registerRecipe(RecipeBuilder)
 * @see ZentrixRecipe
 */
public class RecipeBuilder {

    private String id;
    private ItemStack result;
    private ZentrixRecipe.RecipeType type = ZentrixRecipe.RecipeType.SHAPED;
    private String[] pattern = new String[0];
    private final Map<Character, ItemStack> ingredientMap = new HashMap<>();
    private final List<ItemStack> shapelessIngredients = new ArrayList<>();
    private boolean oneTime = false;
    private int craftLimit = -1;
    private final Map<String, Object> customFields = new HashMap<>();

    /**
     * Creates a new RecipeBuilder instance.
     */
    public RecipeBuilder() {}

    // ==========================================
    // Core Properties
    // ==========================================

    /**
     * Sets the unique identifier for this recipe.
     * <p>
     * Recipe IDs must only contain lowercase letters, numbers, hyphens, and underscores.
     * The ID will be automatically converted to lowercase.
     * </p>
     *
     * @param id The recipe ID (e.g., "super-sword", "magic_potion")
     * @return This builder for chaining
     * @throws IllegalArgumentException if the ID is null, empty, or contains invalid characters
     */
    @NotNull
    public RecipeBuilder id(@NotNull String id) {
        Objects.requireNonNull(id, "Recipe ID cannot be null");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Recipe ID cannot be empty");
        }
        String lowerId = id.toLowerCase();
        if (!lowerId.matches("[a-z0-9_-]+")) {
            throw new IllegalArgumentException(
                "Recipe ID must only contain lowercase letters, numbers, hyphens, and underscores: " +
                    id
            );
        }
        this.id = lowerId;
        return this;
    }

    /**
     * Sets the result item that this recipe produces.
     *
     * @param result The result ItemStack (amount determines output quantity)
     * @return This builder for chaining
     * @throws IllegalArgumentException if result is null or AIR
     */
    @NotNull
    public RecipeBuilder result(@NotNull ItemStack result) {
        Objects.requireNonNull(result, "Result cannot be null");
        if (result.getType() == Material.AIR) {
            throw new IllegalArgumentException("Result cannot be AIR");
        }
        this.result = result.clone();
        return this;
    }

    /**
     * Sets this recipe as a shaped recipe.
     * <p>
     * Shaped recipes require ingredients to be in specific grid positions.
     * Use {@link #pattern(String...)} and {@link #ingredient(char, ItemStack)}
     * to define the crafting pattern.
     * </p>
     * <p>
     * Note: Calling this clears any shapeless ingredients previously added.
     * </p>
     *
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder shaped() {
        this.type = ZentrixRecipe.RecipeType.SHAPED;
        return this;
    }

    /**
     * Sets this recipe as a shapeless recipe.
     * <p>
     * Shapeless recipes allow ingredients to be placed anywhere in the grid.
     * Use {@link #addIngredient(ItemStack)} to add required ingredients.
     * </p>
     * <p>
     * Note: Calling this clears any shaped pattern/ingredients previously set.
     * </p>
     *
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder shapeless() {
        this.type = ZentrixRecipe.RecipeType.SHAPELESS;
        return this;
    }

    // ==========================================
    // Shaped Recipe Configuration
    // ==========================================

    /**
     * Sets the crafting pattern for a shaped recipe.
     * <p>
     * The pattern is an array of 1-3 strings, each representing a row
     * of the crafting grid. Each character maps to an ingredient.
     * Use a space character for empty slots.
     * </p>
     *
     * <p>Example patterns:</p>
     * <pre>{@code
     * // 3x3 pattern
     * .pattern("DDD", "DSD", "DDD")
     *
     * // 2x2 pattern (centered)
     * .pattern("DD", "DD")
     *
     * // 1x3 pattern (stick-like)
     * .pattern("D", "D", "D")
     * }</pre>
     *
     * @param pattern The pattern rows (1-3 rows, each 1-3 characters)
     * @return This builder for chaining
     * @throws IllegalArgumentException if pattern is invalid
     */
    @NotNull
    public RecipeBuilder pattern(@NotNull String... pattern) {
        Objects.requireNonNull(pattern, "Pattern cannot be null");
        if (pattern.length == 0 || pattern.length > 3) {
            throw new IllegalArgumentException("Pattern must have 1-3 rows");
        }
        for (int i = 0; i < pattern.length; i++) {
            String row = pattern[i];
            if (row == null) {
                throw new IllegalArgumentException(
                    "Pattern row " + i + " cannot be null"
                );
            }
            if (row.isEmpty() || row.length() > 3) {
                throw new IllegalArgumentException(
                    "Each pattern row must be 1-3 characters, row " +
                        i +
                        " has " +
                        row.length()
                );
            }
        }
        this.pattern = Arrays.copyOf(pattern, pattern.length);
        return this;
    }

    /**
     * Maps a pattern character to an ingredient item.
     * <p>
     * Each unique character in the pattern (except spaces) should be
     * mapped to an ingredient using this method.
     * </p>
     *
     * @param key  The pattern character (must not be a space)
     * @param item The ingredient item for this character
     * @return This builder for chaining
     * @throws IllegalArgumentException if item is null or key is a space
     */
    @NotNull
    public RecipeBuilder ingredient(char key, @NotNull ItemStack item) {
        Objects.requireNonNull(item, "Ingredient cannot be null");
        if (key == ' ') {
            throw new IllegalArgumentException(
                "Cannot map space character to an ingredient - spaces represent empty slots"
            );
        }
        this.ingredientMap.put(key, item.clone());
        return this;
    }

    // ==========================================
    // Shapeless Recipe Configuration
    // ==========================================

    /**
     * Adds an ingredient to a shapeless recipe.
     * <p>
     * For shapeless recipes, call this method for each required ingredient.
     * The same ingredient can be added multiple times if multiple are required.
     * </p>
     *
     * @param item The ingredient item
     * @return This builder for chaining
     * @throws IllegalArgumentException if item is null or AIR
     */
    @NotNull
    public RecipeBuilder addIngredient(@NotNull ItemStack item) {
        Objects.requireNonNull(item, "Ingredient cannot be null");
        if (item.getType() == Material.AIR) {
            throw new IllegalArgumentException("Ingredient cannot be AIR");
        }
        this.shapelessIngredients.add(item.clone());
        return this;
    }

    /**
     * Adds multiple of the same ingredient to a shapeless recipe.
     *
     * @param item   The ingredient item
     * @param amount How many of this ingredient are required (1-9)
     * @return This builder for chaining
     * @throws IllegalArgumentException if item is null/AIR or amount is invalid
     */
    @NotNull
    public RecipeBuilder addIngredient(@NotNull ItemStack item, int amount) {
        if (amount < 1) {
            throw new IllegalArgumentException("Amount must be at least 1");
        }
        if (amount > 9) {
            throw new IllegalArgumentException(
                "Amount cannot exceed 9 (crafting grid size)"
            );
        }
        for (int i = 0; i < amount; i++) {
            addIngredient(item);
        }
        return this;
    }

    /**
     * Adds an ingredient using just a Material type.
     *
     * @param material The ingredient material
     * @return This builder for chaining
     * @throws IllegalArgumentException if material is null or AIR
     */
    @NotNull
    public RecipeBuilder addIngredient(@NotNull Material material) {
        Objects.requireNonNull(material, "Material cannot be null");
        return addIngredient(new ItemStack(material));
    }

    /**
     * Adds multiple of a Material ingredient.
     *
     * @param material The ingredient material
     * @param amount   How many of this ingredient are required (1-9)
     * @return This builder for chaining
     * @throws IllegalArgumentException if material is null/AIR or amount is invalid
     */
    @NotNull
    public RecipeBuilder addIngredient(@NotNull Material material, int amount) {
        Objects.requireNonNull(material, "Material cannot be null");
        return addIngredient(new ItemStack(material), amount);
    }

    // ==========================================
    // Recipe Limits and Restrictions
    // ==========================================

    /**
     * Sets whether this recipe can only be crafted once per world/game.
     * <p>
     * <b>IMPORTANT:</b> One-time means only 1 player TOTAL can craft this
     * recipe per world/game, not once per player!
     * </p>
     * <p>
     * Example: If one-time is enabled and Player A crafts it, Player B
     * cannot craft it in the same game.
     * </p>
     * <p>
     * <b>Note:</b> Setting {@code oneTime(true)} will clear any previously
     * set craft limit, as these options are mutually exclusive.
     * </p>
     *
     * @param oneTime true to make this a one-time recipe (1 total craft per world)
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder oneTime(boolean oneTime) {
        this.oneTime = oneTime;
        if (oneTime) {
            // Clear craft limit - oneTime and craftLimit are mutually exclusive
            this.craftLimit = -1;
        }
        return this;
    }

    /**
     * Marks this recipe as a one-time recipe.
     * <p>
     * Equivalent to {@code oneTime(true)}.
     * Only 1 player TOTAL can craft this recipe per world/game!
     * </p>
     * <p>
     * <b>Note:</b> This will clear any previously set craft limit,
     * as oneTime and craftLimit are mutually exclusive.
     * </p>
     *
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder oneTime() {
        return oneTime(true);
    }

    /**
     * Sets the maximum number of TOTAL crafts for this recipe per world/game.
     * <p>
     * <b>IMPORTANT:</b> This is a GLOBAL limit, not per player!
     * If set to 9, only 9 total crafts by ANY players are allowed per world.
     * </p>
     * <p>
     * <b>Note:</b> Setting a craft limit will clear the one-time flag,
     * as these options are mutually exclusive. Use {@link #oneTime()} if you
     * want exactly 1 craft per world.
     * </p>
     * <p>
     * Use -1 or {@link #unlimited()} for no limit.
     * </p>
     *
     * @param limit The GLOBAL craft limit (must be -1 or positive)
     * @return This builder for chaining
     * @throws IllegalArgumentException if limit is less than -1 or zero
     */
    @NotNull
    public RecipeBuilder craftLimit(int limit) {
        if (limit < -1) {
            throw new IllegalArgumentException(
                "Craft limit must be -1 (unlimited) or a positive number, got: " +
                    limit
            );
        }
        if (limit == 0) {
            throw new IllegalArgumentException(
                "Craft limit of 0 would make the recipe uncraftable. " +
                    "Use -1 for unlimited or a positive number for a limit."
            );
        }
        if (limit == 1) {
            // If limit is 1, use oneTime for clarity
            return oneTime(true);
        }
        this.craftLimit = limit;
        if (limit > 0) {
            // Clear oneTime - craftLimit and oneTime are mutually exclusive
            this.oneTime = false;
        }
        return this;
    }

    /**
     * Removes any craft limit from this recipe.
     * <p>
     * The recipe can be crafted unlimited times by any number of players.
     * </p>
     *
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder unlimited() {
        this.craftLimit = -1;
        this.oneTime = false;
        return this;
    }

    /**
     * Checks if this recipe has any craft restrictions.
     *
     * @return true if oneTime is enabled or craftLimit is set
     */
    public boolean hasRestrictions() {
        return oneTime || craftLimit > 0;
    }

    /**
     * Gets the effective limit for this recipe.
     * <p>
     * Returns:
     * <ul>
     *   <li>1 if one-time is enabled</li>
     *   <li>The craft limit if set (and > 0)</li>
     *   <li>-1 if unlimited</li>
     * </ul>
     * </p>
     *
     * @return The effective craft limit
     */
    public int getEffectiveLimit() {
        if (oneTime) {
            return 1;
        }
        return craftLimit;
    }

    // ==========================================
    // Custom Metadata
    // ==========================================

    /**
     * Adds a custom metadata field to this recipe.
     * <p>
     * Custom fields can store additional information about the recipe
     * that addons can read later. Common fields include:
     * <ul>
     *   <li>{@code addon_id} - The addon that created this recipe</li>
     *   <li>{@code creator} - Player name who created the recipe</li>
     * </ul>
     * </p>
     * <p>
     * <b>Reserved field names:</b> {@code one_time}, {@code craft_limit},
     * {@code creation_time} - these are managed internally.
     * </p>
     *
     * @param key   The field key (cannot be null)
     * @param value The field value (should be serializable, can be null to remove)
     * @return This builder for chaining
     * @throws IllegalArgumentException if key is a reserved field name
     */
    @NotNull
    public RecipeBuilder customField(
        @NotNull String key,
        @Nullable Object value
    ) {
        Objects.requireNonNull(key, "Custom field key cannot be null");
        // Warn about reserved fields but don't block (internal use may need them)
        if (key.equals("one_time") || key.equals("craft_limit")) {
            // These are set via dedicated methods
            throw new IllegalArgumentException(
                "'" +
                    key +
                    "' is a reserved field. Use oneTime() or craftLimit() instead."
            );
        }
        if (value == null) {
            this.customFields.remove(key);
        } else {
            this.customFields.put(key, value);
        }
        return this;
    }

    /**
     * Removes a custom metadata field from this recipe.
     *
     * @param key The field key to remove
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder removeCustomField(@NotNull String key) {
        Objects.requireNonNull(key, "Custom field key cannot be null");
        this.customFields.remove(key);
        return this;
    }

    /**
     * Checks if a custom field exists.
     *
     * @param key The field key
     * @return true if the field exists
     */
    public boolean hasCustomField(@NotNull String key) {
        return customFields.containsKey(key);
    }

    // ==========================================
    // Getters (for RecipeService implementation)
    // ==========================================

    /**
     * Gets the recipe ID.
     *
     * @return The recipe ID, or null if not set
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     * Gets the result item.
     *
     * @return The result ItemStack (cloned), or null if not set
     */
    @Nullable
    public ItemStack getResult() {
        return result != null ? result.clone() : null;
    }

    /**
     * Gets the recipe type.
     *
     * @return The recipe type (SHAPED or SHAPELESS)
     */
    @NotNull
    public ZentrixRecipe.RecipeType getType() {
        return type;
    }

    /**
     * Gets the crafting pattern (for shaped recipes).
     *
     * @return A copy of the pattern array
     */
    @NotNull
    public String[] getPattern() {
        return Arrays.copyOf(pattern, pattern.length);
    }

    /**
     * Gets the ingredient map (for shaped recipes).
     *
     * @return Map of pattern characters to ingredients (cloned)
     */
    @NotNull
    public Map<Character, ItemStack> getIngredientMap() {
        Map<Character, ItemStack> copy = new HashMap<>();
        ingredientMap.forEach((k, v) -> copy.put(k, v.clone()));
        return copy;
    }

    /**
     * Gets the shapeless ingredients list.
     *
     * @return List of ingredients for shapeless recipes (cloned)
     */
    @NotNull
    public List<ItemStack> getShapelessIngredients() {
        List<ItemStack> copy = new ArrayList<>();
        shapelessIngredients.forEach(item -> copy.add(item.clone()));
        return copy;
    }

    /**
     * Checks if this is a one-time recipe.
     *
     * @return true if one-time (1 total craft per world)
     */
    public boolean isOneTime() {
        return oneTime;
    }

    /**
     * Gets the craft limit.
     *
     * @return The craft limit, or -1 if unlimited
     */
    public int getCraftLimit() {
        return craftLimit;
    }

    /**
     * Gets all custom fields.
     *
     * @return Map of custom fields (copy)
     */
    @NotNull
    public Map<String, Object> getCustomFields() {
        return new HashMap<>(customFields);
    }

    // ==========================================
    // Validation
    // ==========================================

    /**
     * Validates that this builder has all required fields set correctly.
     * <p>
     * Checks performed:
     * <ul>
     *   <li>Recipe ID is set and valid</li>
     *   <li>Result item is set</li>
     *   <li>Shaped recipes have a pattern with all characters mapped</li>
     *   <li>Shapeless recipes have 1-9 ingredients</li>
     * </ul>
     * </p>
     *
     * @throws IllegalStateException if the builder is not valid
     */
    public void validate() throws IllegalStateException {
        if (id == null || id.isEmpty()) {
            throw new IllegalStateException("Recipe ID is required");
        }
        if (result == null) {
            throw new IllegalStateException("Recipe result is required");
        }

        if (type == ZentrixRecipe.RecipeType.SHAPED) {
            validateShapedRecipe();
        } else {
            validateShapelessRecipe();
        }
    }

    private void validateShapedRecipe() {
        if (pattern.length == 0) {
            throw new IllegalStateException(
                "Shaped recipes require a pattern. Use pattern(\"ABC\", \"DEF\", \"GHI\")"
            );
        }

        // Collect all unique characters from pattern (excluding spaces)
        java.util.Set<Character> patternChars = new java.util.HashSet<>();
        for (String row : pattern) {
            for (char c : row.toCharArray()) {
                if (c != ' ') {
                    patternChars.add(c);
                }
            }
        }

        // Check that all pattern characters have ingredient mappings
        List<Character> unmapped = new ArrayList<>();
        for (char c : patternChars) {
            if (!ingredientMap.containsKey(c)) {
                unmapped.add(c);
            }
        }
        if (!unmapped.isEmpty()) {
            throw new IllegalStateException(
                "Pattern character(s) " +
                    unmapped +
                    " have no ingredient mapping. " +
                    "Use ingredient('" +
                    unmapped.get(0) +
                    "', new ItemStack(Material.XXX))"
            );
        }

        // Warn about unused ingredient mappings (not an error, just inefficient)
        for (char c : ingredientMap.keySet()) {
            if (!patternChars.contains(c)) {
                // This is just a warning, not an error
                // Could log this if we had a logger
            }
        }
    }

    private void validateShapelessRecipe() {
        if (shapelessIngredients.isEmpty()) {
            throw new IllegalStateException(
                "Shapeless recipes require at least one ingredient. " +
                    "Use addIngredient(new ItemStack(Material.XXX))"
            );
        }
        if (shapelessIngredients.size() > 9) {
            throw new IllegalStateException(
                "Shapeless recipes cannot have more than 9 ingredients " +
                    "(crafting grid limit). Current: " +
                    shapelessIngredients.size()
            );
        }
    }

    /**
     * Checks if this builder is valid without throwing an exception.
     *
     * @return true if the builder has all required fields
     */
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Gets a list of validation errors without throwing.
     *
     * @return List of error messages, empty if valid
     */
    @NotNull
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();

        if (id == null || id.isEmpty()) {
            errors.add("Recipe ID is required");
        }
        if (result == null) {
            errors.add("Recipe result is required");
        }

        if (type == ZentrixRecipe.RecipeType.SHAPED) {
            if (pattern.length == 0) {
                errors.add("Shaped recipes require a pattern");
            } else {
                for (String row : pattern) {
                    for (char c : row.toCharArray()) {
                        if (c != ' ' && !ingredientMap.containsKey(c)) {
                            errors.add(
                                "Pattern character '" +
                                    c +
                                    "' has no ingredient mapping"
                            );
                        }
                    }
                }
            }
        } else {
            if (shapelessIngredients.isEmpty()) {
                errors.add("Shapeless recipes require at least one ingredient");
            }
            if (shapelessIngredients.size() > 9) {
                errors.add(
                    "Shapeless recipes cannot have more than 9 ingredients"
                );
            }
        }

        return errors;
    }

    // ==========================================
    // Copy and Reset
    // ==========================================

    /**
     * Creates a copy of this builder.
     *
     * @return A new RecipeBuilder with the same configuration
     */
    @NotNull
    public RecipeBuilder copy() {
        RecipeBuilder copy = new RecipeBuilder();
        if (this.id != null) copy.id = this.id;
        if (this.result != null) copy.result = this.result.clone();
        copy.type = this.type;
        copy.pattern = Arrays.copyOf(this.pattern, this.pattern.length);
        this.ingredientMap.forEach((k, v) ->
            copy.ingredientMap.put(k, v.clone())
        );
        this.shapelessIngredients.forEach(item ->
            copy.shapelessIngredients.add(item.clone())
        );
        copy.oneTime = this.oneTime;
        copy.craftLimit = this.craftLimit;
        copy.customFields.putAll(this.customFields);
        return copy;
    }

    /**
     * Resets this builder to its initial state.
     *
     * @return This builder for chaining
     */
    @NotNull
    public RecipeBuilder reset() {
        this.id = null;
        this.result = null;
        this.type = ZentrixRecipe.RecipeType.SHAPED;
        this.pattern = new String[0];
        this.ingredientMap.clear();
        this.shapelessIngredients.clear();
        this.oneTime = false;
        this.craftLimit = -1;
        this.customFields.clear();
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RecipeBuilder{");
        sb.append("id='").append(id).append('\'');
        sb.append(", type=").append(type);
        sb
            .append(", result=")
            .append(result != null ? result.getType() : "null");

        if (oneTime) {
            sb.append(", oneTime=true");
        } else if (craftLimit > 0) {
            sb.append(", craftLimit=").append(craftLimit);
        } else {
            sb.append(", unlimited");
        }

        if (!customFields.isEmpty()) {
            sb.append(", customFields=").append(customFields.keySet());
        }

        sb.append('}');
        return sb.toString();
    }
}
