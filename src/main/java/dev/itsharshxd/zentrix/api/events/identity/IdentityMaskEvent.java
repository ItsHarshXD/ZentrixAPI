package dev.itsharshxd.zentrix.api.events.identity;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.identity.MaskedIdentity;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before a player is put behind a masked identity - by Hidden Identities, another scenario
 * or an addon - including when a new mask would replace the one they wear.
 *
 * <p>Listeners may replace the proposed identity or cancel the mask; a cancelled request returns an
 * already released handle and leaves any current mask in place. The alias still has to be free:
 * an alias another player wears refuses the mask after this event, as before.
 *
 * <p>Main thread; only fired while a listener is registered. Real names stay server-side: this
 * event is for plugins, never something to show players.
 *
 * @since 1.8.0
 */
public final class IdentityMaskEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Plugin owner;
    private final String scope;
    private final MaskedIdentity previous;
    private MaskedIdentity identity;
    private boolean cancelled;

    public IdentityMaskEvent(@NotNull Player player, @NotNull Plugin owner, @NotNull String scope,
                             @NotNull MaskedIdentity identity, @Nullable MaskedIdentity previous) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.owner = Objects.requireNonNull(owner, "owner");
        this.scope = Objects.requireNonNull(scope, "scope");
        this.identity = Objects.requireNonNull(identity, "identity");
        this.previous = previous;
    }

    /** The player to be masked. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The plugin asking for the mask. */
    @NotNull
    public Plugin getOwner() {
        return owner;
    }

    /** The runtime ID of the match the mask belongs to. */
    @NotNull
    public String getScope() {
        return scope;
    }

    /** The identity the player would wear. */
    @NotNull
    public MaskedIdentity getIdentity() {
        return identity;
    }

    /** Replaces the identity the player will wear. */
    public void setIdentity(@NotNull MaskedIdentity identity) {
        this.identity = Objects.requireNonNull(identity, "identity");
    }

    /** The identity this mask would replace, when the player already wears one. */
    @NotNull
    public Optional<MaskedIdentity> getPreviousIdentity() {
        return Optional.ofNullable(previous);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
