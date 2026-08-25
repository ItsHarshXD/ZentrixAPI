package dev.itsharshxd.zentrix.api.events.corpse;

import dev.itsharshxd.zentrix.api.corpse.CorpseRemovalReason;
import dev.itsharshxd.zentrix.api.corpse.ZentrixCorpse;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired after a corpse leaves Zentrix tracking. */
public final class CorpseRemoveEvent extends ZentrixGameEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ZentrixCorpse corpse;
    private final CorpseRemovalReason reason;
    public CorpseRemoveEvent(
            @NotNull ZentrixCorpse corpse,
            @NotNull CorpseRemovalReason reason) {
        super(corpse.getGame()); this.corpse = corpse; this.reason = reason;
    }
    @NotNull public ZentrixCorpse getCorpse() { return corpse; }
    @NotNull public CorpseRemovalReason getReason() { return reason; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
