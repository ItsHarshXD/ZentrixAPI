package dev.itsharshxd.zentrix.api.events.perk;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.perk.Perk;
import dev.itsharshxd.zentrix.api.perk.PerkTier;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a perk limit stops a player: hosting one more custom game than their tier allows,
 * making a private game, inviting someone to a full party, using the team changer.
 *
 * <p>The action has already been refused; the player is sent the limit message from
 * {@code locales/en.yml} after this event, naming {@link #getNextTier()} when there is one. A shop
 * plugin can offer that tier, and may suppress the message to send its own.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.9.0
 */
public final class PerkLimitReachedEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Perk perk;
    private final int limit;
    private final PerkTier currentTier;
    private final PerkTier nextTier;
    private boolean messageSuppressed;

    public PerkLimitReachedEvent(@NotNull Player player, @NotNull Perk perk, int limit,
                                 @Nullable PerkTier currentTier, @Nullable PerkTier nextTier) {
        super(false);
        this.player = Objects.requireNonNull(player, "player");
        this.perk = Objects.requireNonNull(perk, "perk");
        this.limit = limit;
        this.currentTier = currentTier;
        this.nextTier = nextTier;
    }

    /** The player who was stopped. */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /** The perk that stopped them. */
    @NotNull
    public Perk getPerk() {
        return perk;
    }

    /**
     * The player's value of a {@link Perk.Type#NUMBER} perk, which they reached; 0 for a toggle
     * perk they are not allowed.
     */
    public int getLimit() {
        return limit;
    }

    /** The tier the player belongs to, or empty when they hold none. */
    @NotNull
    public Optional<PerkTier> getCurrentTier() {
        return Optional.ofNullable(currentTier);
    }

    /** The lowest tier above theirs that would allow the action, or empty when none would. */
    @NotNull
    public Optional<PerkTier> getNextTier() {
        return Optional.ofNullable(nextTier);
    }

    /** Whether the limit message is withheld. */
    public boolean isMessageSuppressed() {
        return messageSuppressed;
    }

    /** Withholds the limit message, for a listener that tells the player itself. */
    public void setMessageSuppressed(boolean messageSuppressed) {
        this.messageSuppressed = messageSuppressed;
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
