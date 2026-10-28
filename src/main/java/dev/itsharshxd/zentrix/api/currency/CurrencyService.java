package dev.itsharshxd.zentrix.api.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Provides currency queries for Zentrix games.
 * <p>
 * Use it to read player balances, currency configuration, and reward values, and since 1.8.0
 * to change balances with {@link #deposit}, {@link #withdraw}, {@link #setBalance} and
 * {@link #resetBalance}. Built-in rewards can be adjusted or withheld through
 * {@link dev.itsharshxd.zentrix.api.events.currency.CurrencyRewardEvent}, and every committed
 * change is reported by {@link dev.itsharshxd.zentrix.api.events.currency.CurrencyTransactionEvent}.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * CurrencyService currencyService = ZentrixProvider.get().getCurrencyService();
 *
 * // Get the player's balance
 * double balance = currencyService.getCachedBalance(player);
 *
 * // Format the balance
 * String formatted = currencyService.formatBalance(balance);
 * // Result: "⛃ 150"
 *
 * // Check an event reward
 * double killReward = currencyService.getEventReward(CurrencyEventType.PLAYER_KILL);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface CurrencyService {

    /**
     * Gets a player's balance asynchronously.
     * <p>
     * If the balance is not cached, this loads it from storage. Use
     * {@link #getCachedBalance(UUID)} for synchronous access.
     * </p>
     *
     * @param playerId The player's UUID
     * @return CompletableFuture containing the balance
     */
    @NotNull
    CompletableFuture<Double> getBalance(@NotNull UUID playerId);

    /**
     * Gets a player's balance asynchronously.
     *
     * @param player The player
     * @return CompletableFuture containing the balance
     */
    @NotNull
    CompletableFuture<Double> getBalance(@NotNull Player player);

    /**
     * Gets a player's cached balance for immediate access.
     * <p>
     * Returns the cached value when available, or the starting balance otherwise.
     * Use this for scoreboards and GUIs that cannot wait for an async result.
     * </p>
     *
     * @param playerId The player's UUID
     * @return The cached balance (may be slightly outdated)
     */
    double getCachedBalance(@NotNull UUID playerId);

    /**
     * Gets a player's cached balance for immediate access.
     *
     * @param player The player
     * @return The cached balance (may be slightly outdated)
     */
    double getCachedBalance(@NotNull Player player);

    /**
     * Gets the currency display name.
     * <p>
     * Example: {@code &6Coins} or {@code &#FFD700Gold}
     * </p>
     *
     * @return The display name with color codes (never null)
     */
    @NotNull
    String getDisplayName();

    /**
     * Gets the currency symbol.
     * <p>
     * Example: "⛃" or "$"
     * </p>
     *
     * @return The currency symbol (never null)
     */
    @NotNull
    String getSymbol();

    /**
     * Gets the starting balance for new players.
     *
     * @return The starting balance
     */
    double getStartingBalance();

    /**
     * Checks if a specific event type has rewards enabled.
     *
     * @param eventType The event type to check
     * @return {@code true} if the event awards currency
     */
    boolean isEventEnabled(@NotNull CurrencyEventType eventType);

    /**
     * Gets the reward amount for a specific event type.
     * <p>
     * Returns 0 if the event is not enabled.
     * Negative values indicate penalties (currency deduction).
     * </p>
     *
     * @param eventType The event type
     * @return The reward amount (can be negative for penalties)
     */
    double getEventReward(@NotNull CurrencyEventType eventType);

    /**
     * Formats a currency amount for display.
     * <p>
     * Example: formatAmount(10.0) returns "10"
     * Example: formatAmount(10.5) returns "10.5"
     * </p>
     *
     * @param amount The amount to format
     * @return Formatted string (never null)
     */
    @NotNull
    String formatAmount(double amount);

    /**
     * Formats a balance with the currency symbol.
     * <p>
     * Example: formatBalance(150.0) returns "⛃ 150"
     * </p>
     *
     * @param balance The balance to format
     * @return Formatted string with symbol (never null)
     */
    @NotNull
    String formatBalance(double balance);

    /**
     * Refreshes the cached balance for a player.
     * <p>
     * Fetches the latest balance from storage and updates the cache.
     * </p>
     *
     * @param playerId The player's UUID
     */
    void refreshCache(@NotNull UUID playerId);

    /**
     * Refreshes the cached balance for a player.
     *
     * @param player The player
     */
    void refreshCache(@NotNull Player player);

    // ==========================================
    // Balance changes (since 1.8.0)
    // ==========================================

    /**
     * Adds currency to a player's balance.
     * <p>
     * The change goes through the same pipeline as Zentrix's own: it is queued behind the player's
     * other changes, {@link dev.itsharshxd.zentrix.api.events.currency.CurrencyChangeEvent} fires
     * before it is applied with reason {@code ADDON} and the given source, and the future completes
     * with what was really committed. No message is sent to the player. Any thread; the future may
     * complete on any thread.
     * </p>
     *
     * @param playerId The player's UUID; the player does not need to be online
     * @param amount   The amount to add; must be positive and finite
     * @param source   What the change is for, such as {@code "myaddon:quest-reward"}
     * @return The committed change
     * @since 1.8.0
     */
    @NotNull
    default CompletableFuture<CurrencyTransaction> deposit(
            @NotNull UUID playerId, double amount, @NotNull String source) {
        return CompletableFuture.completedFuture(CurrencyTransaction.refused(
                playerId, CurrencyTransaction.Status.UNAVAILABLE, getCachedBalance(playerId), amount,
                source, "Balance changes require Zentrix API 1.8.0"));
    }

    /**
     * Takes currency from a player's balance, only when they have at least that much.
     * <p>
     * The balance is checked and changed as one step of the player's queue, so two withdrawals
     * racing each other on this server can never take more than the player holds: one completes
     * with
     * {@link CurrencyTransaction.Status#INSUFFICIENT_FUNDS} and changes nothing. Otherwise it
     * behaves like {@link #deposit(UUID, double, String)}.
     * </p>
     *
     * @param playerId The player's UUID
     * @param amount   The amount to take; must be positive and finite
     * @param source   What the change is for, such as {@code "myaddon:shop"}
     * @return The committed change
     * @since 1.8.0
     */
    @NotNull
    default CompletableFuture<CurrencyTransaction> withdraw(
            @NotNull UUID playerId, double amount, @NotNull String source) {
        return CompletableFuture.completedFuture(CurrencyTransaction.refused(
                playerId, CurrencyTransaction.Status.UNAVAILABLE, getCachedBalance(playerId), -amount,
                source, "Balance changes require Zentrix API 1.8.0"));
    }

    /**
     * Sets a player's balance outright. Otherwise behaves like
     * {@link #deposit(UUID, double, String)}.
     *
     * @param playerId The player's UUID
     * @param balance  The new balance; must be zero or more and finite
     * @param source   What the change is for
     * @return The committed change
     * @since 1.8.0
     */
    @NotNull
    default CompletableFuture<CurrencyTransaction> setBalance(
            @NotNull UUID playerId, double balance, @NotNull String source) {
        double current = getCachedBalance(playerId);
        return CompletableFuture.completedFuture(CurrencyTransaction.refused(
                playerId, CurrencyTransaction.Status.UNAVAILABLE, current, balance - current,
                source, "Balance changes require Zentrix API 1.8.0"));
    }

    /**
     * Sets a player's balance back to {@link #getStartingBalance()}. Otherwise behaves like
     * {@link #deposit(UUID, double, String)}.
     *
     * @param playerId The player's UUID
     * @param source   What the change is for
     * @return The committed change
     * @since 1.8.0
     */
    @NotNull
    default CompletableFuture<CurrencyTransaction> resetBalance(
            @NotNull UUID playerId, @NotNull String source) {
        return setBalance(playerId, getStartingBalance(), source);
    }

    /**
     * The multiplier applied to the match rewards of {@code gameplay/economy.yml} this player
     * earns, from their permission nodes such as {@code zentrix.multiplier.1.5}. The highest node
     * applies; nodes do not stack.
     *
     * <p>Only positive match rewards are multiplied, before
     * {@link dev.itsharshxd.zentrix.api.events.currency.CurrencyRewardEvent} sees them. Penalties,
     * administrator commands and addon transactions such as {@link #deposit(UUID, double, String)}
     * never are.
     *
     * @param player The player
     * @return 1.0 when the player has no multiplier node or multipliers are switched off
     * @since 1.9.0
     */
    default double getRewardMultiplier(@NotNull org.bukkit.entity.Player player) {
        return 1.0D;
    }
}
