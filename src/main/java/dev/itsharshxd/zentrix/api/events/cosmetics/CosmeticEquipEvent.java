package dev.itsharshxd.zentrix.api.events.cosmetics;

import dev.itsharshxd.zentrix.api.cosmetics.CosmeticActionOrigin;
import dev.itsharshxd.zentrix.api.cosmetics.CosmeticTypeKey;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before a player's own choice of cosmetic is saved, in any type, built-in or an addon's:
 * equipping one, or unequipping to go back to the type's default. Made in the cosmetics menu or by
 * an addon through {@code CosmeticsService#equip} and {@code #unequip}.
 *
 * <p>For built-in categories {@link CosmeticSelectEvent} is called first; cancelling either keeps
 * the current choice. Choices made through {@code CosmeticsService#select} do not fire this event.
 * Cancelling from the menu plays its error sound; telling the player why is up to the listener.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.11.0
 */
public final class CosmeticEquipEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final CosmeticTypeKey type;
    private final String cosmeticId;
    private final String previousId;
    private final CosmeticActionOrigin origin;
    private boolean cancelled;

    public CosmeticEquipEvent(@NotNull Player player, @NotNull CosmeticTypeKey type,
                              @Nullable String cosmeticId, @Nullable String previousId,
                              @NotNull CosmeticActionOrigin origin) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.type = Objects.requireNonNull(type, "type");
        this.cosmeticId = cosmeticId;
        this.previousId = previousId;
        this.origin = Objects.requireNonNull(origin, "origin");
    }

    /** The player choosing. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The type they choose in. */
    @NotNull
    public CosmeticTypeKey getType() {
        return type;
    }

    /** The cosmetic equipped; empty when they unequip, going back to the type's default. */
    @NotNull
    public Optional<String> getCosmeticId() {
        return Optional.ofNullable(cosmeticId);
    }

    /** Whether the player unequips, going back to the type's default. */
    public boolean isUnequip() {
        return cosmeticId == null;
    }

    /** Their stored choice until now; empty when they had none. */
    @NotNull
    public Optional<String> getPreviousId() {
        return Optional.ofNullable(previousId);
    }

    /** Where the choice was made. */
    @NotNull
    public CosmeticActionOrigin getOrigin() {
        return origin;
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
