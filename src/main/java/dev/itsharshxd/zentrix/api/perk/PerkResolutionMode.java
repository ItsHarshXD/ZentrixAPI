package dev.itsharshxd.zentrix.api.perk;

import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * How a player holding several tiers gets each perk, set by {@code resolution-mode} in
 * {@code perks.yml}. Either way, the tier a player is shown as belonging to is the
 * highest-priority one they hold.
 *
 * @since 1.16.0
 */
public enum PerkResolutionMode {

    /** Every perk comes from the highest-priority tier the player holds. */
    HIGHEST_PRIORITY("highest-priority"),
    /**
     * Each perk takes the best value across every tier the player holds: the highest number,
     * unlimited above all, and a toggle any of them allows.
     */
    BEST_VALUE("best-value");

    private final String key;

    PerkResolutionMode(String key) {
        this.key = key;
    }

    /** The value written in {@code perks.yml}, such as {@code best-value}. */
    @NotNull
    public String getKey() {
        return key;
    }

    /** The mode a {@code perks.yml} value names, ignoring case, dashes and underscores. */
    @NotNull
    public static Optional<PerkResolutionMode> fromKey(@Nullable String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = key.trim().toLowerCase(Locale.ROOT).replace('_', '-');
        for (PerkResolutionMode mode : values()) {
            if (mode.key.equals(normalized)) {
                return Optional.of(mode);
            }
        }
        return Optional.empty();
    }
}
