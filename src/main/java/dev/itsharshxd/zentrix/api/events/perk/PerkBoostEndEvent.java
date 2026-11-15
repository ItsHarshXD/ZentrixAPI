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
 * Called after a player's temporary boost ended: removed early, such as with
 * {@code /zx perks clear}, or run out.
 *
 * <p>Main thread. A removal fires on the server that made it. An expiry fires on the server the
 * player is online on as it runs out, or, when it ran out while they were away, on the server
 * they next join. Only fired while a listener is registered.
 *
 * @since 1.16.0
 */
public final class PerkBoostEndEvent extends ZentrixEvent {

    /** Why the boost ended. */
    public enum Reason {
        /** Removed before its time. */
        REMOVED,
        /** Its time ran out. */
        EXPIRED
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final PerkBoost boost;
    private final Reason reason;

    public PerkBoostEndEvent(@NotNull UUID playerId, @NotNull PerkBoost boost, @NotNull Reason reason) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.boost = Objects.requireNonNull(boost, "boost");
        this.reason = Objects.requireNonNull(reason, "reason");
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

    /** Who or what gave the boost. */
    @NotNull
    public String getSource() {
        return boost.source();
    }

    /** When the boost was due to end; in the past for an expiry. */
    @NotNull
    public Instant getExpiresAt() {
        return boost.expiresAt();
    }

    @NotNull
    public Reason getReason() {
        return reason;
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
