package dev.itsharshxd.zentrix.api.classes;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for accessing player class information within Zentrix.
 * <p>
 * This service provides access to available player classes, their configurations,
 * and player class selections. Classes define starting kits and passive abilities.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ClassService classService = ZentrixProvider.get().getClassService();
 *
 * // Get all available classes
 * Collection<PlayerClass> classes = classService.getAvailableClasses();
 *
 * // Check player's selected class
 * Optional<PlayerClass> selected = classService.getSelectedClass(player);
 * selected.ifPresent(playerClass -> {
 *     String name = playerClass.getDisplayName();
 *     player.sendMessage("Your class: " + name);
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface ClassService {

    /**
     * Gets all available player classes.
     * <p>
     * These are the classes configured in classes.yml that players
     * can select before or during a game.
     * </p>
     *
     * @return An unmodifiable collection of available classes (never null, may be empty)
     */
    @NotNull
    Collection<PlayerClass> getAvailableClasses();

    /**
     * Gets a player class by its type/identifier.
     *
     * @param classType The class type name (case-insensitive)
     * @return Optional containing the class, or empty if not found
     */
    @NotNull
    Optional<PlayerClass> getClass(@NotNull String classType);

    /**
     * Gets a player's currently selected class.
     *
     * @param player The player
     * @return Optional containing the selected class, or empty if none selected
     */
    @NotNull
    Optional<PlayerClass> getSelectedClass(@NotNull Player player);

    /**
     * Gets a player's currently selected class by UUID.
     *
     * @param playerId The player's UUID
     * @return Optional containing the selected class, or empty if none selected
     */
    @NotNull
    Optional<PlayerClass> getSelectedClass(@NotNull UUID playerId);

    /**
     * Checks if a player has selected a class.
     *
     * @param player The player
     * @return {@code true} if the player has a class selected
     */
    boolean hasSelectedClass(@NotNull Player player);

    /**
     * Checks if a player has selected a class by UUID.
     *
     * @param playerId The player's UUID
     * @return {@code true} if the player has a class selected
     */
    boolean hasSelectedClass(@NotNull UUID playerId);

    /**
     * Checks if a player has selected a specific class.
     *
     * @param player    The player
     * @param classType The class type to check
     * @return {@code true} if the player has that class selected
     */
    boolean hasClass(@NotNull Player player, @NotNull String classType);

    /**
     * Checks if a player has selected a specific class by UUID.
     *
     * @param playerId  The player's UUID
     * @param classType The class type to check
     * @return {@code true} if the player has that class selected
     */
    boolean hasClass(@NotNull UUID playerId, @NotNull String classType);

    /**
     * Gets the total number of available classes.
     *
     * @return The class count
     */
    int getClassCount();

    /**
     * Checks if a class type exists.
     *
     * @param classType The class type name
     * @return {@code true} if the class exists
     */
    boolean classExists(@NotNull String classType);

    /**
     * Gets all players who have selected a specific class in any active game.
     *
     * @param classType The class type
     * @return An unmodifiable collection of player UUIDs (never null, may be empty)
     */
    @NotNull
    Collection<UUID> getPlayersWithClass(@NotNull String classType);

    /**
     * Gets the default class that is selected if a player doesn't choose one.
     * <p>
     * Returns empty if no default class is configured.
     * </p>
     *
     * @return Optional containing the default class, or empty if none
     */
    @NotNull
    Optional<PlayerClass> getDefaultClass();

    /**
     * Checks if classes are enabled in the configuration.
     * <p>
     * When disabled, players don't select classes and don't receive class kits.
     * </p>
     *
     * @return {@code true} if the class system is enabled
     */
    boolean isClassSystemEnabled();
}
