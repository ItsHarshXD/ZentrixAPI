package dev.itsharshxd.zentrix.api.events.classes;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called before a player is handed their class kit: at match start, and when a revival or
 * {@code RevivalService#restoreLoadout} restores class items.
 *
 * <p>{@link #getItems()} is the mutable list of copies about to be added to the player's
 * inventory; listeners may add, remove or replace items. Initial ability effects - the
 * Fisherman's Luck - follow unless {@link #setApplyEffects(boolean)} turns them off. Cancelling
 * hands out neither.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class ClassKitGrantEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String classType;
    private final Cause cause;
    private final List<ItemStack> items;
    private boolean applyEffects = true;
    private boolean cancelled;

    public ClassKitGrantEvent(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull String classType,
            @NotNull Cause cause,
            @NotNull List<ItemStack> items) {
        super(game);
        this.player = Objects.requireNonNull(player, "player");
        this.classType = Objects.requireNonNull(classType, "classType");
        this.cause = Objects.requireNonNull(cause, "cause");
        this.items = new ArrayList<>(items);
    }

    /** The player receiving the kit. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** Their class, as {@code PlayerClass#getType()} names it, such as {@code WARRIOR}. */
    @NotNull
    public String getClassType() {
        return classType;
    }

    /** Why the kit is handed out. */
    @NotNull
    public Cause getCause() {
        return cause;
    }

    /** The items about to be added, in order; listeners may change the list. */
    @NotNull
    public List<ItemStack> getItems() {
        return items;
    }

    /** Whether the class's initial ability effects are applied with the kit. */
    public boolean isApplyEffects() {
        return applyEffects;
    }

    /** Applies or skips the class's initial ability effects. */
    public void setApplyEffects(boolean applyEffects) {
        this.applyEffects = applyEffects;
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

    /** Why a class kit is handed out. */
    public enum Cause {
        /** The match started. */
        MATCH_START,
        /** A revival, or {@code RevivalService#restoreLoadout}, restored class items. */
        RESTORATION
    }
}
