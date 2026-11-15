package dev.itsharshxd.zentrix.api.perk;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One named tier of {@code perks.yml}, such as {@code vip}. A snapshot.
 *
 * <p>A player belongs to the enabled tier with the highest priority whose permission they hold.
 * A tier may inherit from a parent tier: each value comes from the tier itself, then up its
 * parent chain, and only then from the server-wide default. The values here are the effective
 * ones, inherited values included; a perk none of them sets follows the server-wide default.
 *
 * <p>A number value of {@link PerkDefinition#UNLIMITED} ({@code -1}) means unlimited, for perks
 * that support it.
 *
 * @param id           the tier's key in {@code perks.yml}
 * @param displayName  the name shown to players, with its color codes
 * @param permission   the node that puts a player in the tier
 * @param priority     higher wins when a player holds several tiers' permissions
 * @param numbers      the {@link Perk.Type#NUMBER} built-in perks the tier sets or inherits
 * @param toggles      the {@link Perk.Type#TOGGLE} built-in perks the tier sets or inherits
 * @param parent       the ID of the tier it inherits from, or null (since 1.16.0)
 * @param icon         the item it wears in menus, or null for the default (since 1.16.0)
 * @param description  lines describing it in menus, with color codes (since 1.16.0)
 * @param addonNumbers the number addon perks it sets or inherits, by key (since 1.16.0)
 * @param addonToggles the toggle addon perks it sets or inherits, by key (since 1.16.0)
 * @since 1.9.0
 */
public record PerkTier(
        @NotNull String id, @NotNull String displayName, @NotNull String permission, int priority,
        @NotNull Map<Perk, Integer> numbers, @NotNull Map<Perk, Boolean> toggles,
        @Nullable String parent, @Nullable Material icon, @NotNull List<String> description,
        @NotNull Map<String, Integer> addonNumbers, @NotNull Map<String, Boolean> addonToggles) {

    public PerkTier {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(permission, "permission");
        numbers = copy(numbers);
        toggles = copy(toggles);
        description = description == null ? List.of() : List.copyOf(description);
        addonNumbers = copyKeyed(addonNumbers);
        addonToggles = copyKeyed(addonToggles);
    }

    /** A tier without a parent, icon, description or addon perks. */
    public PerkTier(@NotNull String id, @NotNull String displayName, @NotNull String permission, int priority,
                    @NotNull Map<Perk, Integer> numbers, @NotNull Map<Perk, Boolean> toggles) {
        this(id, displayName, permission, priority, numbers, toggles, null, null, List.of(), Map.of(), Map.of());
    }

    private static <V> Map<Perk, V> copy(Map<Perk, V> values) {
        Map<Perk, V> copy = new EnumMap<>(Perk.class);
        if (values != null) {
            values.forEach((perk, value) -> {
                if (perk != null && value != null) {
                    copy.put(perk, value);
                }
            });
        }
        return Collections.unmodifiableMap(copy);
    }

    private static <V> Map<String, V> copyKeyed(Map<String, V> values) {
        Map<String, V> copy = new LinkedHashMap<>();
        if (values != null) {
            values.forEach((key, value) -> {
                if (key != null && value != null) {
                    copy.put(key, value);
                }
            });
        }
        return Collections.unmodifiableMap(copy);
    }

    /** Whether the tier sets or inherits a perk rather than leaving it to the server-wide default. */
    public boolean sets(@NotNull Perk perk) {
        return numbers.containsKey(perk) || toggles.containsKey(perk);
    }

    /**
     * Whether the tier sets or inherits a perk, built-in or addon, by its key.
     *
     * @since 1.16.0
     */
    public boolean sets(@NotNull String perkKey) {
        Optional<Perk> perk = builtIn(perkKey);
        return perk.isPresent() ? sets(perk.get())
                : addonNumbers.containsKey(perkKey) || addonToggles.containsKey(perkKey);
    }

    /**
     * The number the tier sets for a {@link Perk.Type#NUMBER} perk, if it sets one;
     * {@link PerkDefinition#UNLIMITED} when unlimited.
     */
    @NotNull
    public OptionalInt getNumber(@NotNull Perk perk) {
        Integer value = numbers.get(perk);
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    /**
     * The number the tier sets for a number perk, built-in or addon, by its key.
     *
     * @since 1.16.0
     */
    @NotNull
    public OptionalInt getNumber(@NotNull String perkKey) {
        Optional<Perk> perk = builtIn(perkKey);
        if (perk.isPresent()) {
            return getNumber(perk.get());
        }
        Integer value = addonNumbers.get(perkKey);
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    /** Whether the tier allows a {@link Perk.Type#TOGGLE} perk, if it sets it. */
    @NotNull
    public Optional<Boolean> getToggle(@NotNull Perk perk) {
        return Optional.ofNullable(toggles.get(perk));
    }

    /**
     * Whether the tier allows a toggle perk, built-in or addon, by its key, if it sets it.
     *
     * @since 1.16.0
     */
    @NotNull
    public Optional<Boolean> getToggle(@NotNull String perkKey) {
        Optional<Perk> perk = builtIn(perkKey);
        return perk.isPresent() ? getToggle(perk.get()) : Optional.ofNullable(addonToggles.get(perkKey));
    }

    /**
     * The parent tier's ID, when the tier inherits from one.
     *
     * @since 1.16.0
     */
    @NotNull
    public Optional<String> getParent() {
        return Optional.ofNullable(parent);
    }

    private static Optional<Perk> builtIn(String key) {
        for (Perk perk : Perk.values()) {
            if (perk.getKey().equals(key)) {
                return Optional.of(perk);
            }
        }
        return Optional.empty();
    }
}
