package dev.itsharshxd.zentrix.api.events.currency;

import dev.itsharshxd.zentrix.api.currency.CurrencyEventType;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before Zentrix pays one of its built-in rewards, such as a kill, a placement or a class
 * ability trigger, configured in {@code gameplay/economy.yml}.
 *
 * <p>This is the reward policy: change the amount, or cancel to withhold the reward without
 * affecting anything else the trigger does - a cancelled kill reward still counts the kill. It
 * fires once per reward, after Zentrix decided the player earns it and before the balance change is
 * queued; {@link CurrencyChangeEvent} still fires for the resulting change. A reward whose amount is
 * zero after this event is not paid and sends no message.
 *
 * <p>The amount already includes Zentrix's reward multiplier, such as the player's
 * {@code rewards.bonus} perk or a {@code zentrix.multiplier.*} permission.
 * {@link #getBaseAmount()} and {@link #getMultiplier()} expose it, so an addon's boost can replace
 * or build on it deliberately instead of stacking on it unnoticed.
 *
 * <p>Fired on the main thread, and only while a listener is registered.
 *
 * @since 1.8.0
 */
public final class CurrencyRewardEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final CurrencyEventType rewardType;
    private final ZentrixGame game;
    private final double baseAmount;
    private final double configuredAmount;
    private final double multiplier;
    private double amount;
    private boolean cancelled;

    public CurrencyRewardEvent(
            @NotNull Player player,
            @NotNull CurrencyEventType rewardType,
            @Nullable ZentrixGame game,
            double amount) {
        this(player, rewardType, game, amount, 1.0D);
    }

    /**
     * Constructs a reward whose amount already includes Zentrix's reward multiplier.
     *
     * @param baseAmount   the amount before the multiplier
     * @param multiplier   the multiplier Zentrix applied, such as the player's {@code rewards.bonus}
     *                     perk; 1.0 for none
     * @since 1.13.0
     */
    public CurrencyRewardEvent(
            @NotNull Player player,
            @NotNull CurrencyEventType rewardType,
            @Nullable ZentrixGame game,
            double baseAmount,
            double multiplier) {
        super(false);
        if (!Double.isFinite(multiplier) || multiplier < 0) {
            throw new IllegalArgumentException("Reward multiplier must be a finite, non-negative number");
        }
        this.player = player;
        this.rewardType = rewardType;
        this.game = game;
        this.baseAmount = baseAmount;
        this.multiplier = multiplier;
        this.configuredAmount = multiply(baseAmount, multiplier);
        this.amount = configuredAmount;
    }

    /** The player being rewarded. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** Which built-in reward this is. */
    @NotNull
    public CurrencyEventType getRewardType() {
        return rewardType;
    }

    /** The match the reward was earned in, when the player is in one. */
    @NotNull
    public Optional<ZentrixGame> getGame() {
        return Optional.ofNullable(game);
    }

    /**
     * The amount Zentrix decided before any listener, after its {@linkplain #getMultiplier() reward
     * multiplier}: the configured reward, or the amount an earlier, more specific event such as
     * {@code PlayerKillEvent} settled on, raised by the multiplier.
     */
    public double getConfiguredAmount() {
        return configuredAmount;
    }

    /**
     * The amount before Zentrix's reward multiplier: the configured reward, or the amount an earlier,
     * more specific event such as {@code PlayerKillEvent} settled on.
     *
     * @since 1.13.0
     */
    public double getBaseAmount() {
        return baseAmount;
    }

    /**
     * The reward multiplier Zentrix already applied, such as the player's {@code rewards.bonus}
     * perk or a {@code zentrix.multiplier.*} permission. The amount includes it: an addon that
     * boosts the reward can compare against it rather than stack on it unnoticed.
     *
     * @return the multiplier; 1.0 when none
     * @since 1.13.0
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * How much the reward multiplier added to {@linkplain #getBaseAmount() the base amount}, before
     * any listener: {@code getConfiguredAmount() - getBaseAmount()}.
     *
     * @return the bonus; 0 when no multiplier applied
     * @since 1.13.0
     */
    public double getBonusAmount() {
        return configuredAmount - baseAmount;
    }

    /** The amount that will be paid; negative for a penalty. */
    public double getAmount() {
        return amount;
    }

    /**
     * Changes the amount that will be paid.
     *
     * @param amount the amount; negative for a penalty, zero to pay nothing
     * @throws IllegalArgumentException if the amount is not a finite number
     */
    public void setAmount(double amount) {
        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException("Reward amount must be finite");
        }
        this.amount = amount;
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

    /** The same rounding Zentrix's currency rewards use, so a listener sees the amount that pays. */
    private static double multiply(double amount, double multiplier) {
        if (multiplier == 1.0D) {
            return amount;
        }
        return Math.round(amount * multiplier * 100.0D) / 100.0D;
    }
}
