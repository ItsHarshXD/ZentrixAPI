package dev.itsharshxd.zentrix.api.gui.background;

import org.jetbrains.annotations.NotNull;

/**
 * Lets an add-on choose a menu's texture itself, instead of the rules in {@code guis.yml}.
 *
 * <p>A resolver is asked every time a menu it covers opens or redraws, after the {@code guis.yml}
 * rules have decided, and is shown what they decided. It can keep that, replace it with glyphs of
 * its own, or remove the texture. Resolvers for one menu are asked before resolvers for
 * {@link #ALL_MENUS every menu}, each group in the order registered, and the first one that does
 * not {@link MenuBackgroundChoice#keep() keep} the result decides.
 *
 * <p>A menu covered by a resolver is decided even when {@code guis.yml} gives it no background,
 * so an add-on can texture any menu on its own. Resolvers are not asked while the server owner has
 * turned backgrounds off with {@code backgrounds.enabled: false}. A resolver that throws, or
 * returns glyph names {@code guis.yml} does not define, keeps the rules' result and is reported in
 * the console once.
 *
 * @since 1.15.0
 */
@FunctionalInterface
public interface MenuBackgroundResolver {

    /** Registers a resolver for every menu instead of a single one. */
    String ALL_MENUS = "*";

    /**
     * Chooses the texture for one viewer's menu. Runs on the main thread.
     *
     * @param context    the viewer, their menu and the moment
     * @param configured what the {@code guis.yml} rules, or an earlier resolver that kept them,
     *                   decided; without rules for the menu it has no texture
     * @return the choice; {@link MenuBackgroundChoice#keep()} to leave the decision to others
     */
    @NotNull
    MenuBackgroundChoice choose(@NotNull MenuConditionContext context, @NotNull MenuBackgroundResult configured);
}
