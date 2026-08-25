package dev.itsharshxd.zentrix.api.scenario;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.hook.GameplayHook;
import dev.itsharshxd.zentrix.api.scenario.hook.GameplayHookHandler;
import dev.itsharshxd.zentrix.api.scenario.hook.HookHandle;
import dev.itsharshxd.zentrix.api.world.GameWorldType;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Everything one scenario may do inside one match.
 *
 * <p>A context belongs to exactly one scenario in exactly one game. Every scheduled task, event
 * listener, gameplay override, tracked block, entity and dropped item registered through it is
 * owned by that pair and released automatically when the scenario is deactivated, the match ends,
 * the match is cancelled, or the server shuts down. That is what keeps two simultaneous matches —
 * even two running the same scenario — completely independent, and what guarantees the world is
 * handed back the way Zentrix expects it.
 *
 * <p>The context is not a place to hold long-lived state: use {@link #store()} for anything the
 * scenario needs to remember for the length of the match. Everything in the store is discarded
 * during cleanup.
 *
 * <p>All methods are safe to call from the server main thread. Scheduling helpers may be called
 * from any thread.
 *
 * @since 1.7.0
 */
public interface ScenarioContext {

    // ==========================================
    // Identity
    // ==========================================

    /** The scenario this context belongs to. */
    @NotNull
    ScenarioDescriptor descriptor();

    /** The match this context belongs to. */
    @NotNull
    ZentrixGame game();

    /** The runtime {@code game-*} identifier of the match. */
    @NotNull
    String runtimeId();

    /** The source arena the match was copied from, when known. */
    @NotNull
    Optional<String> sourceArenaName();

    /** The plugin that registered this scenario. */
    @NotNull
    org.bukkit.plugin.Plugin owner();

    /** The settings of this scenario as they apply to this match. */
    @NotNull
    ScenarioSettings settings();

    /** Whether the scenario is still active in this match. */
    boolean isActive();

    // ==========================================
    // Scope
    // ==========================================

    /** Every world that belongs to this match: arena, waiting lobby, Nether, End, deathmatch. */
    @NotNull
    Collection<World> worlds();

    /** One of the match's worlds, empty when it does not exist for this match. */
    @NotNull
    Optional<World> world(@NotNull GameWorldType type);

    /** Whether a world belongs to this match. */
    boolean isInScope(@Nullable World world);

    /**
     * The role a world plays in this match, empty when it belongs to another match or to none.
     *
     * <p>The inverse of {@link #world(GameWorldType)}, and what a listener uses to tell an arena
     * apart from that match's Nether copy or deathmatch arena without hard-coding world names.
     */
    @NotNull
    Optional<GameWorldType> worldType(@Nullable World world);

    /** Whether a location lies in one of the match's worlds. */
    boolean isInScope(@Nullable Location location);

    /**
     * Whether a player is a living participant of this match.
     *
     * <p>Spectators are excluded, which is the check a listener needs before touching anything a
     * player did.
     */
    boolean isParticipant(@Nullable Player player);

    /** Whether an entity belongs to one of the match's worlds. */
    boolean isInScope(@Nullable Entity entity);

    /** The living participants of this match. */
    @NotNull
    Collection<Player> participants();

    // ==========================================
    // Behaviour overrides
    // ==========================================

    /**
     * Takes part in one of Zentrix's gameplay decisions for the length of this match.
     *
     * <p>Handlers are consulted in descending scenario priority. The returned handle is released
     * automatically during cleanup; releasing it earlier restores Zentrix's own behaviour
     * immediately.
     *
     * @param hook    the decision point, from
     *                {@link dev.itsharshxd.zentrix.api.scenario.hook.GameplayHooks} or the
     *                scenario's own
     * @param handler what this scenario decides
     * @return a handle for dropping the override early
     */
    @NotNull
    <R, V> HookHandle override(
            @NotNull GameplayHook<R, V> hook, @NotNull GameplayHookHandler<R, V> handler);

    /**
     * Asks every scenario active in this match about a decision point, in priority order.
     *
     * <p>This is how a scenario dispatches a hook it defined itself, so mechanics Zentrix never
     * anticipated stay composable between scenarios.
     *
     * @param hook    the decision point
     * @param request the request handed to the handlers
     * @return the first non-passing outcome, or {@code pass} when nobody took the decision
     */
    @NotNull
    <R, V> dev.itsharshxd.zentrix.api.scenario.hook.HookOutcome<V> dispatch(
            @NotNull GameplayHook<R, V> hook, @NotNull R request);

    // ==========================================
    // Listeners and scheduling
    // ==========================================

    /**
     * Registers a Bukkit listener for the length of this match.
     *
     * <p>The listener is unregistered during cleanup. It still receives server-wide events, so use
     * {@link #isParticipant(Player)} or {@link #isInScope(World)} to ignore anything happening
     * outside this match.
     */
    void registerListener(@NotNull Listener listener);

    /** Unregisters a listener registered through {@link #registerListener(Listener)}. */
    void unregisterListener(@NotNull Listener listener);

    /** Runs a task on the main thread, skipped if the match ended first. */
    void runTask(@NotNull Runnable task);

    /** Runs a task on the main thread after a delay, cancelled if the match ends first. */
    int runTaskLater(@NotNull Runnable task, long delayTicks);

    /** Runs a repeating main-thread task, cancelled when the match ends. */
    int runTaskTimer(@NotNull Runnable task, long delayTicks, long periodTicks);

    /** Runs a task off the main thread, cancelled if the match ends first. */
    int runTaskAsync(@NotNull Runnable task);

    /** Cancels one task started through this context. */
    void cancelTask(int taskId);

    // ==========================================
    // Tracked world changes
    // ==========================================

    /**
     * Records a block this scenario is about to change so its original state is restored during
     * cleanup.
     *
     * <p>Call this <em>before</em> changing the block. Tracking the same block twice keeps the
     * first snapshot, so the block always returns to what the arena template had.
     */
    void trackBlock(@NotNull Block block);

    /** Records an entity this scenario spawned so it is removed during cleanup. */
    void trackEntity(@NotNull Entity entity);

    /**
     * Drops an item that belongs to this scenario and removes it during cleanup if it is still
     * lying around.
     */
    @NotNull
    Entity dropItem(@NotNull Location location, @NotNull ItemStack item);

    // ==========================================
    // Per-match state
    // ==========================================

    /** A scratch space private to this scenario and this match. */
    @NotNull
    ScenarioStore store();

    /**
     * Reports a recoverable problem against this scenario.
     *
     * <p>The message is logged with the scenario's ID. Repeated failures may lead Zentrix to
     * deactivate the scenario for this match so the rest of the game keeps running.
     */
    void reportFailure(@NotNull String message, @Nullable Throwable cause);

    /** A player's UUID as this match knows it, for convenience in listeners. */
    @NotNull
    default Optional<UUID> participantId(@Nullable Player player) {
        return isParticipant(player) ? Optional.of(player.getUniqueId()) : Optional.empty();
    }
}
