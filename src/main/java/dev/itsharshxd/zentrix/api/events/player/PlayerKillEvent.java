package dev.itsharshxd.zentrix.api.events.player;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Called when a player kills another player in a Zentrix game.
 * <p>
 * This event is fired after a player has killed another player but before
 * kill rewards and statistics are processed. At this point:
 * <ul>
 *   <li>The victim has been eliminated</li>
 *   <li>Kill credit has not yet been awarded</li>
 *   <li>Currency rewards have not yet been given</li>
 *   <li>Statistics have not yet been updated</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>cancellable</b>. Cancelling prevents kill credit,
 * currency rewards, and statistic updates from being processed.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onPlayerKill(PlayerKillEvent event) {
 *     ZentrixPlayer killer = event.getKiller();
 *     ZentrixPlayer victim = event.getVictim();
 *     ZentrixGame game = event.getGame();
 *
 *     // Log the kill
 *     getLogger().info(killer.getName() + " killed " + victim.getName() +
 *                      " in arena " + event.getArenaName());
 *
 *     // Double rewards during final phase
 *     game.getCurrentPhase().ifPresent(phase -> {
 *         if (phase.getName().contains("final")) {
 *             event.setCurrencyReward(event.getCurrencyReward() * 2);
 *             killer.getBukkitPlayer().ifPresent(p -> {
 *                 p.sendMessage("&6Double kill reward during final phase!");
 *             });
 *         }
 *     });
 *
 *     // Award bonus for first blood
 *     if (event.isFirstBlood()) {
 *         event.setCurrencyReward(event.getCurrencyReward() + 50);
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see PlayerDeathGameEvent
 * @see PlayerJoinGameEvent
 */
public class PlayerKillEvent extends ZentrixGameEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixPlayer killer;
    private final ZentrixPlayer victim;
    private final boolean firstBlood;
    private double currencyReward;
    private boolean cancelled;

    /**
     * Constructs a new PlayerKillEvent.
     *
     * @param game           The game where the kill occurred
     * @param killer         The player who got the kill
     * @param victim         The player who was killed
     * @param currencyReward The base currency reward for the kill
     * @param firstBlood     Whether this is the first kill of the game
     */
    public PlayerKillEvent(@NotNull ZentrixGame game,
                           @NotNull ZentrixPlayer killer,
                           @NotNull ZentrixPlayer victim,
                           double currencyReward,
                           boolean firstBlood) {
        super(game);
        this.killer = killer;
        this.victim = victim;
        this.currencyReward = currencyReward;
        this.firstBlood = firstBlood;
        this.cancelled = false;
    }

    /**
     * Gets the player who made the kill.
     *
     * @return The killer (never null)
     */
    @NotNull
    public ZentrixPlayer getKiller() {
        return killer;
    }

    /**
     * Gets the player who was killed.
     *
     * @return The victim (never null)
     */
    @NotNull
    public ZentrixPlayer getVictim() {
        return victim;
    }

    /**
     * Gets the Bukkit player instance of the killer, if online.
     *
     * @return Optional containing the killer's Bukkit player, or empty if offline
     */
    @NotNull
    public Optional<Player> getKillerBukkit() {
        return killer.getBukkitPlayer();
    }

    /**
     * Gets the Bukkit player instance of the victim, if online.
     *
     * @return Optional containing the victim's Bukkit player, or empty if offline
     */
    @NotNull
    public Optional<Player> getVictimBukkit() {
        return victim.getBukkitPlayer();
    }

    /**
     * Gets the killer's name.
     * <p>
     * Convenience method equivalent to {@code getKiller().getName()}.
     * </p>
     *
     * @return The killer's name (never null)
     */
    @NotNull
    public String getKillerName() {
        return killer.getName();
    }

    /**
     * Gets the victim's name.
     * <p>
     * Convenience method equivalent to {@code getVictim().getName()}.
     * </p>
     *
     * @return The victim's name (never null)
     */
    @NotNull
    public String getVictimName() {
        return victim.getName();
    }

    /**
     * Checks if this kill was the first kill of the game (first blood).
     *
     * @return {@code true} if this is the first kill in the game
     */
    public boolean isFirstBlood() {
        return firstBlood;
    }

    /**
     * Gets the currency reward for this kill.
     * <p>
     * This is the amount of currency that will be awarded to the killer.
     * Can be modified by addons before the event completes.
     * </p>
     *
     * @return The currency reward amount
     */
    public double getCurrencyReward() {
        return currencyReward;
    }

    /**
     * Sets the currency reward for this kill.
     * <p>
     * Allows addons to modify the reward amount. Set to 0 to prevent
     * any currency from being awarded (without cancelling the kill credit).
     * </p>
     *
     * @param amount The new reward amount (can be 0 or negative)
     */
    public void setCurrencyReward(double amount) {
        this.currencyReward = amount;
    }

    /**
     * Adds to the currency reward for this kill.
     * <p>
     * Convenience method to add a bonus without knowing the current reward.
     * </p>
     *
     * @param bonus The amount to add (can be negative to reduce)
     */
    public void addCurrencyBonus(double bonus) {
        this.currencyReward += bonus;
    }

    /**
     * Multiplies the currency reward for this kill.
     * <p>
     * Convenience method to apply a multiplier to the current reward.
     * </p>
     *
     * @param multiplier The multiplier to apply
     */
    public void multiplyCurrencyReward(double multiplier) {
        this.currencyReward *= multiplier;
    }

    /**
     * Gets the killer's current kill count in this game (before this kill).
     * <p>
     * Convenience method equivalent to {@code getKiller().getGameKills()}.
     * </p>
     *
     * @return The killer's current kill count
     */
    public int getKillerKillCount() {
        return killer.getGameKills();
    }

    /**
     * Gets the killer's kill count after this kill is credited.
     *
     * @return The killer's kill count after this kill
     */
    public int getKillerKillCountAfter() {
        return killer.getGameKills() + 1;
    }

    /**
     * Gets the killer's current kill streak.
     * <p>
     * Convenience method equivalent to {@code getKiller().getKillStreak()}.
     * </p>
     *
     * @return The killer's current kill streak
     */
    public int getKillerKillStreak() {
        return killer.getKillStreak();
    }

    /**
     * Checks if the killer and victim were on the same team.
     * <p>
     * This should normally not happen if friendly fire is disabled,
     * but can occur if friendly fire is enabled for certain teams.
     * </p>
     *
     * @return {@code true} if killer and victim were teammates
     */
    public boolean wasTeamKill() {
        return killer.getTeamId().isPresent() &&
               victim.getTeamId().isPresent() &&
               killer.getTeamId().get().equals(victim.getTeamId().get());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the handler list for this event.
     *
     * @return The handler list
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the static handler list for this event type.
     *
     * @return The handler list
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
