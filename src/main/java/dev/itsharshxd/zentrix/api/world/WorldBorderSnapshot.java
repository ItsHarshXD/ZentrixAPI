package dev.itsharshxd.zentrix.api.world;

/** Immutable snapshot of Bukkit world-border settings. Diameter, damage buffer,
 * and warning distance are measured in blocks; warning time is in seconds.
 * @since 1.3.0
 */
public record WorldBorderSnapshot(
    double diameter,
    double damageAmount,
    double damageBuffer,
    int warningDistance,
    int warningTime
) {
    public WorldBorderSnapshot {
        if (!Double.isFinite(diameter) || !Double.isFinite(damageAmount) || !Double.isFinite(damageBuffer)
            || diameter < 0 || damageAmount < 0 || damageBuffer < 0 || warningDistance < 0 || warningTime < 0) {
            throw new IllegalArgumentException("World border values cannot be negative");
        }
    }
}
