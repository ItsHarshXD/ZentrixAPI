package dev.itsharshxd.zentrix.api.events.perk;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.perk.PerkTier;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after a tier a player was given for a time ended: taken back early, such as with
 * {@code /zx perks revoke}, or run out.
 *
 * <p>Main thread. A revocation fires on the server that made it. An expiry fires on the server the
 * player is online on as it runs out, or, when it ran out while they were away, on the server
 * they next join. Only fired while a listener is registered.
 *
 * @since 1.16.0
 */
public final class TimedTierEndEvent extends ZentrixEvent {

    /** Why the grant ended. */
    public enum Reason {
        /** Taken back before its time. */
        REVOKED,
        /** Its time ran out. */
        EXPIRED
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final String tierId;
    private final PerkTier tier;
    private final Instant expiresAt;
    private final String source;
    private final Reason reason;

    public TimedTierEndEvent(@NotNull UUID playerId, @NotNull String tierId, @Nullable PerkTier tier,
                             @NotNull Instant expiresAt, @NotNull String source, @NotNull Reason reason) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.tierId = Objects.requireNonNull(tierId, "tierId");
        this.tier = tier;
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt");
        this.source = Objects.requireNonNull(source, "source");
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

    /** The tier's ID in {@code perks.yml}. */
    @NotNull
    public String getTierId() {
        return tierId;
    }

    /** The tier, or empty while it is disabled or gone. */
    @NotNull
    public Optional<PerkTier> getTier() {
        return Optional.ofNullable(tier);
    }

    /** When the grant was due to end; in the past for an expiry. */
    @NotNull
    public Instant getExpiresAt() {
        return expiresAt;
    }

    /** Who or what gave the grant. */
    @NotNull
    public String getSource() {
        return source;
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
