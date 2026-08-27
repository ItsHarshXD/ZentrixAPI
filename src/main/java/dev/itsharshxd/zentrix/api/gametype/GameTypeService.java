package dev.itsharshxd.zentrix.api.gametype;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Manages game types within Zentrix.
 * <p>
 * A game type defines a mode of play, such as Solos, Duos, or Squads, with its
 * team size, player limits, and scoreboards. The service registers and queries
 * game types.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * GameTypeService gameTypeService = ZentrixAPI.get().getGameTypeService();
 *
 * // Register a game type
 * GameTypeBuilder trios = new GameTypeBuilder()
 *     .name("trios")
 *     .teamSize(3)
 *     .minimumPlayers(6)
 *     .maximumPlayers(30)
 *     .startTime(45)
 *     .scoreboard(GameTypeState.WAITING, sb -> sb
 *         .title("&#FFD700&lTRIOS")
 *         .line("&#AAAAAA Players: &#55FF55%players%/%max%"))
 *     .addonId(getAddonId());
 *
 * gameTypeService.registerGameType(trios);
 *
 * // List game types
 * Collection<ZentrixGameType> allTypes = gameTypeService.getAllGameTypes();
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public interface GameTypeService {

    // ==========================================
    // Registration
    // ==========================================

    /**
     * Registers a custom game type from a builder.
     * <p>
     * The registration is kept in memory only.
     * </p>
     *
     * @param builder The game type builder with configuration
     * @return {@code true} if registration was successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    boolean registerGameType(@NotNull GameTypeBuilder builder);

    /**
     * Registers a custom game type asynchronously with persistence.
     * <p>
     * The game type is registered and saved to {@code game-types.yml}.
     * </p>
     *
     * @param builder The game type builder with configuration
     * @return A future that completes with {@code true} if successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    @NotNull
    CompletableFuture<Boolean> registerGameTypeAsync(@NotNull GameTypeBuilder builder);

    /**
     * Unregisters a game type by name.
     * <p>
     * The removal affects memory only.
     * Built-in game types cannot be unregistered.
     * </p>
     *
     * @param gameTypeName The name of the game type to unregister
     * @return {@code true} if the game type was found and removed
     */
    boolean unregisterGameType(@NotNull String gameTypeName);

    /**
     * Updates an existing game type with new configuration.
     * <p>
     * The game type must already exist.
     * </p>
     *
     * @param builder The game type builder with updated configuration
     * @return {@code true} if the game type was found and updated
     */
    boolean updateGameType(@NotNull GameTypeBuilder builder);

    // ==========================================
    // Queries
    // ==========================================

    /**
     * Gets a game type by its name.
     *
     * @param name The game type name (case-insensitive)
     * @return Optional containing the game type, or empty if not found
     */
    @NotNull
    Optional<ZentrixGameType> getGameType(@NotNull String name);

    /**
     * Gets all registered game types.
     *
     * @return An unmodifiable collection of all game types (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixGameType> getAllGameTypes();

    /**
     * Gets all game types registered by a specific addon.
     *
     * @param addonId The addon identifier
     * @return Collection of game types registered by the addon (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixGameType> getGameTypesByAddon(@NotNull String addonId);

    /**
     * Checks if a game type exists with the given name.
     *
     * @param name The game type name to check
     * @return {@code true} if the game type exists
     */
    boolean gameTypeExists(@NotNull String name);

    /**
     * Gets all game type names.
     *
     * @return Collection of game type names (never null, may be empty)
     */
    @NotNull
    Collection<String> getGameTypeNames();

    /**
     * Checks if a game type is built-in (from configuration).
     *
     * @param name The game type name
     * @return {@code true} if the game type is built-in
     */
    boolean isBuiltIn(@NotNull String name);

    // ==========================================
    // Convenience methods
    // ==========================================

    /**
     * Creates a new GameTypeBuilder instance.
     *
     * @return A new GameTypeBuilder
     */
    @NotNull
    default GameTypeBuilder createGameTypeBuilder() {
        return new GameTypeBuilder();
    }
}
