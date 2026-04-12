package dev.itsharshxd.zentrix.api.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Fluent builder for creating custom items dynamically.
 * <p>
 * This builder allows addon developers to create items that integrate
 * with the Zentrix item system, supporting vanilla items and external
 * sources like ItemsAdder and Nexo.
 * </p>
 *
 * <h2>Example Usage - Vanilla Item</h2>
 * <pre>{@code
 * ItemBuilder compass = new ItemBuilder()
 *     .id("tracking-compass")
 *     .material(Material.COMPASS)
 *     .displayName("&#FFD700&l Tracking Compass")
 *     .lore(
 *         "",
 *         "&#AAAAAA Right-click to track",
 *         "&#AAAAAA the nearest player!",
 *         "",
 *         "&#555555Cooldown: 10s"
 *     )
 *     .enchant(Enchantment.UNBREAKING, 1)
 *     .flag(ItemFlag.HIDE_ENCHANTS)
 *     .glow(true)
 *     .slot(4)
 *     .addonId(getAddonId());
 *
 * itemService.registerItem(compass);
 * }</pre>
 *
 * <h2>Example Usage - External Item</h2>
 * <pre>{@code
 * ItemBuilder customSword = new ItemBuilder()
 *     .id("custom-diamond-sword")
 *     .external("itemsadder:mynamespace:my_sword")
 *     .slot(0)
 *     .addonId(getAddonId());
 *
 * itemService.registerItem(customSword);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 * @see ItemService#registerItem(ItemBuilder)
 */
public class ItemBuilder {

    private String id;
    private Material material = Material.STONE;
    private String external;
    private String displayName;
    private final List<String> lore = new ArrayList<>();
    private int amount = 1;
    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private final Set<ItemFlag> flags = EnumSet.noneOf(ItemFlag.class);
    private int customModelData = 0;
    private boolean unbreakable = false;
    private boolean glow = false;
    private int slot = -1;
    private boolean enabled = true;
    private String addonId;
    private final Map<String, Object> customFields = new HashMap<>();

    /**
     * Creates a new ItemBuilder instance.
     */
    public ItemBuilder() {}

    // ==========================================
    // Core Properties
    // ==========================================

    /**
     * Sets the unique identifier for this item.
     * <p>
     * Item IDs must only contain lowercase letters, numbers, hyphens, and underscores.
     * </p>
     *
     * @param id The item ID (e.g., "tracking-compass", "custom_sword")
     * @return This builder for chaining
     * @throws IllegalArgumentException if the ID is null, empty, or contains invalid characters
     */
    @NotNull
    public ItemBuilder id(@NotNull String id) {
        Objects.requireNonNull(id, "Item ID cannot be null");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be empty");
        }
        String lowerId = id.toLowerCase();
        if (!lowerId.matches("[a-z0-9_-]+")) {
            throw new IllegalArgumentException(
                "Item ID must only contain lowercase letters, numbers, hyphens, and underscores: " + id
            );
        }
        this.id = lowerId;
        return this;
    }

    /**
     * Sets the material for this item.
     *
     * @param material The Bukkit material
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder material(@NotNull Material material) {
        Objects.requireNonNull(material, "Material cannot be null");
        if (material == Material.AIR) {
            throw new IllegalArgumentException("Material cannot be AIR");
        }
        this.material = material;
        return this;
    }

    /**
     * Sets this item to use an external source (ItemsAdder, Nexo, etc.).
     * <p>
     * Format examples:
     * <ul>
     *   <li>"itemsadder:namespace:item_id"</li>
     *   <li>"nexo:item_id"</li>
     * </ul>
     * </p>
     *
     * @param source The external source identifier
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder external(@NotNull String source) {
        Objects.requireNonNull(source, "External source cannot be null");
        this.external = source;
        return this;
    }

    /**
     * Sets the display name for this item.
     *
     * @param name The display name (supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder displayName(@NotNull String name) {
        Objects.requireNonNull(name, "Display name cannot be null");
        this.displayName = name;
        return this;
    }

    /**
     * Sets the lore for this item.
     *
     * @param lines The lore lines (varargs, supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder lore(@NotNull String... lines) {
        Objects.requireNonNull(lines, "Lore cannot be null");
        this.lore.clear();
        Collections.addAll(this.lore, lines);
        return this;
    }

    /**
     * Adds a lore line to this item.
     *
     * @param line The lore line to add
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder addLore(@NotNull String line) {
        Objects.requireNonNull(line, "Lore line cannot be null");
        this.lore.add(line);
        return this;
    }

    /**
     * Sets the item amount.
     *
     * @param amount The amount (1-64)
     * @return This builder for chaining
     * @throws IllegalArgumentException if amount is not between 1 and 64
     */
    @NotNull
    public ItemBuilder amount(int amount) {
        if (amount < 1 || amount > 64) {
            throw new IllegalArgumentException("Amount must be between 1 and 64: " + amount);
        }
        this.amount = amount;
        return this;
    }

    /**
     * Adds an enchantment to this item.
     *
     * @param enchantment The enchantment
     * @param level       The enchantment level
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder enchant(@NotNull Enchantment enchantment, int level) {
        Objects.requireNonNull(enchantment, "Enchantment cannot be null");
        if (level < 1) {
            throw new IllegalArgumentException("Enchantment level must be at least 1: " + level);
        }
        this.enchantments.put(enchantment, level);
        return this;
    }

    /**
     * Adds item flags to this item.
     *
     * @param flags The item flags (varargs)
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder flag(@NotNull ItemFlag... flags) {
        Objects.requireNonNull(flags, "Flags cannot be null");
        for (ItemFlag flag : flags) {
            if (flag != null) {
                this.flags.add(flag);
            }
        }
        return this;
    }

    /**
     * Sets the custom model data for this item.
     *
     * @param data The custom model data value
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder customModelData(int data) {
        this.customModelData = data;
        return this;
    }

    /**
     * Sets whether this item is unbreakable.
     *
     * @param unbreakable {@code true} to make unbreakable
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    /**
     * Sets whether this item has a glow effect.
     * <p>
     * If true and no enchantments are present, a dummy enchantment
     * will be added with HIDE_ENCHANTS flag.
     * </p>
     *
     * @param glow {@code true} to enable glow
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder glow(boolean glow) {
        this.glow = glow;
        return this;
    }

    /**
     * Sets the preferred inventory slot for this item.
     *
     * @param slot The slot index (0-8 for hotbar, -1 for no preference)
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder slot(int slot) {
        if (slot < -1 || slot > 40) {
            throw new IllegalArgumentException("Slot must be between -1 and 40: " + slot);
        }
        this.slot = slot;
        return this;
    }

    /**
     * Sets whether this item is enabled.
     *
     * @param enabled {@code true} to enable
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Sets the addon ID that owns this item.
     *
     * @param addonId The addon identifier
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder addonId(@NotNull String addonId) {
        Objects.requireNonNull(addonId, "Addon ID cannot be null");
        this.addonId = addonId;
        return this;
    }

    /**
     * Adds a custom metadata field to this item.
     *
     * @param key   The field key
     * @param value The field value (should be serializable)
     * @return This builder for chaining
     */
    @NotNull
    public ItemBuilder customField(@NotNull String key, @Nullable Object value) {
        Objects.requireNonNull(key, "Custom field key cannot be null");
        if (value == null) {
            this.customFields.remove(key);
        } else {
            this.customFields.put(key, value);
        }
        return this;
    }

    // ==========================================
    // Utility Methods
    // ==========================================

    /**
     * Creates a copy of this builder.
     *
     * @return A new ItemBuilder with the same configuration
     */
    @NotNull
    public ItemBuilder copy() {
        ItemBuilder copy = new ItemBuilder();
        copy.id = this.id;
        copy.material = this.material;
        copy.external = this.external;
        copy.displayName = this.displayName;
        copy.lore.addAll(this.lore);
        copy.amount = this.amount;
        copy.enchantments.putAll(this.enchantments);
        copy.flags.addAll(this.flags);
        copy.customModelData = this.customModelData;
        copy.unbreakable = this.unbreakable;
        copy.glow = this.glow;
        copy.slot = this.slot;
        copy.enabled = this.enabled;
        copy.addonId = this.addonId;
        copy.customFields.putAll(this.customFields);
        return copy;
    }

    // ==========================================
    // Getters (for Service implementation)
    // ==========================================

    @Nullable
    public String getId() {
        return id;
    }

    @NotNull
    public Material getMaterial() {
        return material;
    }

    @Nullable
    public String getExternal() {
        return external;
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public List<String> getLore() {
        return new ArrayList<>(lore);
    }

    public int getAmount() {
        return amount;
    }

    @NotNull
    public Map<Enchantment, Integer> getEnchantments() {
        return new HashMap<>(enchantments);
    }

    @NotNull
    public Set<ItemFlag> getFlags() {
        return flags.isEmpty() ? EnumSet.noneOf(ItemFlag.class) : EnumSet.copyOf(flags);
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public boolean isGlow() {
        return glow;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Nullable
    public String getAddonId() {
        return addonId;
    }

    @NotNull
    public Map<String, Object> getCustomFields() {
        return new HashMap<>(customFields);
    }

    // ==========================================
    // Validation
    // ==========================================

    /**
     * Validates that this builder has all required fields set correctly.
     *
     * @throws IllegalStateException if the builder is not valid
     */
    public void validate() throws IllegalStateException {
        if (id == null || id.isEmpty()) {
            throw new IllegalStateException("Item ID is required");
        }
        if (external == null && material == Material.AIR) {
            throw new IllegalStateException("Item must have a material or external source");
        }
    }

    /**
     * Checks if this builder is valid without throwing an exception.
     *
     * @return {@code true} if the builder has all required fields
     */
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ItemBuilder{" +
            "id='" + id + '\'' +
            ", material=" + material +
            ", external='" + external + '\'' +
            ", displayName='" + displayName + '\'' +
            ", amount=" + amount +
            ", slot=" + slot +
            ", enabled=" + enabled +
            ", addonId='" + addonId + '\'' +
            '}';
    }
}
