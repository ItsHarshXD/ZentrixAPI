package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.Objects;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Where a cosmetic preview shows, and how, as passed to
 * {@link CosmeticHandler#startPreview(org.bukkit.entity.Player, CosmeticDefinition, CosmeticPreview, Runnable)}.
 *
 * <p>In place, {@link #location()} is the player's own location and the cosmetic shows on them.
 * In the showcase, the player watches a still model of themselves from a viewing spot, and the
 * cosmetic shows at the model's location instead - not on the player, who may be some distance
 * away.
 *
 * @param location where the cosmetic should appear
 * @param showcase whether the preview shows in the showcase
 * @since 1.13.0
 */
public record CosmeticPreview(@NotNull Location location, boolean showcase) {

    public CosmeticPreview {
        Objects.requireNonNull(location, "location");
        location = location.clone();
    }
}
