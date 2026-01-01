package dev.itsharshxd.zentrix.api.profile;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Service for accessing player profile statistics.
 * <p>
 * This service provides access to persistent player statistics such as
 * wins, kills, deaths, and other lifetime achievements.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ProfileService profileService = ZentrixProvider.get().getProfileService();
 *
 * // Get player stats asynchronously
 * profileService.getStats(player.getUniqueId()).thenAccept(stats -> {
 *     int wins = stats.getWins();
 *     int kills = stats.getKills();
 *     double kd = stats.getKDRatio();
 *
 *     player.sendMessage("Wins: " + wins + ", K/D: " + kd);
 * });
 *
 * // Or get cached stats synchronously
 * PlayerStats cached = profileService.getCachedStats(player.getUniqueId());
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ProfileService {

    /**
     * Gets a player's statistics asynchronously.
     * <p>
     * This method fetches stats from storage if not cached.
     * For synchronous access, use {@link #getCachedStats(UUID)}.
     * </p>
     *
     * @param playerId The player's UUID
     * @return CompletableFuture containing the player's stats
     */
    @NotNull
    CompletableFuture<PlayerStats> getStats(@NotNull UUID playerId);

    /**
     * Gets a player's statistics asynchronously.
     *
     * @param player The player
     * @return CompletableFuture containing the player's stats
     */
    @NotNull
    CompletableFuture<PlayerStats> getStats(@NotNull Player player);

    /**
     * Gets a player's cached statistics for immediate access.
     * <p>
     * Returns the cached stats if available, or empty stats if not yet loaded.
     * Use this for scoreboards and GUIs where async operations are not desirable.
     * </p>
     *
     * @param playerId The player's UUID
     * @return The cached stats (may be empty if not loaded)
     */
    @NotNull
    PlayerStats getCachedStats(@NotNull UUID playerId);

    /**
     * Gets a player's cached statistics for immediate access.
     *
     * @param player The player
     * @return The cached stats (may be empty if not loaded)
     */
    @NotNull
    PlayerStats getCachedStats(@NotNull Player player);

    /**
     * Preloads a player's statistics into the cache.
     * <p>
     * Call this when a player joins the server to ensure their stats
     * are available for quick access.
     * </p>
     *
     * @param playerId The player's UUID
     * @return CompletableFuture that completes when loading is done
     */
    @NotNull
    CompletableFuture<PlayerStats> loadStats(@NotNull UUID playerId);

    /**
     * Preloads a player's statistics into the cache.
     *
     * @param player The player
     * @return CompletableFuture that completes when loading is done
     */
    @NotNull
    CompletableFuture<PlayerStats> loadStats(@NotNull Player player);

    /**
     * Checks if a player's stats are currently cached.
     *
     * @param playerId The player's UUID
     * @return {@code true} if stats are in the cache
     */
    boolean isCached(@NotNull UUID playerId);

    /**
     * Formats a survival time in seconds to a human-readable format.
     * <p>
     * Examples:
     * <ul>
     *   <li>45 seconds → "45s"</li>
     *   <li>150 seconds → "2m 30s"</li>
     *   <li>3700 seconds → "1h 1m"</li>
     * </ul>
     * </p>
     *
     * @param seconds The time in seconds
     * @return Formatted time string (never null)
     */
    @NotNull
    String formatSurvivalTime(double seconds);

    /**
     * Gets the total number of wins for a player (cached).
     * <p>
     * Convenience method for quick access to win count.
     * </p>
     *
     * @param playerId The player's UUID
     * @return The win count, or 0 if not cached
     */
    int getWins(@NotNull UUID playerId);

    /**
     * Gets the total number of kills for a player (cached).
     *
     * @param playerId The player's UUID
     * @return The kill count, or 0 if not cached
     */
    int getKills(@NotNull UUID playerId);

    /**
     * Gets the total number of deaths for a player (cached).
     *
     * @param playerId The player's UUID
     * @return The death count, or 0 if not cached
     */
    int getDeaths(@NotNull UUID playerId);

    /**
     * Gets the total matches played for a player (cached).
     *
     * @param playerId The player's UUID
     * @return The match count, or 0 if not cached
     */
    int getMatchesPlayed(@NotNull UUID playerId);

    /**
     * Gets the K/D ratio for a player (cached).
     *
     * @param playerId The player's UUID
     * @return The K/D ratio, or 0 if not cached
     */
    double getKDRatio(@NotNull UUID playerId);

    /**
     * Gets the win rate percentage for a player (cached).
     *
     * @param playerId The player's UUID
     * @return The win rate (0-100), or 0 if not cached
     */
    double getWinRate(@NotNull UUID playerId);
}
