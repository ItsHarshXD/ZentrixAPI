package dev.itsharshxd.zentrix.api;

import dev.itsharshxd.zentrix.api.addon.AddonManager;
import dev.itsharshxd.zentrix.api.classes.ClassService;
import dev.itsharshxd.zentrix.api.currency.CurrencyService;
import dev.itsharshxd.zentrix.api.data.DataService;
import dev.itsharshxd.zentrix.api.game.GameService;
import dev.itsharshxd.zentrix.api.phase.PhaseService;
import dev.itsharshxd.zentrix.api.player.PlayerService;
import dev.itsharshxd.zentrix.api.profile.ProfileService;
import dev.itsharshxd.zentrix.api.recipe.RecipeService;
import dev.itsharshxd.zentrix.api.team.TeamService;
import org.jetbrains.annotations.NotNull;

/**
 * Main entry point for the Zentrix Developer API.
 * <p>
 * This API allows third-party developers to create addons that interact
 * with Zentrix Battle Royale games without accessing the core plugin source code.
 * </p>
 *
 * <h2>Getting Started</h2>
 * <pre>{@code
 * // Get the API instance
 * ZentrixAPI api = ZentrixProvider.get();
 *
 * // Access services
 * GameService gameService = api.getGameService();
 * Collection<ZentrixGame> games = gameService.getActiveGames();
 * }</pre>
 *
 * <h2>Available Services</h2>
 * <ul>
 *   <li>{@link GameService} - Game management and queries</li>
 *   <li>{@link PlayerService} - Player-related operations</li>
 *   <li>{@link TeamService} - Team management and queries</li>
 *   <li>{@link ClassService} - Player class information</li>
 *   <li>{@link CurrencyService} - Economy operations</li>
 *   <li>{@link ProfileService} - Player statistics</li>
 *   <li>{@link PhaseService} - Game phase information</li>
 *   <li>{@link DataService} - Data folder and configuration access</li>
 *   <li>{@link RecipeService} - Custom recipe management</li>
 * </ul>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ZentrixAPI {
    /**
     * Gets the API version for compatibility checks.
     * <p>
     * Addons should check this version to ensure compatibility
     * with the installed Zentrix version.
     * </p>
     *
     * @return Semantic version string (e.g., "1.0.0")
     */
    @NotNull
    String getAPIVersion();

    /**
     * Gets the game service for game management operations.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get active games</li>
     *   <li>Find which game a player is in</li>
     *   <li>Query game state and information</li>
     * </ul>
     * </p>
     *
     * @return The game service instance
     */
    @NotNull
    GameService getGameService();

    /**
     * Gets the player service for player-related queries.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get player information within games</li>
     *   <li>Check player states (alive, spectating)</li>
     *   <li>Query player game statistics</li>
     * </ul>
     * </p>
     *
     * @return The player service instance
     */
    @NotNull
    PlayerService getPlayerService();

    /**
     * Gets the team service for team-related operations.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get team information</li>
     *   <li>Check team membership</li>
     *   <li>Query team status (alive, eliminated)</li>
     * </ul>
     * </p>
     *
     * @return The team service instance
     */
    @NotNull
    TeamService getTeamService();

    /**
     * Gets the class service for player class information.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get available player classes</li>
     *   <li>Query class details and abilities</li>
     *   <li>Check player's selected class</li>
     * </ul>
     * </p>
     *
     * @return The class service instance
     */
    @NotNull
    ClassService getClassService();

    /**
     * Gets the currency service for economy operations.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Check player balances</li>
     *   <li>Get currency configuration</li>
     *   <li>Query reward amounts</li>
     * </ul>
     * </p>
     *
     * @return The currency service instance
     */
    @NotNull
    CurrencyService getCurrencyService();

    /**
     * Gets the profile service for player statistics.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get player lifetime statistics</li>
     *   <li>Query wins, kills, deaths, etc.</li>
     *   <li>Access player rankings</li>
     * </ul>
     * </p>
     *
     * @return The profile service instance
     */
    @NotNull
    ProfileService getProfileService();

    /**
     * Gets the phase service for game phase information.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get current phase for a game</li>
     *   <li>Query phase configurations</li>
     *   <li>Check phase timings and borders</li>
     * </ul>
     * </p>
     *
     * @return The phase service instance
     */
    @NotNull
    PhaseService getPhaseService();

    /**
     * Gets the addon manager for addon lifecycle management.
     * <p>
     * Use this to:
     * <ul>
     *   <li>Query registered addons</li>
     *   <li>Check addon status</li>
     * </ul>
     * </p>
     *
     * @return The addon manager instance
     */
    @NotNull
    AddonManager getAddonManager();

    /**
     * Gets the data service for data folder and configuration access.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Access the Zentrix plugin data folder</li>
     *   <li>Create addon-specific data folders</li>
     *   <li>Read and write configuration files</li>
     *   <li>Access Zentrix configuration values (read-only)</li>
     * </ul>
     * </p>
     *
     * @return The data service instance
     */
    @NotNull
    DataService getDataService();

    /**
     * Gets the recipe service for custom recipe management.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Register custom crafting recipes</li>
     *   <li>Query existing recipes</li>
     *   <li>Check player craft limits</li>
     *   <li>Manage recipe restrictions (one-time, craft limits)</li>
     * </ul>
     * </p>
     *
     * @return The recipe service instance
     */
    @NotNull
    RecipeService getRecipeService();
}
