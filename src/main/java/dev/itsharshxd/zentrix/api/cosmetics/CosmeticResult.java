package dev.itsharshxd.zentrix.api.cosmetics;

import dev.itsharshxd.zentrix.api.currency.CurrencyTransaction;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What became of a player's own action on a cosmetic: equipping, unequipping or buying it.
 *
 * @param status      what happened
 * @param change      the player's cosmetics of the type before and after, when storage was
 *                    reached; null otherwise
 * @param transaction the payment of a purchase, when one was attempted; null otherwise
 * @param message     why it failed, for logs; null on success
 * @since 1.11.0
 */
public record CosmeticResult(
        @NotNull Status status,
        @Nullable CosmeticChange change,
        @Nullable CurrencyTransaction transaction,
        @Nullable String message) {

    /** What happened. */
    public enum Status {
        /** It was done. */
        SUCCESS,
        /** Nothing needed doing, such as equipping what is already equipped. */
        UNCHANGED,
        /** The type or cosmetic does not exist. */
        UNKNOWN,
        /** The player may not use the cosmetic. */
        LOCKED,
        /** The player can already use the cosmetic, so there is nothing to buy. */
        ALREADY_UNLOCKED,
        /** The cosmetic is not for sale. */
        NOT_FOR_SALE,
        /** The player's balance does not cover the price. */
        INSUFFICIENT_FUNDS,
        /** An event listener refused it. */
        CANCELLED,
        /**
         * Cosmetics, storage or the currency cannot be used now, or the type is not part of this
         * edition.
         */
        UNAVAILABLE,
        /** Something went wrong; see {@link #message()}. */
        FAILED
    }

    public CosmeticResult {
        Objects.requireNonNull(status, "status");
    }

    /** A result that reached nothing. */
    @NotNull
    public static CosmeticResult of(@NotNull Status status, @Nullable String message) {
        return new CosmeticResult(status, null, null, message);
    }

    /** Whether it was done. */
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    /** The change, when storage was reached. */
    @NotNull
    public Optional<CosmeticChange> getChange() {
        return Optional.ofNullable(change);
    }

    /** The payment, when one was attempted. */
    @NotNull
    public Optional<CurrencyTransaction> getTransaction() {
        return Optional.ofNullable(transaction);
    }
}
