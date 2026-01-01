package dev.itsharshxd.zentrix.api.events.player;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.player.ZentrixPlayer;
import dev.itsharshxd.zentrix.api.team.ZentrixTeam;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Called when a player is eliminated (dies) in a Zentrix game.
 * <p>
 * This event is fired after a player dies and is about to be converted
 * to a spectator. At this point:
 * <ul>
 *   <li>The player has died</li>
 *   <li>Death message has been prepared</li>
 *   <li>The player is about to become a spectator</li>
 *   <li>Team elimination may be triggered if this was the last team member</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>NOT cancellable</b>.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onPlayerDeath(PlayerDeathGameEvent event) {
 *     ZentrixPlayer victim = event.getVictim();
 *     ZentrixGame game = event.getGame();
 *
 *     // Check if killed by another player
 *     if (event.hasKiller()) {
 *         ZentrixPlayer killer = event.getKiller().get();
 *         getLogger().info(victim.getName() + " was killed by " + killer.getName());
 *     } else {
 *         getLogger().info(victim.getName() + " died to environmental damage");
 *     }
 *
 *     // Check if this death eliminates a team
 *     if (event.eliminatesTeam()) {
 *         event.getVictimTeam().ifPresent(team -> {
 *             game.broadcast("&c" + team.getDisplayName() + " has been eliminated!");
 *         });
 *     }
 *
 *     // Get death location for custom effects
 *     Location deathLoc = event.getDeathLocation();
 *     deathLoc.getWorld().strikeLightningEffect(deathLoc);
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see PlayerKillEvent
 * @see PlayerLeaveGameEvent
 * @see dev.itsharshxd.zentrix.api.events.team.TeamEliminatedEvent
 */
public class PlayerDeathGameEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final ZentrixPlayer victim;
    private final ZentrixPlayer killer;
    private final ZentrixTeam victimTeam;
    private final Location deathLocation;
    private final DeathCause deathCause;
    private final boolean eliminatesTeam;
    private final int remainingPlayers;
    private final int remainingTeams;

    /**
     * Constructs a new PlayerDeathGameEvent.
     *
     * @param game             The game where the death occurred
     * @param victim           The player who died
     * @param killer           The player who killed the victim (null if environmental death)
     * @param victimTeam       The victim's team (null if not on a team)
     * @param deathLocation    The location where the player died
     * @param deathCause       The cause of death
     * @param eliminatesTeam   Whether this death eliminates the victim's team
     * @param remainingPlayers The number of players remaining after this death
     * @param remainingTeams   The number of teams remaining after this death
     */
    public PlayerDeathGameEvent(@NotNull ZentrixGame game,
                                @NotNull ZentrixPlayer victim,
                                @Nullable ZentrixPlayer killer,
                                @Nullable ZentrixTeam victimTeam,
                                @NotNull Location deathLocation,
                                @NotNull DeathCause deathCause,
                                boolean eliminatesTeam,
                                int remainingPlayers,
                                int remainingTeams) {
        super(game);
        this.victim = victim;
        this.killer = killer;
        this.victimTeam = victimTeam;
        this.deathLocation = deathLocation.clone();
        this.deathCause = deathCause;
        this.eliminatesTeam = eliminatesTeam;
        this.remainingPlayers = remainingPlayers;
        this.remainingTeams = remainingTeams;
    }

    /**
     * Gets the player who died.
     *
     * @return The victim (never null)
     */
    @NotNull
    public ZentrixPlayer getVictim() {
        return victim;
    }

    /**
     * Gets the player who killed the victim, if any.
     * <p>
     * Returns empty if the victim died to environmental damage
     * (fall damage, border damage, drowning, etc.).
     * </p>
     *
     * @return Optional containing the killer, or empty if environmental death
     */
    @NotNull
    public Optional<ZentrixPlayer> getKiller() {
        return Optional.ofNullable(killer);
    }

    /**
     * Checks if the victim was killed by another player.
     *
     * @return {@code true} if there is a killer
     */
    public boolean hasKiller() {
        return killer != null;
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
     * Gets the Bukkit player instance of the killer, if any and online.
     *
     * @return Optional containing the killer's Bukkit player, or empty if no killer or offline
     */
    @NotNull
    public Optional<Player> getKillerBukkit() {
        return killer != null ? killer.getBukkitPlayer() : Optional.empty();
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
     * Gets the killer's name, if any.
     *
     * @return Optional containing the killer's name, or empty if no killer
     */
    @NotNull
    public Optional<String> getKillerName() {
        return killer != null ? Optional.of(killer.getName()) : Optional.empty();
    }

    /**
     * Gets the victim's team.
     *
     * @return Optional containing the victim's team, or empty if not on a team
     */
    @NotNull
    public Optional<ZentrixTeam> getVictimTeam() {
        return Optional.ofNullable(victimTeam);
    }

    /**
     * Gets the location where the player died.
     * <p>
     * Returns a clone to prevent modification of the original location.
     * </p>
     *
     * @return A clone of the death location (never null)
     */
    @NotNull
    public Location getDeathLocation() {
        return deathLocation.clone();
    }

    /**
     * Gets the cause of death.
     *
     * @return The death cause (never null)
     */
    @NotNull
    public DeathCause getDeathCause() {
        return deathCause;
    }

    /**
     * Checks if this death eliminates the victim's team.
     * <p>
     * Returns true if the victim was the last alive member of their team.
     * </p>
     *
     * @return {@code true} if this death eliminates the team
     */
    public boolean eliminatesTeam() {
        return eliminatesTeam;
    }

    /**
     * Gets the number of players remaining after this death.
     *
     * @return Remaining player count
     */
    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    /**
     * Gets the number of teams remaining after this death.
     *
     * @return Remaining team count
     */
    public int getRemainingTeams() {
        return remainingTeams;
    }

    /**
     * Checks if this death was caused by another player.
     *
     * @return {@code true} if killed by another player
     */
    public boolean wasPlayerKill() {
        return deathCause == DeathCause.PLAYER;
    }

    /**
     * Checks if this death was caused by border damage.
     *
     * @return {@code true} if killed by the border
     */
    public boolean wasBorderDeath() {
        return deathCause == DeathCause.BORDER;
    }

    /**
     * Checks if this death was environmental (not by another player).
     *
     * @return {@code true} if environmental death
     */
    public boolean wasEnvironmentalDeath() {
        return deathCause != DeathCause.PLAYER;
    }

    /**
     * Gets the victim's survival time in this game, in seconds.
     *
     * @return Survival time in seconds
     */
    public long getSurvivalTime() {
        return victim.getSurvivalTimeSeconds();
    }

    /**
     * Gets the victim's kill count before dying.
     *
     * @return The victim's kill count
     */
    public int getVictimKills() {
        return victim.getGameKills();
    }

    /**
     * Checks if this death could trigger a win condition.
     * <p>
     * Returns true if only one team remains after this death.
     * </p>
     *
     * @return {@code true} if this death may end the game
     */
    public boolean couldTriggerWin() {
        return remainingTeams <= 1;
    }

    /**
     * Checks if the killer and victim were on the same team.
     * <p>
     * This can happen when friendly fire is enabled.
     * </p>
     *
     * @return {@code true} if this was a team kill
     */
    public boolean wasTeamKill() {
        if (killer == null || victimTeam == null) {
            return false;
        }
        return killer.getTeamId().isPresent() &&
               victim.getTeamId().isPresent() &&
               killer.getTeamId().get().equals(victim.getTeamId().get());
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

    /**
     * Represents the cause of a player's death.
     */
    public enum DeathCause {

        /**
         * Killed by another player.
         */
        PLAYER,

        /**
         * Killed by the world border.
         */
        BORDER,

        /**
         * Killed by fall damage.
         */
        FALL,

        /**
         * Killed by fire or lava.
         */
        FIRE,

        /**
         * Killed by drowning.
         */
        DROWNING,

        /**
         * Killed by an explosion.
         */
        EXPLOSION,

        /**
         * Killed by a mob.
         */
        MOB,

        /**
         * Killed by starvation.
         */
        STARVATION,

        /**
         * Killed by suffocation.
         */
        SUFFOCATION,

        /**
         * Killed by void damage.
         */
        VOID,

        /**
         * Any other cause of death.
         */
        OTHER
    }
}
