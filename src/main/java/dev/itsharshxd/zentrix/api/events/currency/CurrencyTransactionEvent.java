package dev.itsharshxd.zentrix.api.events.currency;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.UUID;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a balance change has been committed, as the counterpart of the cancellable
 * {@link CurrencyChangeEvent} that came before it.
 *
 * <p>Committed means written to storage or, for a reward while storage cannot take it, kept for
 * storage to receive as soon as it can, so the change will not be lost or applied twice. A change
 * that was cancelled, refused or left the balance as it was is not reported. Nothing about the
 * change can be altered any more.
 *
 * <p>Fired on the main thread, shortly after the change, and only while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CurrencyTransactionEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final double oldBalance;
    private final double newBalance;
    private final CurrencyChangeEvent.ChangeReason reason;
    private final String source;

    public CurrencyTransactionEvent(
            @NotNull UUID playerId,
            double oldBalance,
            double newBalance,
            @NotNull CurrencyChangeEvent.ChangeReason reason,
            @Nullable String source) {
        super(false);
        this.playerId = playerId;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.reason = reason;
        this.source = source;
    }

    /** The player whose balance changed. */
    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    /** The balance before the change. */
    public double getOldBalance() {
        return oldBalance;
    }

    /** The balance after the change. */
    public double getNewBalance() {
        return newBalance;
    }

    /** How much the balance moved; negative for a loss. */
    public double getChangeAmount() {
        return newBalance - oldBalance;
    }

    /** What kind of change it was. */
    @NotNull
    public CurrencyChangeEvent.ChangeReason getReason() {
        return reason;
    }

    /** The specific source, such as {@code "player-kill"} or an addon's own source. */
    @Nullable
    public String getSource() {
        return source;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
