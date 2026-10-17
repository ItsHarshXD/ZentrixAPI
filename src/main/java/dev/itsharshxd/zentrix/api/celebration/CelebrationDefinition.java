package dev.itsharshxd.zentrix.api.celebration;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * One victory celebration of {@code content/cosmetics/celebrations.yml}. A snapshot.
 *
 * @param id             the stable ID cosmetics ownership is stored under
 * @param name           the display name, in Zentrix's colour format
 * @param icon           the menu icon as written: a material name or a custom item identifier
 * @param audience       who sees and hears it
 * @param componentCount how many sound, particle and firework effects it plays; 0 plays nothing
 * @param durationTicks  how long it plays once started
 * @since 1.8.0
 */
public record CelebrationDefinition(
        @NotNull String id,
        @NotNull String name,
        @NotNull String icon,
        @NotNull Audience audience,
        int componentCount,
        int durationTicks) {

    public CelebrationDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(icon, "icon");
        Objects.requireNonNull(audience, "audience");
    }

    /** Who sees and hears a celebration. */
    public enum Audience {
        /** Each winner experiences only the celebration around themselves. */
        WINNER,
        /** The winners and their fallen teammates see every winner celebrate. */
        TEAM,
        /** Every player and spectator of the match sees every winner celebrate. */
        ARENA
    }
}
