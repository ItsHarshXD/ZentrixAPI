package dev.itsharshxd.zentrix.api.events.cosmetics;

import dev.itsharshxd.zentrix.api.cosmetics.CosmeticCategory;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before a player's own choice of cosmetic in the cosmetics menu is saved, and, since
 * 1.11.0, one an addon makes for them through {@code CosmeticsService#equip} or {@code #unequip}.
 * Only for built-in categories; {@link CosmeticEquipEvent}, called next, covers every type.
 *
 * <p>Cancelling keeps their current choice; the menu plays its error sound, and telling the player
 * why is up to the listener. Choices made through {@code CosmeticsService#select} do not fire this
 * event.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CosmeticSelectEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final CosmeticCategory category;
    private final String cosmeticId;
    private boolean cancelled;

    public CosmeticSelectEvent(@NotNull Player player, @NotNull CosmeticCategory category,
                               @Nullable String cosmeticId) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.category = Objects.requireNonNull(category, "category");
        this.cosmeticId = cosmeticId;
    }

    /** The player choosing. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The category they choose in. */
    @NotNull
    public CosmeticCategory getCategory() {
        return category;
    }

    /** The cosmetic chosen; empty when they chose the server's default. */
    @NotNull
    public Optional<String> getCosmeticId() {
        return Optional.ofNullable(cosmeticId);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
