package dev.itsharshxd.zentrix.api.events.broadcast;

import dev.itsharshxd.zentrix.api.broadcast.BroadcastType;
import dev.itsharshxd.zentrix.api.broadcast.GameState;
import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called just before a broadcast is sent, whether its timer came due or it was triggered through
 * {@code BroadcastService#triggerBroadcastNow}.
 *
 * <p>Listeners can narrow the audience - {@link #getRecipients()} allows removing players, never
 * adding them, so a broadcast cannot leak into a match or lobby it was not meant for - replace the
 * text, or cancel this delivery. Cancelling does not give the timer back: the broadcast is next due
 * one interval later, as if it had been sent. Text is in Zentrix's colour format, before formatting.
 * {@link BroadcastSentEvent} follows once it has been sent.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class BroadcastDeliverEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String broadcastId;
    private final BroadcastType type;
    private final Origin origin;
    private final Set<GameState> states;
    private final LinkedHashSet<Player> recipients;
    private final Set<Player> recipientView;
    private List<String> lines;
    private String title;
    private String subtitle;
    private String actionBar;
    private boolean cancelled;

    public BroadcastDeliverEvent(
            @NotNull String broadcastId,
            @NotNull BroadcastType type,
            @NotNull Origin origin,
            @NotNull Collection<GameState> states,
            @NotNull Collection<? extends Player> recipients,
            @NotNull List<String> lines,
            @Nullable String title,
            @Nullable String subtitle,
            @Nullable String actionBar) {
        super(false);
        this.broadcastId = broadcastId;
        this.type = type;
        this.origin = origin;
        this.states = Set.copyOf(states);
        this.recipients = new LinkedHashSet<>(recipients);
        this.recipientView = new RemovalOnlySet<>(this.recipients);
        this.lines = List.copyOf(lines);
        this.title = title;
        this.subtitle = subtitle;
        this.actionBar = actionBar;
    }

    /** The broadcast's id. */
    @NotNull
    public String getBroadcastId() {
        return broadcastId;
    }

    /** How it is shown. */
    @NotNull
    public BroadcastType getType() {
        return type;
    }

    /** Where the broadcast comes from and what sent it now. */
    @NotNull
    public Origin getOrigin() {
        return origin;
    }

    /** The states whose players it was sent to. */
    @NotNull
    public Set<GameState> getStates() {
        return states;
    }

    /**
     * The players it will be sent to. Remove players to keep it from them; adding is not
     * supported and throws {@link UnsupportedOperationException}.
     */
    @NotNull
    public Set<Player> getRecipients() {
        return recipientView;
    }

    /** The chat lines of a {@link BroadcastType#CHAT} broadcast; empty for other types. */
    @NotNull
    public List<String> getLines() {
        return lines;
    }

    /** Replaces the chat lines of a {@link BroadcastType#CHAT} broadcast. */
    public void setLines(@NotNull List<String> lines) {
        this.lines = List.copyOf(Objects.requireNonNull(lines, "lines"));
    }

    /** The title of a {@link BroadcastType#TITLE} broadcast. */
    @NotNull
    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    /** Replaces the title of a {@link BroadcastType#TITLE} broadcast; {@code null} for none. */
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    /** The subtitle of a {@link BroadcastType#TITLE} broadcast. */
    @NotNull
    public Optional<String> getSubtitle() {
        return Optional.ofNullable(subtitle);
    }

    /** Replaces the subtitle of a {@link BroadcastType#TITLE} broadcast; {@code null} for none. */
    public void setSubtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
    }

    /** The text of an {@link BroadcastType#ACTIONBAR} broadcast. */
    @NotNull
    public Optional<String> getActionBar() {
        return Optional.ofNullable(actionBar);
    }

    /** Replaces the text of an {@link BroadcastType#ACTIONBAR} broadcast; {@code null} for none. */
    public void setActionBar(@Nullable String actionBar) {
        this.actionBar = actionBar;
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

    /** Where a broadcast comes from and what sent it. */
    public enum Origin {
        /** A broadcast from {@code gameplay/broadcasts.yml} whose timer came due. */
        CONFIGURED,
        /** A broadcast an addon registered whose timer came due. */
        ADDON,
        /** Sent on demand through {@code BroadcastService#triggerBroadcastNow}. */
        TRIGGERED
    }

    /** A set view that allows removing elements but not adding them. */
    private static final class RemovalOnlySet<E> extends AbstractSet<E> {
        private final Set<E> backing;

        RemovalOnlySet(Set<E> backing) {
            this.backing = backing;
        }

        @NotNull
        @Override
        public Iterator<E> iterator() {
            return backing.iterator();
        }

        @Override
        public int size() {
            return backing.size();
        }

        @Override
        public boolean contains(Object o) {
            return backing.contains(o);
        }

        @Override
        public boolean remove(Object o) {
            return backing.remove(o);
        }
    }
}
