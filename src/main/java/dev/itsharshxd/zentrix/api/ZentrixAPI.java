package dev.itsharshxd.zentrix.api;

import dev.itsharshxd.zentrix.api.addon.AddonManager;
import dev.itsharshxd.zentrix.api.broadcast.BroadcastService;
import dev.itsharshxd.zentrix.api.chat.ChatChannelService;
import dev.itsharshxd.zentrix.api.classes.ClassService;
import dev.itsharshxd.zentrix.api.currency.CurrencyService;
import dev.itsharshxd.zentrix.api.data.DataService;
import dev.itsharshxd.zentrix.api.game.GameService;
import dev.itsharshxd.zentrix.api.gametype.GameTypeService;
import dev.itsharshxd.zentrix.api.item.ItemService;
import dev.itsharshxd.zentrix.api.locale.LocaleService;
import dev.itsharshxd.zentrix.api.party.PartyService;
import dev.itsharshxd.zentrix.api.phase.PhaseService;
import dev.itsharshxd.zentrix.api.player.PlayerService;
import dev.itsharshxd.zentrix.api.profile.ProfileService;
import dev.itsharshxd.zentrix.api.recipe.RecipeService;
import dev.itsharshxd.zentrix.api.team.TeamService;
import dev.itsharshxd.zentrix.api.world.RuntimeWorldService;
import dev.itsharshxd.zentrix.api.nether.NetherService;
import dev.itsharshxd.zentrix.api.end.EndService;
import dev.itsharshxd.zentrix.api.deathmatch.DeathmatchService;
import dev.itsharshxd.zentrix.api.gamerule.GameRuleService;
import dev.itsharshxd.zentrix.api.arena.ArenaSourceService;
import dev.itsharshxd.zentrix.api.matchmaking.MatchmakingService;
import dev.itsharshxd.zentrix.api.block.BlockMechanicsService;
import dev.itsharshxd.zentrix.api.compass.CompassTrackerService;
import dev.itsharshxd.zentrix.api.cornucopia.CornucopiaService;
import dev.itsharshxd.zentrix.api.corpse.CorpseService;
import dev.itsharshxd.zentrix.api.dragon.DragonTransportService;
import dev.itsharshxd.zentrix.api.gui.BuiltInMenuService;
import dev.itsharshxd.zentrix.api.gui.GuiLayoutService;
import dev.itsharshxd.zentrix.api.loot.CornucopiaLootService;
import dev.itsharshxd.zentrix.api.loot.GameLootService;
import dev.itsharshxd.zentrix.api.loot.LootTableCatalogService;
import dev.itsharshxd.zentrix.api.revival.RevivalService;
import dev.itsharshxd.zentrix.api.scenario.ScenarioService;
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
 * // Get the API instance (recommended)
 * ZentrixAPI api = ZentrixAPI.get();
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
 *   <li>{@link PhaseService} - Game phase information and dynamic registration</li>
 *   <li>{@link DataService} - Data folder and configuration access</li>
 *   <li>{@link RecipeService} - Custom recipe management</li>
 *   <li>{@link BroadcastService} - Broadcast registration and management (since 1.1.0)</li>
 *   <li>{@link GameTypeService} - Game type registration and management (since 1.1.0)</li>
 *   <li>{@link ItemService} - Item registration and management (since 1.1.0)</li>
 *   <li>{@link PartyService} - Party system operations (since 1.2.0)</li>
 *   <li>{@link ChatChannelService} - Chat channel management (since 1.2.0)</li>
 *   <li>{@link RuntimeWorldService} - Runtime-world ownership and shared scopes (since 1.3.0)</li>
 *   <li>{@link NetherService} - Per-game Nether status and access (since 1.3.0)</li>
 *   <li>{@link EndService} - Per-game End status and access (since 1.6.0)</li>
 *   <li>{@link DeathmatchService} - Deathmatch status and startup (since 1.3.0)</li>
 *   <li>{@link GameRuleService} - Typed dynamic game rules (since 1.3.0)</li>
 *   <li>{@link ArenaSourceService} - Dynamic source-arena registration (since 1.4.0)</li>
 *   <li>{@link MatchmakingService} - Atomic group matchmaking (since 1.4.0)</li>
 *   <li>{@link LocaleService} - Active locale and Zentrix text formatting for addons (since 1.5.0)</li>
 *   <li>{@link RevivalService} - Teammate elimination and revival control (since 1.6.0)</li>
 *   <li>{@link CornucopiaLootService} - Cornucopia loot-pool management (since 1.6.0)</li>
 *   <li>{@link GameLootService} - Per-world lazy game-loot management (since 1.6.0)</li>
 *   <li>{@link LootTableCatalogService} - Local and Minecraft loot-table discovery (since 1.6.0)</li>
 *   <li>{@link CornucopiaService} - Cornucopia placement and podium lifecycle (since 1.6.0)</li>
 *   <li>{@link BlockMechanicsService} - Cornucopia/deathmatch block mechanics (since 1.6.0)</li>
 *   <li>{@link CorpseService} - Corpse lifecycle and stored loot (since 1.6.0)</li>
 *   <li>{@link CompassTrackerService} - Teammate compass tracking (since 1.6.0)</li>
 *   <li>{@link BuiltInMenuService} - Built-in menu opening and extensions (since 1.6.0)</li>
 *   <li>{@link GuiLayoutService} - GUI layout resolution and registration (since 1.6.0)</li>
 *   <li>{@link DragonTransportService} - Dragon bus transport and glider deployment (since 1.6.0)</li>
 *   <li>{@link ScenarioService} - Scenario registration, selection, voting and overrides (since 1.7.0)</li>
 *   <li>{@link dev.itsharshxd.zentrix.api.identity.IdentityService} - Packet-level player identity masking (since 1.9.0)</li>
 *   <li>{@link dev.itsharshxd.zentrix.api.elimination.EliminationService} - Deathless eliminations (since 1.9.0)</li>
 *   <li>{@link dev.itsharshxd.zentrix.api.world.SkyDropService} - Airborne player insertion and glider deployment (since 1.10.0)</li>
 * </ul>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ZentrixAPI {

    /**
     * Gets the ZentrixAPI instance.
     * <p>
     * This is the recommended way to access the Zentrix API from anywhere in your addon.
     * </p>
     *
     * <h3>Usage Example</h3>
     * <pre>{@code
     * // Get the API instance
     * ZentrixAPI api = ZentrixAPI.get();
     *
     * // Use services
     * api.getGameService().getActiveGames();
     * api.getCurrencyService().getBalance(player);
     * }</pre>
     *
     * @return The API instance (never null)
     * @throws IllegalStateException if Zentrix is not loaded. Ensure your plugin.yml
     *         has {@code depend: [Zentrix]} to guarantee load order.
     * @since 1.0.0
     */
    @NotNull
    static ZentrixAPI get() {
        return ZentrixProvider.get();
    }

    /**
     * Checks if the ZentrixAPI is available.
     * <p>
     * Use this method to safely check whether the API can be accessed
     * before calling {@link #get()}.
     * </p>
     *
     * <h3>Usage Example</h3>
     * <pre>{@code
     * if (ZentrixAPI.isAvailable()) {
     *     ZentrixAPI api = ZentrixAPI.get();
     *     // Safe to use API
     * }
     * }</pre>
     *
     * @return {@code true} if the API is initialized and ready to use
     * @since 1.0.0
     */
    static boolean isAvailable() {
        return ZentrixProvider.isAvailable();
    }
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

    /**
     * Gets the broadcast service for broadcast management.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Register custom broadcasts</li>
     *   <li>Query existing broadcasts</li>
     *   <li>Control broadcast timing and triggering</li>
     *   <li>Filter broadcasts by game state</li>
     * </ul>
     * </p>
     *
     * @return The broadcast service instance
     * @since 1.1.0
     */
    @NotNull
    BroadcastService getBroadcastService();

    /**
     * Gets the game type service for game type management.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Register custom game types (Trios, etc.)</li>
     *   <li>Query existing game types</li>
     *   <li>Configure game type scoreboards</li>
     *   <li>Manage team sizes and player limits</li>
     * </ul>
     * </p>
     *
     * @return The game type service instance
     * @since 1.1.0
     */
    @NotNull
    GameTypeService getGameTypeService();

    /**
     * Gets the item service for custom item management.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Register custom items</li>
     *   <li>Retrieve items by ID</li>
     *   <li>Resolve items from multiple sources (vanilla, custom, ItemsAdder, Nexo)</li>
     *   <li>Identify items for custom behavior</li>
     * </ul>
     * </p>
     *
     * @return The item service instance
     * @since 1.1.0
     */
    @NotNull
    ItemService getItemService();

    /**
     * Gets the party service for party system operations.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Query party state and membership</li>
     *   <li>Create, disband, and manage parties</li>
     *   <li>Invite, kick, and promote members</li>
     * </ul>
     * </p>
     *
     * @return The party service instance
     * @since 1.2.0
     */
    @NotNull
    PartyService getPartyService();

    /**
     * Gets the chat channel service for chat channel management.
     * <p>
     * Use this service to:
     * <ul>
     *   <li>Get and set player chat channels</li>
     *   <li>Toggle between available channels</li>
     *   <li>Check channel availability (team, party)</li>
     * </ul>
     * </p>
     *
     * @return The chat channel service instance
     * @since 1.2.0
     */
    @NotNull
    ChatChannelService getChatChannelService();

    /** Runtime-world lookup across arena, waiting lobby, Nether, and deathmatch. */
    @NotNull
    default RuntimeWorldService getRuntimeWorldService() {
        throw new UnsupportedOperationException("RuntimeWorldService requires Zentrix API 1.3.0");
    }

    /** Per-game Nether lifecycle and access control. */
    @NotNull
    default NetherService getNetherService() {
        throw new UnsupportedOperationException("NetherService requires Zentrix API 1.3.0");
    }

    /** Per-game End lifecycle and access control. */
    @NotNull
    default EndService getEndService() {
        throw new UnsupportedOperationException("EndService requires Zentrix API 1.6.0");
    }

    /** Deathmatch status and start operations. */
    @NotNull
    default DeathmatchService getDeathmatchService() {
        throw new UnsupportedOperationException("DeathmatchService requires Zentrix API 1.3.0");
    }

    /** Typed dynamic game-rule queries and mutations. */
    @NotNull
    default GameRuleService getGameRuleService() {
        throw new UnsupportedOperationException("GameRuleService requires Zentrix API 1.3.0");
    }

    /** Dynamic registration of externally installed source arenas. */
    @NotNull
    default ArenaSourceService getArenaSourceService() {
        return ArenaSourceService.unsupported();
    }

    /** Atomic existing/pending/new-game matchmaking for addons. */
    @NotNull
    default MatchmakingService getMatchmakingService() {
        return MatchmakingService.unsupported();
    }

    /** Active locale and Zentrix text formatting for addons. */
    @NotNull
    default LocaleService getLocaleService() {
        throw new UnsupportedOperationException("LocaleService requires Zentrix API 1.5.0");
    }

    /** Teammate elimination records, revival overrides, and revival operations. */
    @NotNull
    default RevivalService getRevivalService() {
        throw new UnsupportedOperationException("RevivalService requires Zentrix API 1.6.0");
    }

    /** Cornucopia loot-pool editing, validation, recovery, and eager generation. */
    @NotNull
    default CornucopiaLootService getCornucopiaLootService() {
        throw new UnsupportedOperationException("CornucopiaLootService requires Zentrix API 1.6.0");
    }

    /** Independent main, Nether, End, and deathmatch loot pools. */
    @NotNull
    default GameLootService getGameLootService() {
        throw new UnsupportedOperationException("GameLootService requires Zentrix API 1.6.0");
    }

    /** Local JSON and native Minecraft chest-loot-table catalog. */
    @NotNull
    default LootTableCatalogService getLootTableCatalogService() {
        throw new UnsupportedOperationException("LootTableCatalogService requires Zentrix API 1.6.0");
    }

    /** Cornucopia schematic, podium, cage, countdown, and preparation operations. */
    @NotNull
    default CornucopiaService getCornucopiaService() {
        throw new UnsupportedOperationException("CornucopiaService requires Zentrix API 1.6.0");
    }

    /** Original-block protection and placed-block decay in special arenas. */
    @NotNull
    default BlockMechanicsService getBlockMechanicsService() {
        throw new UnsupportedOperationException("BlockMechanicsService requires Zentrix API 1.6.0");
    }

    /** Corpse spawning, lookup, loot, and removal operations. */
    @NotNull
    default CorpseService getCorpseService() {
        throw new UnsupportedOperationException("CorpseService requires Zentrix API 1.6.0");
    }

    /** Teammate compass item and target operations. */
    @NotNull
    default CompassTrackerService getCompassTrackerService() {
        throw new UnsupportedOperationException("CompassTrackerService requires Zentrix API 1.6.0");
    }

    /** Built-in menu discovery, opening, and addon extensions. */
    @NotNull
    default BuiltInMenuService getBuiltInMenuService() {
        throw new UnsupportedOperationException("BuiltInMenuService requires Zentrix API 1.6.0");
    }

    /** GUI layout discovery, resolution, and runtime registration. */
    @NotNull
    default GuiLayoutService getGuiLayoutService() {
        throw new UnsupportedOperationException("GuiLayoutService requires Zentrix API 1.6.0");
    }

    /** Dragon-bus flight, passenger, endpoint, and Matrix Gliders operations. */
    @NotNull
    default DragonTransportService getDragonTransportService() {
        throw new UnsupportedOperationException("DragonTransportService requires Zentrix API 1.6.0");
    }

    /** Scenario registration, configuration, selection, voting, and gameplay overrides. */
    @NotNull
    default ScenarioService getScenarioService() {
        throw new UnsupportedOperationException("ScenarioService requires Zentrix API 1.7.0");
    }

    /** Packet-level player identity masking: aliases, skins, and how Zentrix renders both. */
    @NotNull
    default dev.itsharshxd.zentrix.api.identity.IdentityService getIdentityService() {
        throw new UnsupportedOperationException("IdentityService requires Zentrix API 1.9.0");
    }

    /** Ending a player's or a team's match without a death, through Zentrix's own pipeline. */
    @NotNull
    default dev.itsharshxd.zentrix.api.elimination.EliminationService getEliminationService() {
        throw new UnsupportedOperationException("EliminationService requires Zentrix API 1.9.0");
    }

    /** Finding a safe point in the air above a world and dropping a player into it, glider and all. */
    @NotNull
    default dev.itsharshxd.zentrix.api.world.SkyDropService getSkyDropService() {
        throw new UnsupportedOperationException("SkyDropService requires Zentrix API 1.10.0");
    }
}
