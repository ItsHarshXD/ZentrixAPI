package dev.itsharshxd.zentrix.api.platform;

/**
 * The installed Zentrix edition. Edition limits are enforced by Zentrix; the API reports them but
 * cannot lift them.
 *
 * @since 1.8.0
 */
public enum ZentrixEdition {
    /** The licensed Premium edition. */
    PREMIUM,
    /** A Premium build that runs without a license check. */
    UNLICENSED,
    /** The restricted free edition. */
    FREE;

    /** Whether Premium-only features are part of this edition. */
    public boolean hasPremiumFeatures() {
        return this != FREE;
    }
}
