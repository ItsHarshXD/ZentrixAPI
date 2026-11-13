package dev.itsharshxd.zentrix.api.gui.background;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A condition an add-on adds to the {@code when} of menu background rules in {@code guis.yml}.
 *
 * <p>Server owners write it under the add-on's namespace, {@code namespace:name} (see
 * {@link #nameOf(Plugin, String)}), anywhere a built-in condition such as {@code permission} may
 * stand. For a condition registered as {@code myranks:rank}:
 *
 * <pre>
 * - when: { myranks:rank: gold }                 argument "gold"
 * - when: { myranks:rank: [gold, "!trial"] }     every one must hold; "!" means "not"
 * - when: { myranks:rank: true }                 argument "" (false asks for the opposite)
 * - when: "myranks:rank(gold) and page > 1"      argument "gold"; several are all tested
 * - when: "not myranks:rank"                     argument ""
 * </pre>
 *
 * <p>The argument is what the owner wrote, trimmed; what it means is up to the add-on. A condition
 * that throws counts as false and is reported in the console once.
 *
 * @since 1.15.0
 */
@FunctionalInterface
public interface MenuBackgroundCondition {

    /**
     * Tests the condition for one viewer's menu. Runs on the main thread.
     *
     * @param context  the viewer, their menu and the moment
     * @param argument what the owner wrote after the condition's name, empty when nothing
     * @return whether the condition holds
     */
    boolean test(@NotNull MenuConditionContext context, @NotNull String argument);

    /**
     * The name server owners write for a condition or value a plugin registers: the plugin's name
     * in lowercase, with every character outside {@code a-z}, {@code 0-9}, {@code _} and {@code -}
     * replaced by {@code _}, then a colon and the name.
     *
     * @param owner the registering plugin
     * @param name  the condition's name within the plugin, using {@code a-z}, {@code 0-9},
     *              {@code _} and {@code -}; case is ignored
     * @return the name as written in {@code guis.yml}, such as {@code myranks:rank}
     * @throws IllegalArgumentException when the name uses other characters
     */
    @NotNull
    static String nameOf(@NotNull Plugin owner, @NotNull String name) {
        String key = Objects.requireNonNull(name, "name").trim().toLowerCase(Locale.ROOT);
        if (!Names.PART.matcher(key).matches()) {
            throw new IllegalArgumentException(
                    "Menu background condition names use a-z, 0-9, _ and -: " + name);
        }
        String namespace = Objects.requireNonNull(owner, "owner").getName().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9_-]", "_");
        return namespace + ':' + key;
    }

    /** Holds the name pattern, which an interface cannot keep as a private constant. */
    final class Names {
        static final Pattern PART = Pattern.compile("[a-z0-9_-]+");

        private Names() {
        }
    }
}
