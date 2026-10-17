package dev.itsharshxd.zentrix.api.item;

import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A source of custom items addressed as {@code <namespace>:<id>}, next to Zentrix's built-in
 * {@code custom:}, {@code itemsadder:} and {@code nexo:} prefixes.
 *
 * <p>Once registered through {@link ItemService#registerItemProvider}, identifiers in the provider's
 * namespace work wherever Zentrix resolves or matches item identifiers through its item manager:
 * {@link ItemService#resolveItem}, {@link ItemService#matchesItem}, and configuration that refers
 * to items by identifier when it is used, such as class bonus items. The {@code material} of
 * entries in {@code content/items.yml} is resolved when that file is read - at Zentrix's startup,
 * before dependent addons enable - so such entries resolve from the next {@code /zentrix reload}
 * after the provider registered. Loot pools store their items themselves and are not affected.
 *
 * <p>Calls come from the main thread and must be quick; a provider that throws is treated as not
 * knowing the item.
 *
 * @since 1.8.0
 */
public interface ItemProvider {

    /**
     * The namespace identifiers of this provider start with, without the colon: lowercase
     * {@code a-z}, {@code 0-9}, {@code _} and {@code -}. {@code custom}, {@code itemsadder},
     * {@code nexo}, {@code minecraft} and {@code zentrix} are reserved.
     */
    @NotNull
    String getNamespace();

    /** Whether the provider can create items now, for instance once its own content has loaded. */
    default boolean isAvailable() {
        return true;
    }

    /**
     * Creates the item with this ID.
     *
     * @param id the part after {@code <namespace>:}
     * @return a new stack, or null when no such item exists
     */
    @Nullable
    ItemStack createItem(@NotNull String id);

    /**
     * The ID of an item this provider made, so Zentrix can match it against an identifier.
     *
     * @return the part after {@code <namespace>:}, or null when the item is not one of this
     *         provider's
     */
    @Nullable
    String identify(@NotNull ItemStack item);

    /** Every item ID the provider knows, without the namespace, for suggestions and pickers. */
    @NotNull
    default Set<String> getItemIds() {
        return Set.of();
    }
}
