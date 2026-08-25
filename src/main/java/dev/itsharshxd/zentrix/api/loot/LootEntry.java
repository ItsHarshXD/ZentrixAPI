package dev.itsharshxd.zentrix.api.loot;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Mutable editor copy of one serialized loot entry. Saving a configuration snapshots every field,
 * so later changes to this object do not mutate the active pool.
 */
public final class LootEntry {

    private final String id;
    private boolean enabled;
    private String serializedItem;
    private ItemStack item;
    private String itemLoadError;
    private LootItemSourceType sourceType;
    private String sourceId;
    private String sourceLoadError;
    private double weight;
    private int minimumTotalAppearances;
    private int maximumTotalAppearances;
    private int maximumAppearancesPerContainer;
    private int minimumQuantity;
    private int maximumQuantity;
    private EnumSet<LootContainerType> allowedContainerTypes;

    public LootEntry(
            @NotNull String id,
            boolean enabled,
            @Nullable String serializedItem,
            @Nullable ItemStack item,
            @Nullable String itemLoadError,
            @NotNull LootItemSourceType sourceType,
            @Nullable String sourceId,
            @Nullable String sourceLoadError,
            double weight,
            int minimumTotalAppearances,
            int maximumTotalAppearances,
            int maximumAppearancesPerContainer,
            int minimumQuantity,
            int maximumQuantity,
            @NotNull Set<LootContainerType> allowedContainerTypes) {
        this.id = id;
        this.enabled = enabled;
        this.serializedItem = serializedItem;
        this.item = item == null ? null : item.clone();
        this.itemLoadError = itemLoadError;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.sourceLoadError = sourceLoadError;
        this.weight = weight;
        this.minimumTotalAppearances = minimumTotalAppearances;
        this.maximumTotalAppearances = maximumTotalAppearances;
        this.maximumAppearancesPerContainer = maximumAppearancesPerContainer;
        this.minimumQuantity = minimumQuantity;
        this.maximumQuantity = maximumQuantity;
        this.allowedContainerTypes = allowedContainerTypes.isEmpty()
                ? EnumSet.noneOf(LootContainerType.class)
                : EnumSet.copyOf(allowedContainerTypes);
    }

    /** Creates an unsaved copied-item entry with Zentrix's standard limits. */
    @NotNull
    public static LootEntry create(@NotNull ItemStack item) {
        ItemStack stored = item.clone();
        return new LootEntry(
                UUID.randomUUID().toString(), true, null, stored, null,
                LootItemSourceType.COPIED, null, null, 10.0D,
                0, 10, 3, 1,
                Math.max(1, Math.min(stored.getMaxStackSize(), stored.getAmount())),
                EnumSet.allOf(LootContainerType.class));
    }

    @NotNull public LootEntry copy() { return copyWithId(id); }
    @NotNull public LootEntry duplicate() { return copyWithId(UUID.randomUUID().toString()); }

    @NotNull
    public LootEntry copyWithId(@NotNull String newId) {
        return new LootEntry(
                newId, enabled, serializedItem, item, itemLoadError, sourceType, sourceId,
                sourceLoadError, weight, minimumTotalAppearances, maximumTotalAppearances,
                maximumAppearancesPerContainer, minimumQuantity, maximumQuantity,
                allowedContainerTypes);
    }

    @NotNull public String getId() { return id; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    @Nullable public String getSerializedItem() { return serializedItem; }
    @Nullable public ItemStack getItem() { return item == null ? null : item.clone(); }
    @Nullable public String getItemLoadError() { return itemLoadError; }
    @NotNull public LootItemSourceType getSourceType() { return sourceType; }
    @Nullable public String getSourceId() { return sourceId; }
    @Nullable public String getSourceLoadError() { return sourceLoadError; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public int getMinimumTotalAppearances() { return minimumTotalAppearances; }
    public void setMinimumTotalAppearances(int value) { minimumTotalAppearances = value; }
    public int getMaximumTotalAppearances() { return maximumTotalAppearances; }
    public void setMaximumTotalAppearances(int value) { maximumTotalAppearances = value; }
    public int getMaximumAppearancesPerContainer() { return maximumAppearancesPerContainer; }
    public void setMaximumAppearancesPerContainer(int value) { maximumAppearancesPerContainer = value; }
    public int getMinimumQuantity() { return minimumQuantity; }
    public void setMinimumQuantity(int value) { minimumQuantity = value; }
    public int getMaximumQuantity() { return maximumQuantity; }
    public void setMaximumQuantity(int value) { maximumQuantity = value; }

    @NotNull
    public EnumSet<LootContainerType> getAllowedContainerTypes() {
        return allowedContainerTypes.isEmpty()
                ? EnumSet.noneOf(LootContainerType.class)
                : EnumSet.copyOf(allowedContainerTypes);
    }

    public void setContainerAllowed(@NotNull LootContainerType type, boolean allowed) {
        if (allowed) allowedContainerTypes.add(type); else allowedContainerTypes.remove(type);
    }

    public boolean allows(@NotNull LootContainerType type) {
        return allowedContainerTypes.contains(type);
    }

    /** Used by the service after it safely reserializes a replacement item. */
    public void replaceSerializedItem(
            @Nullable String serializedItem,
            @NotNull ItemStack item,
            @NotNull LootItemSourceType sourceType,
            @Nullable String sourceId) {
        this.serializedItem = serializedItem;
        this.item = item.clone();
        this.itemLoadError = null;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.sourceLoadError = null;
    }
}
