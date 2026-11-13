package dev.itsharshxd.zentrix.api.gui.background;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A value an add-on lets menu background rules in {@code guis.yml} compare, the way the built-in
 * {@code world} or {@code game.players} values are compared.
 *
 * <p>Server owners write it under the add-on's namespace, as
 * {@link MenuBackgroundCondition#nameOf(org.bukkit.plugin.Plugin, String) namespace:name}, with
 * any of the usual comparisons. For a value registered as {@code mystats:wins}:
 *
 * <pre>
 * - when: { mystats:wins: "&gt;=100" }
 * - when: { mystats:wins: "10..99" }
 * - when: "mystats:wins &gt;= 100 or mystats:title in [champion, legend]"
 * - when: "mystats:veteran"                      the value is true
 * </pre>
 *
 * <p>Numbers compare as numbers and everything else as text, ignoring case. A {@code null} value
 * matches nothing, and a value that throws counts as not matching and is reported in the console
 * once.
 *
 * @since 1.15.0
 */
@FunctionalInterface
public interface MenuBackgroundValue {

    /**
     * Gives the value for one viewer's menu. Runs on the main thread.
     *
     * @param context the viewer, their menu and the moment
     * @return the value, compared through its {@code toString()}; null when there is none
     */
    @Nullable
    Object value(@NotNull MenuConditionContext context);
}
