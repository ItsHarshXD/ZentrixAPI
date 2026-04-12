package dev.itsharshxd.zentrix.api.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a read-only view of a custom item definition.
 * <p>
 * Custom items can be registered by addons and used in game phases,
 * player classes, and other game mechanics. Items support external
 * sources like ItemsAdder and Nexo.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ItemService service = ZentrixAPI.get().getItemService();
 *
 * // Get an item definition
 * Optional<ZentrixItem> item = service.getItemDefinition("tracking-compass");
 * item.ifPresent(def -> {
 *     System.out.println("Item: " + def.getId());
 *     System.out.println("Material: " + def.getMaterial());
 *     System.out.println("Display Name: " + def.getDisplayName());
 *
 *     // Create a usable ItemStack
 *     ItemStack stack = def.toItemStack();
 *     player.getInventory().addItem(stack);
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public interface ZentrixItem {

    /**
     * Gets the unique identifier for this item.
     *
     * @return The item ID (never null)
     */
    @NotNull
    String getId();

    /**
     * Gets the base material for this item.
     * <p>
     * For external items, this may return the fallback material.
     * </p>
     *
     * @return The material (never null)
     */
    @NotNull
    Material getMaterial();

    /**
     * Gets the external source identifier for this item.
     * <p>
     * Format: "itemsadder:namespace:id" or "nexo:id"
     * </p>
     *
     * @return Optional containing the external source, or empty if vanilla/custom
     */
    @NotNull
    Optional<String> getExternalSource();

    /**
     * Checks if this item uses an external source (ItemsAdder, Nexo, etc.).
     *
     * @return {@code true} if external
     */
    boolean isExternal();

    /**
     * Gets the display name for this item.
     *
     * @return The display name with color codes (never null)
     */
    @NotNull
    String getDisplayName();

    /**
     * Gets the lore lines for this item.
     *
     * @return An unmodifiable list of lore lines (never null, may be empty)
     */
    @NotNull
    List<String> getLore();

    /**
     * Gets the default amount for this item.
     *
     * @return The item amount
     */
    int getAmount();

    /**
     * Gets the enchantments applied to this item.
     *
     * @return An unmodifiable map of enchantments to levels (never null, may be empty)
     */
    @NotNull
    Map<Enchantment, Integer> getEnchantments();

    /**
     * Gets the item flags applied to this item.
     *
     * @return An unmodifiable set of item flags (never null, may be empty)
     */
    @NotNull
    Set<ItemFlag> getFlags();

    /**
     * Gets the custom model data for this item.
     * <p>
     * Returns 0 if not set.
     * </p>
     *
     * @return The custom model data value
     */
    int getCustomModelData();

    /**
     * Checks if this item is unbreakable.
     *
     * @return {@code true} if unbreakable
     */
    boolean isUnbreakable();

    /**
     * Checks if this item has a glow effect.
     *
     * @return {@code true} if glowing
     */
    boolean isGlow();

    /**
     * Gets the preferred inventory slot for this item.
     * <p>
     * Returns -1 if no specific slot is configured.
     * </p>
     *
     * @return The slot index (0-8 for hotbar), or -1 if not specified
     */
    int getSlot();

    /**
     * Checks if this item is enabled.
     *
     * @return {@code true} if enabled
     */
    boolean isEnabled();

    // ==========================================
    // ItemStack Creation
    // ==========================================

    /**
     * Creates a usable ItemStack from this item definition.
     * <p>
     * The returned ItemStack is a clone and safe to modify.
     * </p>
     *
     * @return A new ItemStack (never null)
     */
    @NotNull
    ItemStack toItemStack();

    /**
     * Creates a usable ItemStack with a specific amount.
     * <p>
     * The returned ItemStack is a clone and safe to modify.
     * </p>
     *
     * @param amount The item amount
     * @return A new ItemStack with the specified amount (never null)
     */
    @NotNull
    ItemStack toItemStack(int amount);

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Gets the addon ID that registered this item.
     *
     * @return Optional containing the addon ID, or empty if not addon-registered
     */
    @NotNull
    Optional<String> getAddonId();

    /**
     * Checks if this item was dynamically registered by an addon.
     *
     * @return {@code true} if dynamically registered
     */
    boolean isDynamicallyRegistered();

    /**
     * Gets custom metadata fields for this item.
     *
     * @return An unmodifiable map of custom fields (never null, may be empty)
     */
    @NotNull
    Map<String, Object> getCustomFields();
}
