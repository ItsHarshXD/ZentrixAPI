package dev.itsharshxd.zentrix.api.events.scenario;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Optional;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A scenario misbehaved and Zentrix contained the damage.
 *
 * <p>Failures are counted per match. Once a scenario exceeds the configured limit it is deactivated
 * for that match alone, with {@link #isIsolated()} set, and everything else — the match, the other
 * scenarios, every other arena — carries on untouched.
 *
 * @since 1.7.0
 */
public final class ScenarioFailureEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String scenarioId;
    private final String runtimeId;
    private final String stage;
    private final String message;
    private final Throwable cause;
    private final int failureCount;
    private final boolean isolated;

    public ScenarioFailureEvent(
            @NotNull String scenarioId,
            @Nullable String runtimeId,
            @NotNull String stage,
            @NotNull String message,
            @Nullable Throwable cause,
            int failureCount,
            boolean isolated) {
        this.scenarioId = scenarioId;
        this.runtimeId = runtimeId;
        this.stage = stage;
        this.message = message;
        this.cause = cause;
        this.failureCount = failureCount;
        this.isolated = isolated;
    }

    @NotNull
    public String getScenarioId() {
        return scenarioId;
    }

    /** The match the failure happened in, empty for failures outside any match. */
    @NotNull
    public Optional<String> getRuntimeId() {
        return Optional.ofNullable(runtimeId);
    }

    /** Which part of the scenario failed, for example {@code onActivate} or a hook ID. */
    @NotNull
    public String getStage() {
        return stage;
    }

    @NotNull
    public String getMessage() {
        return message;
    }

    @NotNull
    public Optional<Throwable> getCause() {
        return Optional.ofNullable(cause);
    }

    /** How often this scenario has failed in this match so far. */
    public int getFailureCount() {
        return failureCount;
    }

    /** Whether this failure caused the scenario to be shut down for the match. */
    public boolean isIsolated() {
        return isolated;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
