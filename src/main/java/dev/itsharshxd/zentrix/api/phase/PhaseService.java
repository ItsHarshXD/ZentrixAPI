package dev.itsharshxd.zentrix.api.phase;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Service for accessing game phase information within Zentrix.
 * <p>
 * This service provides access to phase configurations, current phase state,
 * and phase-related queries for active games.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * PhaseService phaseService = ZentrixProvider.get().getPhaseService();
 *
 * // Get all configured phases
 * Collection<GamePhase> phases = phaseService.getAllPhases();
 *
 * // Get current phase for a game
 * Optional<GamePhase> currentPhase = phaseService.getCurrentPhase(game);
 * currentPhase.ifPresent(phase -> {
 *     String name = phase.getName();
 *     int timeLeft = phaseService.getTimeRemaining(game);
 *     player.sendMessage("Phase: " + name + " - Time left: " + timeLeft + "s");
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface PhaseService {

    /**
     * Gets all configured game phases.
     * <p>
     * These are the phases defined in the phases.yml configuration file.
     * The collection is ordered by phase sequence.
     * </p>
     *
     * @return An unmodifiable collection of all phases (never null, may be empty)
     */
    @NotNull
    Collection<GamePhase> getAllPhases();

    /**
     * Gets a phase by its name.
     *
     * @param phaseName The phase name (case-insensitive)
     * @return Optional containing the phase, or empty if not found
     */
    @NotNull
    Optional<GamePhase> getPhase(@NotNull String phaseName);

    /**
     * Gets the current active phase for a game.
     * <p>
     * Returns empty if the game hasn't started or has ended.
     * </p>
     *
     * @param game The game
     * @return Optional containing the current phase, or empty if not in a phase
     */
    @NotNull
    Optional<GamePhase> getCurrentPhase(@NotNull ZentrixGame game);

    /**
     * Gets the index of the current phase for a game (0-based).
     * <p>
     * Returns -1 if no phase is active.
     * </p>
     *
     * @param game The game
     * @return The phase index, or -1 if not in a phase
     */
    int getCurrentPhaseIndex(@NotNull ZentrixGame game);

    /**
     * Gets the time remaining in the current phase, in seconds.
     * <p>
     * Returns 0 if no phase is active.
     * </p>
     *
     * @param game The game
     * @return Seconds remaining in current phase
     */
    int getTimeRemaining(@NotNull ZentrixGame game);

    /**
     * Gets the elapsed time in the current phase, in seconds.
     * <p>
     * Returns 0 if no phase is active.
     * </p>
     *
     * @param game The game
     * @return Seconds elapsed in current phase
     */
    int getTimeElapsed(@NotNull ZentrixGame game);

    /**
     * Gets the total number of configured phases.
     *
     * @return The phase count
     */
    int getPhaseCount();

    /**
     * Gets the next phase after the current one for a game.
     * <p>
     * Returns empty if there is no next phase (current is the last)
     * or if no phase is active.
     * </p>
     *
     * @param game The game
     * @return Optional containing the next phase, or empty if none
     */
    @NotNull
    Optional<GamePhase> getNextPhase(@NotNull ZentrixGame game);

    /**
     * Gets the previous phase before the current one for a game.
     * <p>
     * Returns empty if there is no previous phase (current is the first)
     * or if no phase is active.
     * </p>
     *
     * @param game The game
     * @return Optional containing the previous phase, or empty if none
     */
    @NotNull
    Optional<GamePhase> getPreviousPhase(@NotNull ZentrixGame game);

    /**
     * Checks if a game is currently in a specific phase.
     *
     * @param game      The game
     * @param phaseName The phase name to check
     * @return {@code true} if the game is in the specified phase
     */
    boolean isInPhase(@NotNull ZentrixGame game, @NotNull String phaseName);

    /**
     * Checks if a game has completed all phases.
     * <p>
     * Returns true if the game has finished its last phase.
     * </p>
     *
     * @param game The game
     * @return {@code true} if all phases are complete
     */
    boolean hasCompletedAllPhases(@NotNull ZentrixGame game);

    /**
     * Checks if phase system is paused for a game.
     *
     * @param game The game
     * @return {@code true} if phases are paused
     */
    boolean isPaused(@NotNull ZentrixGame game);

    /**
     * Gets the total duration of all phases combined, in seconds.
     *
     * @return Total phase duration
     */
    int getTotalPhaseDuration();

    /**
     * Gets a phase by its index (0-based).
     *
     * @param index The phase index
     * @return Optional containing the phase, or empty if index is out of bounds
     */
    @NotNull
    Optional<GamePhase> getPhaseByIndex(int index);

    /**
     * Checks if the current phase for a game has border shrinkage enabled.
     *
     * @param game The game
     * @return {@code true} if current phase has border shrinkage
     */
    boolean hasBorderShrinkage(@NotNull ZentrixGame game);

    /**
     * Gets the target border size for the current phase.
     * <p>
     * Returns 0 if no phase is active or no border config exists.
     * </p>
     *
     * @param game The game
     * @return The target border size, or 0 if not applicable
     */
    double getTargetBorderSize(@NotNull ZentrixGame game);
}
