package dev.itsharshxd.zentrix.api.nether;

import java.util.Optional;

/** Optional border changes for a Nether access toggle. Target radius {@code 0} preserves size. */
public final class NetherBorderSettings {
    private final Boolean shrink;
    private final Double targetRadius;
    private final Integer durationSeconds;
    private final Double damageAmount;
    private final Double damageBuffer;
    private final Integer warningDistance;
    private final Integer warningTime;

    public NetherBorderSettings(Boolean shrink, Double targetRadius, Integer durationSeconds,
                                Double damageAmount, Double damageBuffer,
                                Integer warningDistance, Integer warningTime) {
        requireNonNegative(targetRadius, "targetRadius");
        requireNonNegative(durationSeconds, "durationSeconds");
        requireNonNegative(damageAmount, "damageAmount");
        requireNonNegative(damageBuffer, "damageBuffer");
        requireNonNegative(warningDistance, "warningDistance");
        requireNonNegative(warningTime, "warningTime");
        this.shrink = shrink;
        this.targetRadius = targetRadius;
        this.durationSeconds = durationSeconds;
        this.damageAmount = damageAmount;
        this.damageBuffer = damageBuffer;
        this.warningDistance = warningDistance;
        this.warningTime = warningTime;
    }

    private static void requireNonNegative(Number value, String name) {
        if (value != null && value.doubleValue() < 0) {
            throw new IllegalArgumentException(name + " cannot be negative");
        }
    }

    public Optional<Boolean> getShrink() { return Optional.ofNullable(shrink); }
    public Optional<Double> getTargetRadius() { return Optional.ofNullable(targetRadius); }
    public Optional<Integer> getDurationSeconds() { return Optional.ofNullable(durationSeconds); }
    public Optional<Double> getDamageAmount() { return Optional.ofNullable(damageAmount); }
    public Optional<Double> getDamageBuffer() { return Optional.ofNullable(damageBuffer); }
    public Optional<Integer> getWarningDistance() { return Optional.ofNullable(warningDistance); }
    public Optional<Integer> getWarningTime() { return Optional.ofNullable(warningTime); }
}
