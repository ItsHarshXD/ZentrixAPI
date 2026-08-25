package dev.itsharshxd.zentrix.api.gui;

import java.util.function.Consumer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/** Safe content adapter supplied to a built-in menu extension. */
public interface MenuExtensionContext {
    @NotNull String getMenuId();
    @NotNull Player getPlayer();
    /**
     * Context passed to {@link BuiltInMenuService#open}; empty when the current page was opened by
     * an internal Zentrix navigation path rather than the public service.
     */
    @NotNull BuiltInMenuContext getOpenContext();
    @NotNull GuiLayout getLayout();
    int getSize();
    void setDisplay(int slot, @NotNull ItemStack item);
    void setClickable(
            int slot,
            @NotNull ItemStack item,
            @NotNull Consumer<InventoryClickEvent> onClick);
    void clear(int slot);
}
