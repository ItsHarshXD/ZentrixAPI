package dev.itsharshxd.zentrix.api.events.revival;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired after a phase state or API override changes effective revival access. */
public final class RevivalAccessChangeEvent extends ZentrixGameEvent {

    public enum Source { PHASE, API_OVERRIDE, API_OVERRIDE_CLEARED }

    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean phaseAllowed;
    private final Optional<Boolean> apiOverride;
    private final boolean effectiveAllowed;
    private final Source source;

    public RevivalAccessChangeEvent(
            @NotNull ZentrixGame game,
            boolean phaseAllowed,
            @NotNull Optional<Boolean> apiOverride,
            boolean effectiveAllowed,
            @NotNull Source source) {
        super(game);
        this.phaseAllowed = phaseAllowed;
        this.apiOverride = apiOverride;
        this.effectiveAllowed = effectiveAllowed;
        this.source = source;
    }

    public boolean isPhaseAllowed() { return phaseAllowed; }
    @NotNull public Optional<Boolean> getApiOverride() { return apiOverride; }
    public boolean isEffectiveAllowed() { return effectiveAllowed; }
    @NotNull public Source getSource() { return source; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
