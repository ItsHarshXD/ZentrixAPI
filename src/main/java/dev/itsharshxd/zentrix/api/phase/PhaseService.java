package dev.itsharshxd.zentrix.api.phase;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Provides game phase information for Zentrix games.
 * <p>
 * Use it to read phase configurations, current phase state, and other phase
 * data for active games.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * PhaseService phaseService = ZentrixProvider.get().getPhaseService();
 *
 * // List configured phases
 * Collection<GamePhase> phases = phaseService.getAllPhases();
 *
 * // Get the current phase
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
     * Gets the index of a configured phase by name (0-based).
     * <p>
     * The counterpart of {@link #getCurrentPhaseIndex(ZentrixGame)}: comparing the two is how a
     * caller decides whether a match has reached a named point in its sequence yet, without
     * assuming anything about how the server ordered or named its phases.
     * </p>
     *
     * @param phaseName The phase name (case-insensitive)
     * @return The phase index, or -1 when no phase of that name is configured
     * @since 1.6.0
     */
    int getPhaseIndex(@NotNull String phaseName);

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

    // ==========================================
    // Dynamic phase registration (since 1.1.0)
    // ==========================================

    /**
     * Registers a custom phase from a builder.
     * <p>
     * The phase is added at the end of the phase sequence.
     * The registration is kept in memory only.
     * </p>
     *
     * @param builder The phase builder with configuration
     * @return {@code true} if registration was successful
     * @throws IllegalArgumentException if the builder is invalid
     * @since 1.1.0
     */
    boolean registerPhase(@NotNull PhaseBuilder builder);

    /**
     * Registers a custom phase asynchronously with persistence.
     * <p>
     * The phase is added at the end of the phase sequence and
     * saved to the phases.yml configuration file.
     * </p>
     *
     * @param builder The phase builder with configuration
     * @return A future that completes with {@code true} if successful
     * @throws IllegalArgumentException if the builder is invalid
     * @since 1.1.0
     */
    @NotNull
    CompletableFuture<Boolean> registerPhaseAsync(@NotNull PhaseBuilder builder);

    /**
     * Registers a custom phase at a specific index in the phase sequence.
     * <p>
     * This method performs in-memory registration only.
     * </p>
     *
     * @param builder The phase builder with configuration
     * @param index   The index to insert at (0-based)
     * @return {@code true} if registration was successful
     * @throws IllegalArgumentException if the builder is invalid or index is out of bounds
     * @since 1.1.0
     */
    boolean registerPhaseAt(@NotNull PhaseBuilder builder, int index);

    /**
     * Unregisters a phase by name.
     * <p>
     * The removal affects memory only.
     * </p>
     *
     * @param phaseName The name of the phase to unregister
     * @return {@code true} if the phase was found and removed
     * @since 1.1.0
     */
    boolean unregisterPhase(@NotNull String phaseName);

    /**
     * Unregisters a phase and removes it from the configuration file.
     *
     * @param phaseName The name of the phase to unregister
     * @return A future that completes with {@code true} if successful
     * @since 1.1.0
     */
    @NotNull
    CompletableFuture<Boolean> unregisterPhaseAndDelete(@NotNull String phaseName);

    /**
     * Updates an existing phase with new configuration.
     * <p>
     * The phase must already exist. Its configuration is replaced without changing
     * its position in the sequence.
     * </p>
     *
     * @param builder The phase builder with updated configuration
     * @return {@code true} if the phase was found and updated
     * @since 1.1.0
     */
    boolean updatePhase(@NotNull PhaseBuilder builder);

    /**
     * Gets all phases registered by a specific addon.
     *
     * @param addonId The addon identifier
     * @return Collection of phases registered by the addon (never null, may be empty)
     * @since 1.1.0
     */
    @NotNull
    Collection<GamePhase> getPhasesByAddon(@NotNull String addonId);

    /**
     * Creates a new PhaseBuilder instance.
     * <p>
     * Convenience method for creating a builder.
     * </p>
     *
     * @return A new PhaseBuilder
     * @since 1.1.0
     */
    @NotNull
    default PhaseBuilder createPhaseBuilder() {
        return new PhaseBuilder();
    }
}
