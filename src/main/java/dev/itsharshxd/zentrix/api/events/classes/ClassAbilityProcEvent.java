package dev.itsharshxd.zentrix.api.events.classes;

import dev.itsharshxd.zentrix.api.classes.ClassAbilities;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a built-in class ability triggers, after Zentrix decided it applies - the class,
 * the match state and, for chance-based abilities, the roll - and before its effect happens.
 *
 * <p>Listeners can cancel the effect entirely, change its strength through
 * {@link #setMagnitude(double)} where {@link #getMagnitude()} is present, replace the item it hands
 * out where {@link #getItem()} is present, or keep the ability's currency reward from being paid
 * without touching the effect itself. A cancelled proc pays no reward either.
 *
 * <table>
 *   <caption>What each proc carries</caption>
 *   <tr><th>Proc</th><th>Magnitude</th><th>Item</th><th>Target</th></tr>
 *   <tr><td>{@link Proc#TRUE_DAMAGE}</td><td>bonus damage, dealt a tick later</td><td>-</td><td>the victim</td></tr>
 *   <tr><td>{@link Proc#BOW_SHOT_BUFF}</td><td>-</td><td>-</td><td>the entity hit</td></tr>
 *   <tr><td>{@link Proc#CHEST_BONUS}</td><td>-</td><td>the bonus item added to the container</td><td>-</td></tr>
 *   <tr><td>{@link Proc#FALL_DAMAGE_REDUCTION}</td><td>the fraction of fall damage prevented, 0 to 1</td><td>-</td><td>-</td></tr>
 *   <tr><td>{@link Proc#EXTRA_CATCH}</td><td>-</td><td>the extra copy of the catch dropped</td><td>-</td></tr>
 *   <tr><td>{@link Proc#MINERAL_DROP}</td><td>-</td><td>the mineral dropped</td><td>-</td></tr>
 *   <tr><td>{@link Proc#AUTO_SMELT}</td><td>-</td><td>the smelted result given instead of the ore's drops</td><td>-</td></tr>
 * </table>
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class ClassAbilityProcEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Proc proc;
    private final Entity target;
    private double magnitude;
    private ItemStack item;
    private boolean rewarded = true;
    private boolean cancelled;

    public ClassAbilityProcEvent(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull Proc proc,
            @Nullable Entity target,
            double magnitude,
            @Nullable ItemStack item) {
        super(game);
        this.player = Objects.requireNonNull(player, "player");
        this.proc = Objects.requireNonNull(proc, "proc");
        this.target = target;
        this.magnitude = magnitude;
        this.item = item == null ? null : item.clone();
    }

    /** The player whose class ability triggered. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** What triggered. */
    @NotNull
    public Proc getProc() {
        return proc;
    }

    /** The ability type of the class, one of {@link ClassAbilities#TYPES}. */
    @NotNull
    public String getAbilityType() {
        return proc.getAbilityType();
    }

    /** The entity the ability acts on, for the combat procs. */
    @NotNull
    public Optional<Entity> getTarget() {
        return Optional.ofNullable(target);
    }

    /** The proc's strength, for the procs that have one. */
    @NotNull
    public OptionalDouble getMagnitude() {
        return proc.hasMagnitude() ? OptionalDouble.of(magnitude) : OptionalDouble.empty();
    }

    /**
     * Changes the proc's strength.
     *
     * @throws IllegalStateException    for a proc without a magnitude
     * @throws IllegalArgumentException for a negative or non-finite value, or a fall damage
     *                                  reduction above 1
     */
    public void setMagnitude(double magnitude) {
        if (!proc.hasMagnitude()) {
            throw new IllegalStateException(proc + " has no magnitude");
        }
        if (!Double.isFinite(magnitude) || magnitude < 0
                || (proc == Proc.FALL_DAMAGE_REDUCTION && magnitude > 1.0)) {
            throw new IllegalArgumentException("Invalid magnitude for " + proc + ": " + magnitude);
        }
        this.magnitude = magnitude;
    }

    /** A copy of the item the proc hands out, for the procs that hand one out. */
    @NotNull
    public Optional<ItemStack> getItem() {
        return Optional.ofNullable(item).map(ItemStack::clone);
    }

    /**
     * Replaces the item the proc hands out.
     *
     * @throws IllegalStateException    for a proc that hands out no item
     * @throws IllegalArgumentException for air or an empty stack
     */
    public void setItem(@NotNull ItemStack item) {
        if (!proc.hasItem()) {
            throw new IllegalStateException(proc + " hands out no item");
        }
        Objects.requireNonNull(item, "item");
        if (item.getType().isAir() || item.getAmount() <= 0) {
            throw new IllegalArgumentException("The item must not be empty");
        }
        this.item = item.clone();
    }

    /** Whether the ability's configured currency reward will be paid. */
    public boolean isRewarded() {
        return rewarded;
    }

    /** Keeps the currency reward from being paid, or pays it again, without changing the effect. */
    public void setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
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

    /** A built-in class ability trigger. */
    public enum Proc {
        /** Warrior: a melee hit deals delayed bonus damage. */
        TRUE_DAMAGE(ClassAbilities.TRUE_DAMAGE_PROC, true, false),
        /** Archer: a critical arrow hit grants regeneration and speed. */
        BOW_SHOT_BUFF(ClassAbilities.BOW_SHOT_BUFF, false, false),
        /** Looter: the first eligible chest or barrel opened gets a bonus item. */
        CHEST_BONUS(ClassAbilities.CHEST_BONUS, false, true),
        /** Trapper: part of the fall damage is prevented. */
        FALL_DAMAGE_REDUCTION(ClassAbilities.FALL_DAMAGE_REDUCTION, true, false),
        /** Fisherman: a copy of the catch drops. */
        EXTRA_CATCH(ClassAbilities.FISHING_BONUS, false, true),
        /** Fisherman: a mineral drops with the catch. */
        MINERAL_DROP(ClassAbilities.FISHING_BONUS, false, true),
        /** Miner: a mined ore comes out smelted. */
        AUTO_SMELT(ClassAbilities.AUTO_SMELT, false, true);

        private final String abilityType;
        private final boolean magnitude;
        private final boolean item;

        Proc(String abilityType, boolean magnitude, boolean item) {
            this.abilityType = abilityType;
            this.magnitude = magnitude;
            this.item = item;
        }

        /** The ability type it belongs to, one of {@link ClassAbilities#TYPES}. */
        @NotNull
        public String getAbilityType() {
            return abilityType;
        }

        /** Whether it has a magnitude. */
        public boolean hasMagnitude() {
            return magnitude;
        }

        /** Whether it hands out an item. */
        public boolean hasItem() {
            return item;
        }
    }
}
