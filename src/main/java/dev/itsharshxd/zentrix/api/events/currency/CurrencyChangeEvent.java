package dev.itsharshxd.zentrix.api.events.currency;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Called when a player's currency balance is about to change.
 * <p>
 * This event is fired before the balance modification is applied, allowing
 * addons to:
 * <ul>
 *   <li>Cancel the change entirely</li>
 *   <li>Modify the new balance amount</li>
 *   <li>Log or track balance changes</li>
 *   <li>Implement custom restrictions</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>cancellable</b>. Cancelling prevents the balance change.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onCurrencyChange(CurrencyChangeEvent event) {
 *     UUID playerId = event.getPlayerId();
 *     double oldBalance = event.getOldBalance();
 *     double newBalance = event.getNewBalance();
 *     double change = event.getChangeAmount();
 *     ChangeReason reason = event.getReason();
 *
 *     // Log all currency changes
 *     getLogger().info(String.format(
 *         "Currency change for %s: %.2f -> %.2f (%s%.2f) [%s]",
 *         event.getPlayerName(),
 *         oldBalance, newBalance,
 *         change >= 0 ? "+" : "", change,
 *         reason.name()
 *     ));
 *
 *     // Prevent negative balance
 *     if (newBalance < 0) {
 *         event.setNewBalance(0);
 *     }
 *
 *     // Block changes from certain sources
 *     if (reason == ChangeReason.ADDON && !hasPermission(playerId)) {
 *         event.setCancelled(true);
 *         event.getPlayer().ifPresent(p ->
 *             p.sendMessage("Currency change blocked!"));
 *     }
 *
 *     // Apply tax on large gains
 *     if (change > 1000) {
 *         double taxedAmount = change * 0.9; // 10% tax
 *         event.setNewBalance(oldBalance + taxedAmount);
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see dev.itsharshxd.zentrix.api.currency.CurrencyService
 */
public class CurrencyChangeEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final String playerName;
    private final double oldBalance;
    private double newBalance;
    private final ChangeReason reason;
    private final String source;
    private boolean cancelled;

    /**
     * Constructs a new CurrencyChangeEvent.
     *
     * @param playerId    The UUID of the player whose balance is changing
     * @param playerName  The name of the player
     * @param oldBalance  The balance before the change
     * @param newBalance  The balance after the change (can be modified)
     * @param reason      The reason for the balance change
     * @param source      Optional source identifier (e.g., addon name, event type)
     */
    public CurrencyChangeEvent(@NotNull UUID playerId,
                               @NotNull String playerName,
                               double oldBalance,
                               double newBalance,
                               @NotNull ChangeReason reason,
                               @Nullable String source) {
        super(false);
        this.playerId = playerId;
        this.playerName = playerName;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.reason = reason;
        this.source = source;
        this.cancelled = false;
    }

    /**
     * Constructs a new CurrencyChangeEvent without a source.
     *
     * @param playerId   The UUID of the player whose balance is changing
     * @param playerName The name of the player
     * @param oldBalance The balance before the change
     * @param newBalance The balance after the change
     * @param reason     The reason for the balance change
     */
    public CurrencyChangeEvent(@NotNull UUID playerId,
                               @NotNull String playerName,
                               double oldBalance,
                               double newBalance,
                               @NotNull ChangeReason reason) {
        this(playerId, playerName, oldBalance, newBalance, reason, null);
    }

    /**
     * Gets the UUID of the player whose balance is changing.
     *
     * @return The player's UUID (never null)
     */
    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Gets the name of the player whose balance is changing.
     *
     * @return The player's name (never null)
     */
    @NotNull
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the Bukkit player instance if online.
     * <p>
     * The player may be offline if the change is being made asynchronously
     * or from storage operations.
     * </p>
     *
     * @return Optional containing the player, or empty if offline
     */
    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(org.bukkit.Bukkit.getPlayer(playerId));
    }

    /**
     * Gets the player's balance before the change.
     *
     * @return The old balance
     */
    public double getOldBalance() {
        return oldBalance;
    }

    /**
     * Gets the player's balance after the change.
     * <p>
     * This value can be modified by event handlers before the change is applied.
     * </p>
     *
     * @return The new balance
     */
    public double getNewBalance() {
        return newBalance;
    }

    /**
     * Sets the new balance that will be applied.
     * <p>
     * Use this to modify the final balance. The value will be clamped
     * to a minimum of 0 when applied.
     * </p>
     *
     * @param newBalance The new balance to set
     */
    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    /**
     * Gets the amount of change (positive for gains, negative for losses).
     * <p>
     * This is calculated as {@code newBalance - oldBalance}.
     * </p>
     *
     * @return The change amount
     */
    public double getChangeAmount() {
        return newBalance - oldBalance;
    }

    /**
     * Checks if this is a balance increase (gain).
     *
     * @return {@code true} if the new balance is higher than the old balance
     */
    public boolean isGain() {
        return newBalance > oldBalance;
    }

    /**
     * Checks if this is a balance decrease (loss).
     *
     * @return {@code true} if the new balance is lower than the old balance
     */
    public boolean isLoss() {
        return newBalance < oldBalance;
    }

    /**
     * Checks if there is no actual change in balance.
     *
     * @return {@code true} if old and new balance are equal
     */
    public boolean isNoChange() {
        return Double.compare(oldBalance, newBalance) == 0;
    }

    /**
     * Gets the reason for the balance change.
     *
     * @return The change reason (never null)
     */
    @NotNull
    public ChangeReason getReason() {
        return reason;
    }

    /**
     * Gets the source identifier for this change.
     * <p>
     * This can be an addon name, event type, or other identifier
     * that helps track where the change originated from.
     * </p>
     *
     * @return Optional containing the source, or empty if not specified
     */
    @NotNull
    public Optional<String> getSource() {
        return Optional.ofNullable(source);
    }

    /**
     * Checks if this change was triggered by a game event reward.
     *
     * @return {@code true} if the reason is {@link ChangeReason#EVENT_REWARD}
     */
    public boolean isEventReward() {
        return reason == ChangeReason.EVENT_REWARD;
    }

    /**
     * Checks if this change was made by an administrator.
     *
     * @return {@code true} if the reason is {@link ChangeReason#ADMIN}
     */
    public boolean isAdminChange() {
        return reason == ChangeReason.ADMIN;
    }

    /**
     * Checks if this change was made by an addon.
     *
     * @return {@code true} if the reason is {@link ChangeReason#ADDON}
     */
    public boolean isAddonChange() {
        return reason == ChangeReason.ADDON;
    }

    /**
     * Adds a bonus amount to the new balance.
     * <p>
     * Convenience method to add to the new balance without knowing the current value.
     * </p>
     *
     * @param bonus The amount to add (can be negative to reduce)
     */
    public void addBonus(double bonus) {
        this.newBalance += bonus;
    }

    /**
     * Applies a multiplier to the change amount.
     * <p>
     * This modifies the new balance by multiplying the change amount.
     * For example, a multiplier of 2.0 will double the gain/loss.
     * </p>
     *
     * @param multiplier The multiplier to apply to the change amount
     */
    public void multiplyChange(double multiplier) {
        double change = getChangeAmount();
        this.newBalance = oldBalance + (change * multiplier);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the static handler list for this event type.
     *
     * @return The handler list
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Represents the reason for a currency balance change.
     */
    public enum ChangeReason {

        /**
         * Balance was changed by an administrator command.
         * <p>
         * Examples: /currency set, /currency add, /currency take
         * </p>
         */
        ADMIN,

        /**
         * Balance was changed due to a game event reward.
         * <p>
         * Examples: kill reward, win bonus, first blood, placement rewards
         * </p>
         */
        EVENT_REWARD,

        /**
         * Balance was changed by a third-party addon.
         * <p>
         * Addons should use this reason when modifying balances.
         * </p>
         */
        ADDON,

        /**
         * Balance was changed due to a purchase or transaction.
         */
        PURCHASE,

        /**
         * Balance was changed due to a penalty (e.g., death penalty).
         */
        PENALTY,

        /**
         * Balance was changed due to a transfer between players.
         */
        TRANSFER,

        /**
         * Balance was reset to default/starting balance.
         */
        RESET,

        /**
         * Balance was loaded from storage.
         */
        STORAGE_LOAD,

        /**
         * Balance was changed for an unknown or unspecified reason.
         */
        OTHER
    }
}
