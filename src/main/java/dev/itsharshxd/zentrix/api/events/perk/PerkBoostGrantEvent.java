package dev.itsharshxd.zentrix.api.events.perk;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.perk.PerkBoost;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called after a player was given a temporary boost and it was stored.
 *
 * <p>Main thread, on the server that gave it; the player may be offline. Only fired while a
 * listener is registered.
 *
 * @since 1.16.0
 */
public final class PerkBoostGrantEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final PerkBoost boost;

    public PerkBoostGrantEvent(@NotNull UUID playerId, @NotNull PerkBoost boost) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.boost = Objects.requireNonNull(boost, "boost");
    }

    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    /** The player, while online on this server. */
    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(playerId));
    }

    @NotNull
    public PerkBoost getBoost() {
        return boost;
    }

    /** Who or what gave it. */
    @NotNull
    public String getSource() {
        return boost.source();
    }

    /** When it ends. */
    @NotNull
    public Instant getExpiresAt() {
        return boost.expiresAt();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
