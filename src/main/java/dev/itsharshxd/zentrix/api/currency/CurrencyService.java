package dev.itsharshxd.zentrix.api.currency;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for currency-related queries within Zentrix.
 * <p>
 * This service provides access to player balances, currency configuration,
 * and reward information. Balance modifications should be done through
 * the event system.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * CurrencyService currencyService = ZentrixProvider.get().getCurrencyService();
 *
 * // Get player's balance
 * double balance = currencyService.getCachedBalance(player);
 *
 * // Get formatted balance
 * String formatted = currencyService.formatBalance(balance);
 * // Result: "⛃ 150"
 *
 * // Check reward for an event
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
     * This method fetches the balance from storage if not cached.
     * For synchronous access, use {@link #getCachedBalance(UUID)}.
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
     * Returns the cached value if available, or the starting balance
     * if not yet cached. Use this for scoreboards and GUIs where
     * async operations are not desirable.
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
     * Example: "&6Coins" or "&#FFD700Gold"
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
