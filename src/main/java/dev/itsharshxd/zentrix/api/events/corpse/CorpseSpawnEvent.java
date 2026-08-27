package dev.itsharshxd.zentrix.api.events.corpse;

import dev.itsharshxd.zentrix.api.corpse.ZentrixCorpse;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired after a corpse entity and its persistent record are registered. */
public final class CorpseSpawnEvent extends ZentrixGameEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ZentrixCorpse corpse;
    public CorpseSpawnEvent(@NotNull ZentrixCorpse corpse) {
        super(corpse.getGame()); this.corpse = corpse;
    }
    @NotNull public ZentrixCorpse getCorpse() { return corpse; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
