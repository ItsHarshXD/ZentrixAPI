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
 * Called after a player was given a tier for a time, such as with {@code /zx perks grant}, and the
 * grant was stored. Given again before it ends, the time is added to what was left, and
 * {@link #getExpiresAt()} is the new end.
 *
 * <p>Main thread, on the server that made the grant; the player may be offline. Only fired while
 * a listener is registered.
 *
 * @since 1.16.0
 */
public final class TimedTierGrantEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final String tierId;
    private final PerkTier tier;
    private final Instant expiresAt;
    private final String source;

    public TimedTierGrantEvent(@NotNull UUID playerId, @NotNull String tierId, @Nullable PerkTier tier,
                               @NotNull Instant expiresAt, @NotNull String source) {
        super(false);
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.tierId = Objects.requireNonNull(tierId, "tierId");
        this.tier = tier;
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt");
        this.source = Objects.requireNonNull(source, "source");
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

    /** The tier, or empty while it is disabled, which gives nothing until it is enabled. */
    @NotNull
    public Optional<PerkTier> getTier() {
        return Optional.ofNullable(tier);
    }

    /** When the grant ends. */
    @NotNull
    public Instant getExpiresAt() {
        return expiresAt;
    }

    /** Who or what gave it, such as an administrator's name or {@code console}. */
    @NotNull
    public String getSource() {
        return source;
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
