package dev.itsharshxd.zentrix.api.events.nether;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Optional;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class NetherPreparationCompleteEvent extends ZentrixGameEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final boolean successful;
    private final String failureMessage;
    public NetherPreparationCompleteEvent(@NotNull ZentrixGame game, boolean successful, String failureMessage) {
        super(game); this.successful = successful; this.failureMessage = failureMessage;
    }
    public boolean isSuccessful() { return successful; }
    @NotNull public Optional<String> getFailureMessage() { return Optional.ofNullable(failureMessage); }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
