package dev.itsharshxd.zentrix.api.events.cosmetics;

import dev.itsharshxd.zentrix.api.cosmetics.CosmeticActionOrigin;
import dev.itsharshxd.zentrix.api.cosmetics.CosmeticTypeKey;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before a cosmetic preview starts for a player, in the cosmetics menu or through
 * {@code CosmeticsService#startPreview}, in any type. Cancelling shows nothing.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.11.0
 */
public final class CosmeticPreviewEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final CosmeticTypeKey type;
    private final String cosmeticId;
    private final CosmeticActionOrigin origin;
    private boolean cancelled;

    public CosmeticPreviewEvent(@NotNull Player player, @NotNull CosmeticTypeKey type,
                                @NotNull String cosmeticId, @NotNull CosmeticActionOrigin origin) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.type = Objects.requireNonNull(type, "type");
        this.cosmeticId = Objects.requireNonNull(cosmeticId, "cosmeticId");
        this.origin = Objects.requireNonNull(origin, "origin");
    }

    /** The player who sees the preview. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The type of the cosmetic previewed. */
    @NotNull
    public CosmeticTypeKey getType() {
        return type;
    }

    /** The cosmetic previewed. */
    @NotNull
    public String getCosmeticId() {
        return cosmeticId;
    }

    /** Where the preview was asked for. */
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
