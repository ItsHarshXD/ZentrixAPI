package dev.itsharshxd.zentrix.api.currency;

import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What a balance change requested through {@link CurrencyService} actually did.
 *
 * @param playerId        the player whose balance it was
 * @param status          the outcome
 * @param oldBalance      the balance before the change, as storage held it; the cached balance
 *                        when the change never reached storage
 * @param newBalance      the balance after it; equal to {@code oldBalance} unless it was applied
 * @param requestedAmount the change asked for: the amount for a deposit or withdrawal, the
 *                        difference for a set or reset
 * @param source          the source the caller gave, as {@code CurrencyChangeEvent} reported it
 * @param failure         why it failed, for {@link Status#FAILED} and {@link Status#UNAVAILABLE};
 *                        {@code null} otherwise
 * @since 1.8.0
 */
public record CurrencyTransaction(
        @NotNull UUID playerId,
        @NotNull Status status,
        double oldBalance,
        double newBalance,
        double requestedAmount,
        @NotNull String source,
        @Nullable String failure) {

    public CurrencyTransaction {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(status, "status");
        Objects.requireNonNull(source, "source");
    }

    /** A change that was refused before anything was asked of storage or listeners. */
    @NotNull
    public static CurrencyTransaction refused(
            @NotNull UUID playerId, @NotNull Status status, double balance, double requestedAmount,
            @NotNull String source, @Nullable String failure) {
        return new CurrencyTransaction(playerId, status, balance, balance, requestedAmount, source, failure);
    }

    /** Whether the balance changed, exactly as requested or as a listener adjusted it. */
    public boolean isApplied() {
        return status == Status.APPLIED || status == Status.ADJUSTED;
    }

    /** How much the balance really moved; negative for a loss. */
    public double appliedAmount() {
        return newBalance - oldBalance;
    }

    /** The outcome of a balance change. */
    public enum Status {
        /** Applied as requested; a withdrawal or set that would go below zero is clamped at zero. */
        APPLIED,
        /** Applied, but a {@code CurrencyChangeEvent} listener changed the resulting balance. */
        ADJUSTED,
        /** A {@code CurrencyChangeEvent} listener cancelled it; nothing changed. */
        CANCELLED,
        /** A withdrawal larger than the balance; nothing changed and no event was fired. */
        INSUFFICIENT_FUNDS,
        /** The amount was negative, zero where that is meaningless, or not a finite number. */
        INVALID_AMOUNT,
        /** Currency storage is unavailable or shutting down; nothing changed. */
        UNAVAILABLE,
        /** Storage refused the change; nothing changed. */
        FAILED
    }
}
