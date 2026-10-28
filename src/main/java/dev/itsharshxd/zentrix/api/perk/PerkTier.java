package dev.itsharshxd.zentrix.api.perk;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import org.jetbrains.annotations.NotNull;

/**
 * One named tier of {@code perks.yml}, such as {@code vip}. A snapshot.
 *
 * <p>A player belongs to the enabled tier with the highest priority whose permission they hold.
 * A perk the tier does not set follows the server-wide default.
 *
 * @param id          the tier's key in {@code perks.yml}
 * @param displayName the name shown to players, with its color codes
 * @param permission  the node that puts a player in the tier
 * @param priority    higher wins when a player holds several tiers' permissions
 * @param numbers     the {@link Perk.Type#NUMBER} perks the tier sets
 * @param toggles     the {@link Perk.Type#TOGGLE} perks the tier sets
 * @since 1.9.0
 */
public record PerkTier(
        @NotNull String id, @NotNull String displayName, @NotNull String permission, int priority,
        @NotNull Map<Perk, Integer> numbers, @NotNull Map<Perk, Boolean> toggles) {

    public PerkTier {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(permission, "permission");
        numbers = copy(numbers);
        toggles = copy(toggles);
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

    /** Whether the tier sets a perk rather than leaving it to the server-wide default. */
    public boolean sets(@NotNull Perk perk) {
        return numbers.containsKey(perk) || toggles.containsKey(perk);
    }

    /** The number the tier sets for a {@link Perk.Type#NUMBER} perk, if it sets one. */
    @NotNull
    public OptionalInt getNumber(@NotNull Perk perk) {
        Integer value = numbers.get(perk);
        return value == null ? OptionalInt.empty() : OptionalInt.of(value);
    }

    /** Whether the tier allows a {@link Perk.Type#TOGGLE} perk, if it sets it. */
    @NotNull
    public Optional<Boolean> getToggle(@NotNull Perk perk) {
        return Optional.ofNullable(toggles.get(perk));
    }
}
