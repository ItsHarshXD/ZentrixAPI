package dev.itsharshxd.zentrix.api.recipe;

/**
 * The workstation a custom recipe is made at.
 *
 * <p>The crafting table holds both shaped and shapeless recipes - {@link ZentrixRecipe#getType()}
 * tells them apart. Every other station takes exactly one input. Cooking stations also have a
 * cooking time, and all but the campfire award experience.
 *
 * @since 1.8.0
 */
public enum RecipeStation {
    /** Shaped or shapeless crafting. */
    CRAFTING_TABLE(false, false, 0),
    /** Furnace smelting. */
    FURNACE(true, true, 200),
    /** Blast furnace smelting. */
    BLAST_FURNACE(true, true, 100),
    /** Smoker cooking. */
    SMOKER(true, true, 100),
    /** Campfire cooking; awards no experience. */
    CAMPFIRE(true, false, 600),
    /** Stonecutting. */
    STONECUTTER(false, false, 0),
    /**
     * A recipe type Zentrix loads but has no editor for, such as a hand-written smithing recipe.
     * Reported as it is rather than as a crafting recipe; it cannot be built through
     * {@link RecipeBuilder}.
     */
    OTHER(false, false, 0);

    /**
     * The longest cooking time, in ticks, the furnace, blast furnace and smoker keep: they store
     * it as a signed short.
     */
    public static final int MAX_FURNACE_COOKING_TIME = Short.MAX_VALUE / 20 * 20;

    /** The longest cooking time, in ticks, Zentrix keeps for a campfire: one hour. */
    public static final int MAX_CAMPFIRE_COOKING_TIME = 60 * 60 * 20;

    private final boolean cooking;
    private final boolean experience;
    private final int defaultCookingTime;

    RecipeStation(boolean cooking, boolean experience, int defaultCookingTime) {
        this.cooking = cooking;
        this.experience = experience;
        this.defaultCookingTime = defaultCookingTime;
    }

    /** Whether recipes at this station take exactly one input. */
    public boolean isSingleInput() {
        return this != CRAFTING_TABLE && this != OTHER;
    }

    /** Whether recipes at this station have a cooking time. */
    public boolean isCooking() {
        return cooking;
    }

    /** Whether recipes at this station award experience. */
    public boolean awardsExperience() {
        return experience;
    }

    /** Vanilla's cooking time at this station, in ticks; 0 when it does not cook. */
    public int getDefaultCookingTime() {
        return defaultCookingTime;
    }

    /** The longest cooking time this station keeps, in ticks; 0 when it does not cook. */
    public int getMaxCookingTime() {
        if (!cooking) {
            return 0;
        }
        return this == CAMPFIRE ? MAX_CAMPFIRE_COOKING_TIME : MAX_FURNACE_COOKING_TIME;
    }
}
