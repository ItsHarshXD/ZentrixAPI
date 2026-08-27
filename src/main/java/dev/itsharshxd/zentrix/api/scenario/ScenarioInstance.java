package dev.itsharshxd.zentrix.api.scenario;

import dev.itsharshxd.zentrix.api.phase.GamePhase;
import dev.itsharshxd.zentrix.api.world.GameWorldType;
import java.util.Optional;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * One scenario running in one match.
 *
 * <p>Zentrix creates an instance per game, so a scenario's runtime state never leaks between
 * simultaneous matches. Every callback runs on the server main thread.
 *
 * <p>All methods are optional. Implement only the moments the scenario cares about; the rest do
 * nothing.
 *
 * <p>Anything a callback registers through its {@link ScenarioContext} is released automatically
 * afterwards, so {@link #onDeactivate(ScenarioContext, DeactivationReason)} only has to undo what
 * the scenario did outside the context.
 *
 * <p>Exceptions thrown from any callback are caught, logged against the scenario and — for repeated
 * failures — end with the scenario being deactivated for that one match. The match, the other
 * scenarios and every other arena keep running.
 *
 * @since 1.6.0
 */
public interface ScenarioInstance {

    /** Why a scenario stopped running in a match. */
    enum DeactivationReason {
        /** The match finished normally. */
        GAME_END,
        /** The match was cancelled before it started. */
        GAME_CANCELLED,
        /** The scenario was switched off while the match was running. */
        DISABLED,
        /** The providing plugin was disabled. */
        PROVIDER_DISABLED,
        /** The scenario failed repeatedly and was isolated. */
        FAILED,
        /** The server is shutting down. */
        SHUTDOWN
    }

    /**
     * The scenario has been locked in for this match and may start setting up.
     *
     * <p>Called once, before the match leaves the waiting lobby. This is where listeners, overrides
     * and tasks belong.
     */
    default void onActivate(@NotNull ScenarioContext context) {
    }

    /** The match has entered the playing state and the arena is live. */
    default void onGameStart(@NotNull ScenarioContext context) {
    }

    /**
     * One of the match's worlds has been prepared and had Zentrix's own setup applied to it.
     *
     * <p>Raised for the arena as the match goes live and again for every world the match creates
     * later — its Nether copy, its End copy, its deathmatch arena — always <em>after</em> Zentrix
     * reset that world's weather, time and game rules. A scenario that pins a world-level property
     * belongs here rather than in {@link #onGameStart(ScenarioContext)}, because otherwise the
     * preparation of a world would overwrite what the scenario had already applied.
     *
     * <p>The same world may be reported more than once; treat the callback as "apply your settings
     * to this world now" rather than as a one-off.
     */
    default void onWorldPrepared(
            @NotNull ScenarioContext context,
            @NotNull GameWorldType type,
            @NotNull World world) {
    }

    /**
     * A player of the match moved between two of the match's worlds.
     *
     * <p>Raised for arena-to-Nether, arena-to-End, the returns from either, and the move into the
     * deathmatch arena — every transfer where the destination belongs to this match. Transfers that
     * take a player out of the match entirely are reported through
     * {@link #onPlayerLeave(ScenarioContext, Player)} instead.
     *
     * @param from the world role the player came from, empty when they arrived from outside the
     *             match
     * @param to   the world role the player is now in
     */
    default void onPlayerChangeWorld(
            @NotNull ScenarioContext context,
            @NotNull Player player,
            @NotNull Optional<GameWorldType> from,
            @NotNull GameWorldType to) {
    }

    /** The match moved into a new phase. */
    default void onPhaseChange(
            @NotNull ScenarioContext context,
            @NotNull Optional<GamePhase> previous,
            @NotNull GamePhase current) {
    }

    /** A player joined the match, including a reconnecting one. */
    default void onPlayerJoin(@NotNull ScenarioContext context, @NotNull Player player) {
    }

    /** A player left the match, whether by quitting, dying out or being removed. */
    default void onPlayerLeave(@NotNull ScenarioContext context, @NotNull Player player) {
    }

    /** The deathmatch started for this match. */
    default void onDeathmatchStart(@NotNull ScenarioContext context) {
    }

    /**
     * The match ended. Called before {@link #onDeactivate(ScenarioContext, DeactivationReason)} and
     * only for matches that actually ran.
     *
     * @param winningTeamId the winning team, empty when the match ended without one
     */
    default void onGameEnd(@NotNull ScenarioContext context, @NotNull Optional<String> winningTeamId) {
    }

    /**
     * The scenario is being torn down.
     *
     * <p>Everything registered through the context is already being released, so implement this
     * only to undo side effects the scenario created elsewhere. It is always called exactly once
     * for an activated instance, including on cancellation and shutdown.
     */
    default void onDeactivate(@NotNull ScenarioContext context, @NotNull DeactivationReason reason) {
    }
}
