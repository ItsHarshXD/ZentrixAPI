package dev.itsharshxd.zentrix.api.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the types of events that can trigger currency rewards or penalties.
 * <p>
 * Each event type corresponds to a specific in-game action that may
 * award or deduct currency from a player's balance.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * CurrencyService currencyService = ZentrixProvider.get().getCurrencyService();
 *
 * // Check if kills award currency
 * if (currencyService.isEventEnabled(CurrencyEventType.PLAYER_KILL)) {
 *     double reward = currencyService.getEventReward(CurrencyEventType.PLAYER_KILL);
 *     player.sendMessage("Kills award " + reward + " coins!");
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public enum CurrencyEventType {
    // ==========================================
    // Game Lifecycle Events
    // ==========================================

    /**
     * Awarded when a player/team wins the game.
     */
    GAME_WIN("game-win"),

    /**
     * Awarded/deducted when a player joins a game.
     */
    GAME_JOIN("game-join"),

    /**
     * Applied when a player leaves during active gameplay (can be negative).
     */
    GAME_LEAVE("game-leave"),

    // ==========================================
    // Combat Events
    // ==========================================

    /**
     * Awarded when a player kills another player.
     */
    PLAYER_KILL("player-kill"),

    /**
     * Applied when a player dies (can be a penalty/negative value).
     */
    PLAYER_DEATH("player-death"),

    /**
     * Awarded for getting the first kill in a game.
     */
    FIRST_BLOOD("first-blood"),

    // ==========================================
    // Position/Survival Events
    // ==========================================

    /**
     * Awarded for surviving until deathmatch.
     */
    SURVIVES_DEATHMATCH("survives-deathmatch"),

    /**
     * Awarded for reaching top 5 (last 5 teams/players).
     * Triggers once per game per team.
     */
    TOP_5("reaches-top-5"),

    /**
     * Awarded for reaching top 3 (last 3 teams/players).
     * Triggers once per game per team.
     */
    TOP_3("reaches-top-3"),

    // ==========================================
    // Class System Events
    // ==========================================

    /**
     * Awarded when a player selects or changes class.
     */
    CLASS_SELECT("class-select"),

    /**
     * Awarded when any class ability triggers.
     */
    CLASS_ABILITY_TRIGGER("class-ability-trigger"),

    /**
     * Awarded when Warrior's true damage procs.
     */
    WARRIOR_TRUE_DAMAGE("warrior-true-damage"),

    /**
     * Awarded when Archer's bow buff procs.
     */
    ARCHER_BOW_BUFF("archer-bow-buff"),

    /**
     * Awarded when Looter gets bonus item from chest.
     */
    LOOTER_CHEST_BONUS("looter-chest-bonus"),

    /**
     * Awarded when Trapper's fall damage reduction procs.
     */
    TRAPPER_FALL_REDUCTION("trapper-fall-reduction"),

    /**
     * Awarded when Fisherman's bonus procs.
     */
    FISHERMAN_BONUS("fisherman-bonus"),

    /**
     * Awarded when Miner's auto-smelt procs.
     */
    MINER_AUTO_SMELT("miner-auto-smelt"),

    // ==========================================
    // Crafting Events
    // ==========================================

    /**
     * Awarded for crafting any custom recipe.
     */
    CRAFTS_CUSTOM_RECIPE("crafts-custom-recipe"),

    /**
     * Awarded for first-time craft of a one-time recipe.
     */
    CRAFTS_FIRST_TIME("crafts-first-time"),

    // ==========================================
    // Other Events
    // ==========================================

    /**
     * Awarded when a player becomes the last surviving member of their team.
     */
    LAST_TEAM_MEMBER("last-team-member"),

    /**
     * Awarded for simply participating in a game (regardless of placement).
     */
    PARTICIPATION("participation");

    private final String configKey;

    /**
     * Creates a new currency event type.
     *
     * @param configKey The key used in the configuration file
     */
    CurrencyEventType(@NotNull String configKey) {
        this.configKey = configKey;
    }

    /**
     * Gets the configuration key for this event type.
     * <p>
     * This is the key used in {@code currency.yml} to configure rewards.
     * </p>
     *
     * @return The config key (e.g., "player-kill", "first-blood")
     */
    @NotNull
    public String getConfigKey() {
        return configKey;
    }

    /**
     * Checks if this is a combat-related event.
     *
     * @return {@code true} if this event relates to combat
     */
    public boolean isCombatEvent() {
        return (
            this == PLAYER_KILL || this == PLAYER_DEATH || this == FIRST_BLOOD
        );
    }

    /**
     * Checks if this is a class ability event.
     *
     * @return {@code true} if this event relates to class abilities
     */
    public boolean isClassEvent() {
        return (
            this == CLASS_SELECT ||
            this == CLASS_ABILITY_TRIGGER ||
            this == WARRIOR_TRUE_DAMAGE ||
            this == ARCHER_BOW_BUFF ||
            this == LOOTER_CHEST_BONUS ||
            this == TRAPPER_FALL_REDUCTION ||
            this == FISHERMAN_BONUS ||
            this == MINER_AUTO_SMELT
        );
    }

    /**
     * Checks if this is a milestone/position event.
     *
     * @return {@code true} if this event is a milestone achievement
     */
    public boolean isMilestoneEvent() {
        return (
            this == TOP_5 ||
            this == TOP_3 ||
            this == SURVIVES_DEATHMATCH ||
            this == LAST_TEAM_MEMBER
        );
    }

    /**
     * Checks if this is a game lifecycle event.
     *
     * @return {@code true} if this event relates to game lifecycle
     */
    public boolean isGameLifecycleEvent() {
        return this == GAME_WIN || this == GAME_JOIN || this == GAME_LEAVE;
    }

    /**
     * Gets a CurrencyEventType from its configuration key.
     *
     * @param key The configuration key
     * @return The matching event type, or null if not found
     */
    @Nullable
    public static CurrencyEventType fromConfigKey(@Nullable String key) {
        if (key == null) {
            return null;
        }
        for (CurrencyEventType type : values()) {
            if (type.configKey.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Gets a CurrencyEventType from its name (case-insensitive).
     *
     * @param name The enum name
     * @return The matching event type, or null if not found
     */
    @Nullable
    public static CurrencyEventType fromName(@Nullable String name) {
        if (name == null) {
            return null;
        }
        try {
            return valueOf(name.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return configKey;
    }
}
