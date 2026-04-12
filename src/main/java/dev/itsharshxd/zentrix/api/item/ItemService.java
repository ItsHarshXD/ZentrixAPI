package dev.itsharshxd.zentrix.api.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service for managing custom items within Zentrix.
 * <p>
 * This service allows registration, querying, and retrieval of custom items.
 * It supports vanilla items, custom items, and external items from plugins
 * like ItemsAdder and Nexo.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * ItemService itemService = ZentrixAPI.get().getItemService();
 *
 * // Register a custom item
 * ItemBuilder compass = new ItemBuilder()
 *     .id("tracking-compass")
 *     .material(Material.COMPASS)
 *     .displayName("&#FFD700&l Tracking Compass")
 *     .lore("", "&#AAAAAA Right-click to track", "&#AAAAAA the nearest player!")
 *     .enchant(Enchantment.UNBREAKING, 1)
 *     .flag(ItemFlag.HIDE_ENCHANTS)
 *     .glow(true)
 *     .slot(4)
 *     .addonId(getAddonId());
 *
 * itemService.registerItem(compass);
 *
 * // Retrieve and use the item
 * itemService.getItem("tracking-compass").ifPresent(item -> {
 *     player.getInventory().addItem(item);
 * });
 *
 * // Identify items for custom behavior
 * itemService.identifyItem(heldItem).ifPresent(itemId -> {
 *     if (itemId.equals("tracking-compass")) {
 *         // Custom tracking logic
 *     }
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 */
public interface ItemService {

    // ==========================================
    // Registration
    // ==========================================

    /**
     * Registers a custom item from a builder.
     * <p>
     * This method performs in-memory registration only.
     * </p>
     *
     * @param builder The item builder with configuration
     * @return {@code true} if registration was successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    boolean registerItem(@NotNull ItemBuilder builder);

    /**
     * Registers a custom item asynchronously with persistence.
     * <p>
     * The item is registered and saved to the items.yml configuration file.
     * </p>
     *
     * @param builder The item builder with configuration
     * @return A future that completes with {@code true} if successful
     * @throws IllegalArgumentException if the builder is invalid
     */
    @NotNull
    CompletableFuture<Boolean> registerItemAsync(@NotNull ItemBuilder builder);

    /**
     * Registers multiple items at once.
     * <p>
     * This method performs in-memory registration only.
     * </p>
     *
     * @param builders Collection of item builders
     * @return The number of successfully registered items
     */
    int registerItems(@NotNull Collection<ItemBuilder> builders);

    /**
     * Unregisters an item by ID.
     * <p>
     * This method performs in-memory removal only.
     * </p>
     *
     * @param itemId The ID of the item to unregister
     * @return {@code true} if the item was found and removed
     */
    boolean unregisterItem(@NotNull String itemId);

    /**
     * Updates an existing item with new configuration.
     * <p>
     * The item must already exist.
     * </p>
     *
     * @param builder The item builder with updated configuration
     * @return {@code true} if the item was found and updated
     */
    boolean updateItem(@NotNull ItemBuilder builder);

    // ==========================================
    // Retrieval
    // ==========================================

    /**
     * Gets an ItemStack for a registered item.
     * <p>
     * The returned ItemStack is a clone and safe to modify.
     * </p>
     *
     * @param itemId The item ID
     * @return Optional containing the ItemStack, or empty if not found
     */
    @NotNull
    Optional<ItemStack> getItem(@NotNull String itemId);

    /**
     * Gets an ItemStack for a registered item, or a fallback material.
     * <p>
     * The returned ItemStack is a clone and safe to modify.
     * </p>
     *
     * @param itemId   The item ID
     * @param fallback The fallback material if item not found
     * @return The ItemStack (never null)
     */
    @NotNull
    ItemStack getItemOrDefault(@NotNull String itemId, @NotNull Material fallback);

    // ==========================================
    // Universal Resolver
    // ==========================================

    /**
     * Resolves an item from any supported identifier format.
     * <p>
     * Supported formats:
     * <ul>
     *   <li>"custom:item_id" - Custom Zentrix items</li>
     *   <li>"itemsadder:namespace:id" - ItemsAdder items</li>
     *   <li>"nexo:id" - Nexo items</li>
     *   <li>"MATERIAL_NAME" - Vanilla materials</li>
     * </ul>
     * </p>
     *
     * @param itemIdentifier The item identifier
     * @return Optional containing the resolved ItemStack, or empty if not found
     */
    @NotNull
    Optional<ItemStack> resolveItem(@NotNull String itemIdentifier);

    /**
     * Resolves an item from any supported identifier format, with fallback.
     *
     * @param itemIdentifier The item identifier
     * @param fallback       The fallback material if resolution fails
     * @return The ItemStack (never null)
     */
    @NotNull
    ItemStack resolveItemOrDefault(@NotNull String itemIdentifier, @NotNull Material fallback);

    // ==========================================
    // Queries
    // ==========================================

    /**
     * Gets the item definition for a registered item.
     *
     * @param itemId The item ID
     * @return Optional containing the item definition, or empty if not found
     */
    @NotNull
    Optional<ZentrixItem> getItemDefinition(@NotNull String itemId);

    /**
     * Gets all registered item definitions.
     *
     * @return An unmodifiable collection of all items (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixItem> getAllItems();

    /**
     * Gets all items registered by a specific addon.
     *
     * @param addonId The addon identifier
     * @return Collection of items registered by the addon (never null, may be empty)
     */
    @NotNull
    Collection<ZentrixItem> getItemsByAddon(@NotNull String addonId);

    /**
     * Checks if an item exists with the given ID.
     *
     * @param itemId The item ID to check
     * @return {@code true} if the item exists
     */
    boolean itemExists(@NotNull String itemId);

    /**
     * Gets all item IDs.
     *
     * @return Collection of item IDs (never null, may be empty)
     */
    @NotNull
    Collection<String> getItemIds();

    // ==========================================
    // Matching
    // ==========================================

    /**
     * Checks if an ItemStack matches a registered item.
     *
     * @param itemStack The ItemStack to check
     * @param itemId    The item ID to match against
     * @return {@code true} if the ItemStack matches the item definition
     */
    boolean matchesItem(@NotNull ItemStack itemStack, @NotNull String itemId);

    /**
     * Identifies what registered item an ItemStack represents.
     * <p>
     * Useful for implementing custom item behavior in event handlers.
     * </p>
     *
     * @param itemStack The ItemStack to identify
     * @return Optional containing the item ID, or empty if not a registered item
     */
    @NotNull
    Optional<String> identifyItem(@NotNull ItemStack itemStack);

    // ==========================================
    // Slot Configuration
    // ==========================================

    /**
     * Gets the configured slot for an item.
     *
     * @param itemId The item ID
     * @return The slot index, or -1 if not configured or item not found
     */
    int getItemSlot(@NotNull String itemId);

    /**
     * Checks if an item is enabled.
     *
     * @param itemId The item ID
     * @return {@code true} if the item exists and is enabled
     */
    boolean isItemEnabled(@NotNull String itemId);

    // ==========================================
    // Convenience Methods
    // ==========================================

    /**
     * Creates a new ItemBuilder instance.
     *
     * @return A new ItemBuilder
     */
    @NotNull
    default ItemBuilder createItemBuilder() {
        return new ItemBuilder();
    }
}
