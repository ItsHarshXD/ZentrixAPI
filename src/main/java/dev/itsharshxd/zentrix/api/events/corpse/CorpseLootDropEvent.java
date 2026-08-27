package dev.itsharshxd.zentrix.api.events.corpse;

import dev.itsharshxd.zentrix.api.corpse.CorpseRemovalReason;
import dev.itsharshxd.zentrix.api.corpse.ZentrixCorpse;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/** Cancellable event fired before a corpse releases its stored inventory. */
public final class CorpseLootDropEvent extends ZentrixGameEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ZentrixCorpse corpse;
    private final CorpseRemovalReason reason;
    private Location location;
    private ItemStack[] items;
    private boolean cancelled;

    public CorpseLootDropEvent(
            @NotNull ZentrixCorpse corpse,
            @NotNull CorpseRemovalReason reason,
            @NotNull Location location,
            @NotNull ItemStack[] items) {
        super(corpse.getGame());
        this.corpse = corpse; this.reason = reason; this.location = location.clone();
        this.items = copy(items);
    }
    @NotNull public ZentrixCorpse getCorpse() { return corpse; }
    @NotNull public CorpseRemovalReason getReason() { return reason; }
    @NotNull public Location getDropLocation() { return location.clone(); }
    public void setDropLocation(@NotNull Location location) { this.location = location.clone(); }
    @NotNull public ItemStack[] getItems() { return copy(items); }
    public void setItems(@NotNull ItemStack[] items) { this.items = copy(items); }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
    private static ItemStack[] copy(ItemStack[] items) {
        return Arrays.stream(items).map(item -> item == null ? null : item.clone())
                .toArray(ItemStack[]::new);
    }
}
