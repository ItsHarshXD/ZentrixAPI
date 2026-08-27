package dev.itsharshxd.zentrix.api.events.corpse;

import dev.itsharshxd.zentrix.api.corpse.ZentrixCorpse;
import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/** Fired when the corpse entity dies, before its configured drop/removal is processed. */
public final class CorpseDeathEvent extends ZentrixGameEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ZentrixCorpse corpse;
    private final Location location;
    public CorpseDeathEvent(@NotNull ZentrixCorpse corpse, @NotNull Location location) {
        super(corpse.getGame()); this.corpse = corpse; this.location = location.clone();
    }
    @NotNull public ZentrixCorpse getCorpse() { return corpse; }
    @NotNull public Location getDeathLocation() { return location.clone(); }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
