package dev.itsharshxd.zentrix.api.cosmetics;

/**
 * Where a player's own action on a cosmetic came from.
 *
 * @since 1.11.0
 */
public enum CosmeticActionOrigin {
    /** Zentrix's cosmetics menu, {@code /cosmetics}. */
    MENU,
    /** An addon, through {@link CosmeticsService}. */
    API
}
