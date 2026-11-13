package dev.itsharshxd.zentrix.api.gui.background;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What one open Zentrix menu shows, as its background rules see it.
 *
 * <p>Zentrix records this while the menu places its buttons: every element placed through the
 * menu's {@code guis.yml} layout counts as shown, in the state it was drawn in. It is the same
 * record the {@code shown}, {@code state}, {@code entries}, {@code page} and {@code view}
 * conditions are tested against, and what {@code /zx gui debug} prints.
 *
 * <p>A view is a snapshot: it does not follow the menu after it was taken.
 *
 * @param menuId     the menu's ID in {@code guis.yml}, such as {@code party.members}
 * @param name       the menu's own name: its title without the texture
 * @param rows       the menu's size in rows
 * @param elements   every visible element, in the order placed, mapped to the state it is drawn
 *                   in; an element without states maps to an empty string
 * @param entries    how many entries each content list the menu reported holds, across all pages
 * @param page       the page on screen, counting from 1
 * @param pages      how many pages there are, at least 1
 * @param view       the view the menu reports, such as {@code no-party}; empty for a menu with one
 * @param background what the background decided, or null while conditions are being tested and
 *                   for a menu whose texture Zentrix does not decide: one without rules in
 *                   {@code guis.yml} that no resolver covers
 * @since 1.15.0
 */
public record MenuView(
        @NotNull String menuId,
        @NotNull String name,
        int rows,
        @NotNull Map<String, String> elements,
        @NotNull Map<String, Integer> entries,
        int page,
        int pages,
        @NotNull String view,
        @Nullable MenuBackgroundResult background) {

    /** The content list a menu with several lists calls its main one. */
    public static final String MAIN_LIST = "entries";

    public MenuView {
        Objects.requireNonNull(menuId, "menuId");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(view, "view");
        elements = Collections.unmodifiableMap(new LinkedHashMap<>(Objects.requireNonNull(elements, "elements")));
        entries = Collections.unmodifiableMap(new LinkedHashMap<>(Objects.requireNonNull(entries, "entries")));
    }

    /** Whether an element is visible, ignoring case. */
    public boolean isShown(@NotNull String elementId) {
        return elements.containsKey(normalize(elementId));
    }

    /**
     * The state a visible element is drawn in, ignoring case.
     *
     * @return the state, an empty string for an element without states, or empty when the element
     *         is not visible
     */
    @NotNull
    public Optional<String> state(@NotNull String elementId) {
        return Optional.ofNullable(elements.get(normalize(elementId)));
    }

    /**
     * How many entries one content list holds across all its pages.
     *
     * @return the count, or empty when the menu did not report that list
     */
    @NotNull
    public OptionalInt entryCount(@NotNull String listId) {
        Integer count = entries.get(normalize(listId));
        return count == null ? OptionalInt.empty() : OptionalInt.of(count);
    }

    /**
     * How many entries the menu's main list holds: the list named {@value #MAIN_LIST}, or else the
     * only list the menu reported.
     *
     * @return the count, or empty when there is no such list
     */
    @NotNull
    public OptionalInt entryCount() {
        Integer main = entries.get(MAIN_LIST);
        if (main != null) {
            return OptionalInt.of(main);
        }
        return entries.size() == 1 ? OptionalInt.of(entries.values().iterator().next()) : OptionalInt.empty();
    }

    /** Whether the menu has a page before this one or after it. */
    public boolean isPaged() {
        return pages > 1;
    }

    /** What the background decided, when it decided anything. */
    @NotNull
    public Optional<MenuBackgroundResult> backgroundResult() {
        return Optional.ofNullable(background);
    }

    private static String normalize(String id) {
        return Objects.requireNonNull(id, "id").trim().toLowerCase(Locale.ROOT);
    }
}
