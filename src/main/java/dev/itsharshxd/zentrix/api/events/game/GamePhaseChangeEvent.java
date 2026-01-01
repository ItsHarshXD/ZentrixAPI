package dev.itsharshxd.zentrix.api.events.game;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.phase.GamePhase;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Called when a game phase changes in a Zentrix Battle Royale game.
 * <p>
 * This event is fired whenever a phase transition occurs, including:
 * <ul>
 *   <li>Normal phase progression (one phase ending and the next starting)</li>
 *   <li>Phase system being paused</li>
 *   <li>Phase system being resumed</li>
 *   <li>Phase system being stopped (game ending)</li>
 * </ul>
 * </p>
 *
 * <p>This event is <b>NOT cancellable</b>.</p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @EventHandler
 * public void onPhaseChange(GamePhaseChangeEvent event) {
 *     ZentrixGame game = event.getGame();
 *     GamePhase newPhase = event.getNewPhase();
 *     PhaseChangeType changeType = event.getChangeType();
 *
 *     if (changeType == PhaseChangeType.PHASE_START) {
 *         String phaseName = newPhase.getDisplayName();
 *         int duration = newPhase.getDuration();
 *
 *         getLogger().info("New phase: " + phaseName + " (" + duration + "s)");
 *
 *         // Check if this is the final shrink phase
 *         if (newPhase.getName().contains("final")) {
 *             game.broadcast("&c&lFINAL PHASE! Get ready for the final fight!");
 *         }
 *
 *         // React to border shrinkage
 *         if (event.hasBorderShrinkage()) {
 *             double targetSize = newPhase.getBorderTargetSize();
 *             game.broadcast("Border shrinking to " + targetSize + " blocks!");
 *         }
 *     }
 * }
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 * @see GameStartEvent
 * @see GameEndEvent
 * @see PhaseChangeType
 */
public class GamePhaseChangeEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final GamePhase newPhase;
    private final GamePhase oldPhase;
    private final PhaseChangeType changeType;

    /**
     * Constructs a new GamePhaseChangeEvent.
     *
     * @param game       The game where the phase change occurred
     * @param newPhase   The new phase that is starting
     * @param oldPhase   The previous phase (may be null for the first phase)
     * @param changeType The type of phase change
     */
    public GamePhaseChangeEvent(@NotNull ZentrixGame game,
                                @NotNull GamePhase newPhase,
                                @Nullable GamePhase oldPhase,
                                @NotNull PhaseChangeType changeType) {
        super(game);
        this.newPhase = newPhase;
        this.oldPhase = oldPhase;
        this.changeType = changeType;
    }

    /**
     * Gets the new phase that is starting.
     *
     * @return The new phase (never null)
     */
    @NotNull
    public GamePhase getNewPhase() {
        return newPhase;
    }

    /**
     * Gets the previous phase that was active.
     * <p>
     * Returns empty if this is the first phase of the game.
     * </p>
     *
     * @return Optional containing the previous phase, or empty if this is the first phase
     */
    @NotNull
    public Optional<GamePhase> getOldPhase() {
        return Optional.ofNullable(oldPhase);
    }

    /**
     * Gets the type of phase change that occurred.
     *
     * @return The change type (never null)
     */
    @NotNull
    public PhaseChangeType getChangeType() {
        return changeType;
    }

    /**
     * Checks if this is the first phase of the game.
     * <p>
     * Returns true when there was no previous phase (oldPhase is null).
     * </p>
     *
     * @return {@code true} if this is the first phase
     */
    public boolean isFirstPhase() {
        return oldPhase == null;
    }

    /**
     * Gets the name of the new phase.
     * <p>
     * Convenience method equivalent to {@code getNewPhase().getName()}.
     * </p>
     *
     * @return The phase name (never null)
     */
    @NotNull
    public String getPhaseName() {
        return newPhase.getName();
    }

    /**
     * Gets the display name of the new phase.
     * <p>
     * Convenience method equivalent to {@code getNewPhase().getDisplayName()}.
     * </p>
     *
     * @return The phase display name (never null)
     */
    @NotNull
    public String getPhaseDisplayName() {
        return newPhase.getDisplayName();
    }

    /**
     * Gets the duration of the new phase in seconds.
     * <p>
     * Convenience method equivalent to {@code getNewPhase().getDuration()}.
     * </p>
     *
     * @return Duration in seconds
     */
    public int getPhaseDuration() {
        return newPhase.getDuration();
    }

    /**
     * Checks if the new phase has border shrinkage enabled.
     * <p>
     * Convenience method equivalent to {@code getNewPhase().hasBorderShrinkage()}.
     * </p>
     *
     * @return {@code true} if the border will shrink during this phase
     */
    public boolean hasBorderShrinkage() {
        return newPhase.hasBorderShrinkage();
    }

    /**
     * Gets the target border size for the new phase.
     * <p>
     * Returns 0 if no border shrinkage is configured.
     * </p>
     *
     * @return Target border diameter in blocks, or 0 if not applicable
     */
    public double getBorderTargetSize() {
        return newPhase.getBorderTargetSize();
    }

    /**
     * Checks if the new phase starts a deathmatch.
     * <p>
     * Convenience method equivalent to {@code getNewPhase().startsDeathmatch()}.
     * </p>
     *
     * @return {@code true} if this phase triggers deathmatch
     */
    public boolean startsDeathmatch() {
        return newPhase.startsDeathmatch();
    }

    /**
     * Checks if this is a normal phase progression event.
     *
     * @return {@code true} if the change type is {@link PhaseChangeType#PHASE_START}
     */
    public boolean isNormalProgression() {
        return changeType == PhaseChangeType.PHASE_START;
    }

    /**
     * Checks if the phase system was paused.
     *
     * @return {@code true} if the change type is {@link PhaseChangeType#PHASE_PAUSED}
     */
    public boolean isPaused() {
        return changeType == PhaseChangeType.PHASE_PAUSED;
    }

    /**
     * Checks if the phase system was resumed.
     *
     * @return {@code true} if the change type is {@link PhaseChangeType#PHASE_RESUMED}
     */
    public boolean isResumed() {
        return changeType == PhaseChangeType.PHASE_RESUMED;
    }

    /**
     * Checks if the phase system was stopped.
     *
     * @return {@code true} if the change type is {@link PhaseChangeType#PHASE_STOPPED}
     */
    public boolean isStopped() {
        return changeType == PhaseChangeType.PHASE_STOPPED;
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
     * Represents the type of phase change that occurred.
     */
    public enum PhaseChangeType {

        /**
         * A new phase is starting normally (phase progression).
         * <p>
         * This is the most common change type, occurring when one phase
         * ends and the next phase begins during normal gameplay.
         * </p>
         */
        PHASE_START,

        /**
         * The phase system was paused.
         * <p>
         * Phase timing and progression are temporarily halted.
         * </p>
         */
        PHASE_PAUSED,

        /**
         * The phase system was resumed from a paused state.
         * <p>
         * Phase timing and progression continue from where they were paused.
         * </p>
         */
        PHASE_RESUMED,

        /**
         * The phase system was stopped.
         * <p>
         * This typically occurs when a game is ending or being forcibly stopped.
         * No further phase progressions will occur.
         * </p>
         */
        PHASE_STOPPED
    }
}
