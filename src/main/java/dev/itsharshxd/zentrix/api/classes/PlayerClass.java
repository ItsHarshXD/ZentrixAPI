package dev.itsharshxd.zentrix.api.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a read-only view of a player class configuration.
 * <p>
 * Player classes define starting equipment and passive abilities that
 * players can select before or during a game. Each class provides a
 * unique playstyle with different kit items and special abilities.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ClassService classService = ZentrixProvider.get().getClassService();
 *
 * // Get a specific class
 * Optional<PlayerClass> warrior = classService.getClass("warrior");
 * warrior.ifPresent(pc -> {
 *     String name = pc.getDisplayName();
 *     String desc = pc.getDescription();
 *     Material icon = pc.getIconMaterial();
 *
 *     player.sendMessage("Class: " + name);
 *     player.sendMessage("Description: " + desc);
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface PlayerClass {

    /**
     * Gets the unique type identifier for this class.
     * <p>
     * This is the internal name used to identify the class
     * (e.g., "WARRIOR", "ARCHER", "MINER").
     * </p>
     *
     * @return The class type identifier (never null)
     */
    @NotNull
    String getType();

    /**
     * Gets the display name of this class.
     * <p>
     * This is the formatted name shown in GUIs and messages,
     * including color codes.
     * </p>
     *
     * @return The display name (never null)
     */
    @NotNull
    String getDisplayName();

    /**
     * Gets the description of this class.
     * <p>
     * A brief explanation of what this class does and its playstyle.
     * </p>
     *
     * @return The description (never null)
     */
    @NotNull
    String getDescription();

    /**
     * Gets the icon material for this class.
     * <p>
     * This material is used to represent the class in selection GUIs.
     * </p>
     *
     * @return The icon material (never null)
     */
    @NotNull
    Material getIconMaterial();

    /**
     * Gets a clone of the icon ItemStack used in GUIs.
     * <p>
     * This may be a custom item with special display properties.
     * Returns a clone to prevent modification of the original.
     * </p>
     *
     * @return A clone of the icon ItemStack (never null)
     */
    @NotNull
    ItemStack getIconItem();

    /**
     * Gets the lore lines displayed in the class selection GUI.
     * <p>
     * These lines provide additional information about the class,
     * such as ability details and kit contents.
     * </p>
     *
     * @return An unmodifiable list of lore lines (never null, may be empty)
     */
    @NotNull
    List<String> getLore();

    /**
     * Gets clones of the kit items provided by this class.
     * <p>
     * These items are given to the player when the game starts
     * if they have this class selected. Returns clones to prevent
     * modification of the original items.
     * </p>
     *
     * @return An unmodifiable list of kit item clones (never null, may be empty)
     */
    @NotNull
    List<ItemStack> getKitItems();

    /**
     * Gets the number of items in this class's kit.
     *
     * @return The kit item count
     */
    default int getKitItemCount() {
        return getKitItems().size();
    }

    /**
     * Checks if this class has a passive ability.
     *
     * @return {@code true} if an ability is configured
     */
    boolean hasAbility();

    /**
     * Gets the ability type for this class.
     * <p>
     * Returns null if the class has no ability configured.
     * </p>
     *
     * @return The ability type, or null if none
     */
    @Nullable
    String getAbilityType();

    /**
     * Gets a description of the class's passive ability.
     * <p>
     * Returns an empty string if no ability is configured.
     * </p>
     *
     * @return The ability description (never null)
     */
    @NotNull
    String getAbilityDescription();

    /**
     * Checks if this class has any kit items.
     *
     * @return {@code true} if the class provides kit items
     */
    default boolean hasKitItems() {
        return !getKitItems().isEmpty();
    }

    /**
     * Checks if this class has lore configured.
     *
     * @return {@code true} if lore lines are configured
     */
    default boolean hasLore() {
        return !getLore().isEmpty();
    }
}
