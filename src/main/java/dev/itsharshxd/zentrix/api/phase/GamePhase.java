package dev.itsharshxd.zentrix.api.phase;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents a read-only view of a game phase configuration.
 * <p>
 * Game phases define the stages of a Battle Royale match, including
 * timing, border behavior, and actions that occur when the phase starts.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * PhaseService phaseService = ZentrixProvider.get().getPhaseService();
 *
 * // Get current phase for a game
 * Optional<GamePhase> phase = phaseService.getCurrentPhase(game);
 * phase.ifPresent(p -> {
 *     String name = p.getName();
 *     String display = p.getDisplayName();
 *     int duration = p.getDuration();
 *
 *     if (p.hasBorderShrinkage()) {
 *         double targetSize = p.getBorderTargetSize();
 *         player.sendMessage("Border shrinking to " + targetSize + " blocks!");
 *     }
 * });
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.0.0
 */
public interface GamePhase {

    /**
     * Gets the internal name of this phase.
     * <p>
     * This is the identifier used in configuration files.
     * </p>
     *
     * @return The phase name (never null)
     */
    @NotNull
    String getName();

    /**
     * Gets the display name of this phase.
     * <p>
     * This is the formatted name shown to players, with color codes.
     * Falls back to the internal name if not set.
     * </p>
     *
     * @return The display name (never null)
     */
    @NotNull
    String getDisplayName();

    /**
     * Gets the duration of this phase in seconds.
     *
     * @return Duration in seconds
     */
    int getDuration();

    /**
     * Checks if this phase has border configuration.
     *
     * @return {@code true} if border settings are configured
     */
    boolean hasBorderConfig();

    /**
     * Checks if the world border shrinks during this phase.
     *
     * @return {@code true} if border shrinkage is enabled
     */
    boolean hasBorderShrinkage();

    /**
     * Gets the target border size after shrinkage.
     * <p>
     * Returns 0 if no border shrinkage is configured.
     * </p>
     *
     * @return Target border diameter in blocks
     */
    double getBorderTargetSize();

    /**
     * Gets the duration of the border shrink animation in seconds.
     * <p>
     * Returns 0 if no border shrinkage is configured.
     * </p>
     *
     * @return Shrink duration in seconds
     */
    int getBorderShrinkDuration();

    /**
     * Gets the damage per block when outside the border.
     * <p>
     * Returns 0 if no border configuration exists.
     * </p>
     *
     * @return Damage per block per second
     */
    double getBorderDamagePerBlock();

    /**
     * Gets the warning time before the phase ends, in seconds.
     * <p>
     * A warning may be shown to players when this time remains.
     * Returns 0 if no warning is configured.
     * </p>
     *
     * @return Warning time in seconds
     */
    int getWarningTime();

    /**
     * Checks if this phase has a warning configured.
     *
     * @return {@code true} if a warning is configured
     */
    boolean hasWarning();

    /**
     * Checks if this phase has any on-start actions configured.
     * <p>
     * Actions can include announcements, titles, sounds, effects, etc.
     * </p>
     *
     * @return {@code true} if actions are configured
     */
    boolean hasOnStartActions();

    /**
     * Gets the number of on-start actions configured for this phase.
     *
     * @return The action count
     */
    int getOnStartActionCount();

    /**
     * Gets the time remaining in this phase.
     * <p>
     * This is only meaningful when retrieved from an active game context.
     * When retrieved from configuration, returns the full duration.
     * </p>
     *
     * @return Time remaining in seconds
     */
    int getTimeRemaining();

    /**
     * Gets the time elapsed in this phase.
     * <p>
     * This is only meaningful when retrieved from an active game context.
     * When retrieved from configuration, returns 0.
     * </p>
     *
     * @return Time elapsed in seconds
     */
    int getTimeElapsed();

    /**
     * Gets the progress through this phase as a percentage.
     * <p>
     * Returns a value between 0.0 (just started) and 1.0 (complete).
     * </p>
     *
     * @return Progress percentage (0.0 to 1.0)
     */
    default double getProgress() {
        int duration = getDuration();
        if (duration <= 0) {
            return 1.0;
        }
        return Math.min(1.0, (double) getTimeElapsed() / duration);
    }

    /**
     * Checks if this phase enables PvP.
     * <p>
     * Some phases may toggle PvP state when they start.
     * </p>
     *
     * @return {@code true} if this phase enables PvP,
     *         {@code false} if it disables PvP,
     *         empty if PvP state is not changed
     */
    @NotNull
    Optional<Boolean> getTogglePvP();

    /**
     * Checks if this phase starts the deathmatch.
     *
     * @return {@code true} if this phase triggers deathmatch
     */
    boolean startsDeathmatch();
}
