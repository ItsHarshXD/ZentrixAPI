package dev.itsharshxd.zentrix.api.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Provides currency queries for Zentrix games.
 * <p>
 * Use it to read player balances, currency configuration, and reward values.
 * Change balances through the event system.
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
}
