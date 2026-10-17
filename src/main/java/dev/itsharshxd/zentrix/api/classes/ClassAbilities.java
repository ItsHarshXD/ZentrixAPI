package dev.itsharshxd.zentrix.api.classes;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The passive ability types of the built-in classes, as {@link PlayerClass#getAbilityType()}
 * reports them.
 *
 * <p>Each built-in class runs one ability. Until 1.8.0 the API knew these by older names that
 * described mechanics Zentrix no longer has; those names are still accepted in
 * {@code content/classes.yml} and resolve, as aliases, to the ability the class really runs:
 *
 * <table>
 *   <caption>Ability types and their legacy aliases</caption>
 *   <tr><th>Class</th><th>Ability type</th><th>Legacy alias</th></tr>
 *   <tr><td>Warrior</td><td>{@value #TRUE_DAMAGE_PROC}</td><td>{@code KILL_REWARD}</td></tr>
 *   <tr><td>Archer</td><td>{@value #BOW_SHOT_BUFF}</td><td>-</td></tr>
 *   <tr><td>Looter</td><td>{@value #CHEST_BONUS}</td><td>{@code MOB_EXTRA_DROP}</td></tr>
 *   <tr><td>Trapper</td><td>{@value #FALL_DAMAGE_REDUCTION}</td><td>{@code WOOD_CONSERVATION}</td></tr>
 *   <tr><td>Fisherman</td><td>{@value #FISHING_BONUS}</td><td>-</td></tr>
 *   <tr><td>Miner</td><td>{@value #AUTO_SMELT}</td><td>{@code ORE_DOUBLE_DROP}</td></tr>
 * </table>
 *
 * @since 1.8.0
 */
public final class ClassAbilities {

    /** Warrior: a chance for a melee hit to deal delayed bonus damage. */
    public static final String TRUE_DAMAGE_PROC = "TRUE_DAMAGE_PROC";

    /** Archer: critical arrow hits grant regeneration and speed. */
    public static final String BOW_SHOT_BUFF = "BOW_SHOT_BUFF";

    /** Looter: bonus loot from the first eligible chest or barrel opened. */
    public static final String CHEST_BONUS = "CHEST_BONUS";

    /** Trapper: reduced fall damage. */
    public static final String FALL_DAMAGE_REDUCTION = "FALL_DAMAGE_REDUCTION";

    /** Fisherman: Luck, extra catches and minerals while fishing. */
    public static final String FISHING_BONUS = "FISHING_BONUS";

    /** Miner: mined ores come out smelted, scaled by Fortune. */
    public static final String AUTO_SMELT = "AUTO_SMELT";

    /** Every ability type, in class order. */
    public static final List<String> TYPES = List.of(
            TRUE_DAMAGE_PROC, BOW_SHOT_BUFF, CHEST_BONUS, FALL_DAMAGE_REDUCTION, FISHING_BONUS,
            AUTO_SMELT);

    /** Legacy names, by the ability type each now resolves to. */
    public static final Map<String, String> LEGACY_ALIASES = Map.of(
            "KILL_REWARD", TRUE_DAMAGE_PROC,
            "MOB_EXTRA_DROP", CHEST_BONUS,
            "WOOD_CONSERVATION", FALL_DAMAGE_REDUCTION,
            "ORE_DOUBLE_DROP", AUTO_SMELT);

    private ClassAbilities() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    /**
     * The ability type a configured name stands for.
     *
     * @param name an ability type or legacy alias, in any case, with {@code -} or {@code _}
     * @return the ability type, or empty when the name is neither
     */
    @NotNull
    public static Optional<String> canonical(@Nullable String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        String normalized = name.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        if (TYPES.contains(normalized)) {
            return Optional.of(normalized);
        }
        return Optional.ofNullable(LEGACY_ALIASES.get(normalized));
    }
}
