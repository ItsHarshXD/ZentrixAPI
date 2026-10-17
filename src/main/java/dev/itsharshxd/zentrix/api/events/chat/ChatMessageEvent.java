package dev.itsharshxd.zentrix.api.events.chat;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called just before Zentrix delivers a chat message it formatted itself.
 *
 * <p>Zentrix formats chat only while {@code chat-formatting} is enabled in {@code settings.yml}.
 * The message has then already been routed: {@link #getRoute()} says which audience it was
 * routed to, and {@link #getRecipients()} holds exactly the players who will receive it. Listeners
 * can rewrite the formatted message, narrow the audience or cancel the delivery. Recipients can
 * only be removed, never added, so a listener cannot carry a message into another match or out of
 * the spectator chat.
 *
 * <p>With chat formatting disabled Zentrix leaves formatting to other chat plugins and only narrows
 * the viewers of Paper's {@code AsyncChatEvent} (at {@code HIGHEST} priority); this event is not
 * fired then.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class ChatMessageEvent extends ZentrixEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player sender;
    private final Route route;
    private final String plainMessage;
    private final LinkedHashSet<Player> recipients;
    private final Set<Player> recipientView;
    private Component message;
    private boolean cancelled;

    public ChatMessageEvent(
            @NotNull Player sender,
            @NotNull Route route,
            @NotNull String plainMessage,
            @NotNull Component message,
            @NotNull Collection<? extends Player> recipients) {
        super(false);
        this.sender = Objects.requireNonNull(sender, "sender");
        this.route = Objects.requireNonNull(route, "route");
        this.plainMessage = Objects.requireNonNull(plainMessage, "plainMessage");
        this.message = Objects.requireNonNull(message, "message");
        this.recipients = new LinkedHashSet<>(recipients);
        this.recipientView = new RemovalOnlySet<>(this.recipients);
    }

    /** The player who wrote the message. */
    @NotNull
    public Player getSender() {
        return sender;
    }

    /** The audience the message was routed to. */
    @NotNull
    public Route getRoute() {
        return route;
    }

    /** What the player typed, as plain text. */
    @NotNull
    public String getPlainMessage() {
        return plainMessage;
    }

    /** The formatted message every recipient will receive. */
    @NotNull
    public Component getMessage() {
        return message;
    }

    /** Replaces the formatted message. */
    public void setMessage(@NotNull Component message) {
        this.message = Objects.requireNonNull(message, "message");
    }

    /**
     * The players who will receive the message, the sender included when they would see their own
     * message. Remove players to keep it from them; adding is not supported and throws
     * {@link UnsupportedOperationException}.
     */
    @NotNull
    public Set<Player> getRecipients() {
        return recipientView;
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

    /** Where Zentrix routed a chat message. */
    public enum Route {
        /** A spectator's message, seen only by spectators of the same match. */
        SPECTATOR,
        /** Party chat, seen by every online party member wherever they are. */
        PARTY,
        /** Team chat, seen by the sender's teammates. */
        TEAM,
        /** Ordinary chat, seen by everyone in the sender's match, or in their world outside one. */
        GLOBAL
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
