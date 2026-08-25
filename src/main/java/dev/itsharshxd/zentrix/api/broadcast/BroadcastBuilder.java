package dev.itsharshxd.zentrix.api.broadcast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Fluent builder for creating custom broadcasts.
 * <p>
 * Use it to define CHAT, TITLE, and ACTIONBAR broadcasts for Zentrix.
 * </p>
 *
 * <h2>Chat broadcast example</h2>
 * <pre>{@code
 * BroadcastBuilder tips = new BroadcastBuilder()
 *     .id("gameplay-tips")
 *     .type(BroadcastType.CHAT)
 *     .interval(180)
 *     .showIn(GameState.PLAYING)
 *     .messages(
 *         "&#FFD700[TIP] Use sneak to reduce knockback!",
 *         "&#FFD700[TIP] Golden apples give absorption!"
 *     )
 *     .addonId(getAddonId());
 *
 * broadcastService.registerBroadcast(tips);
 * }</pre>
 *
 * <h2>Title broadcast example</h2>
 * <pre>{@code
 * BroadcastBuilder alert = new BroadcastBuilder()
 *     .id("phase-reminder")
 *     .type(BroadcastType.TITLE)
 *     .interval(60)
 *     .showIn(GameState.PLAYING)
 *     .title("&#FF5555Stay Alert!")
 *     .subtitle("&#AAAAAA The border is shrinking!")
 *     .titleTiming(5, 40, 10)
 *     .addonId(getAddonId());
 *
 * broadcastService.registerBroadcast(alert);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 * @see BroadcastService#registerBroadcast(BroadcastBuilder)
 */
public class BroadcastBuilder {

    private String id;
    private boolean enabled = true;
    private int interval = 60;
    private final Set<GameState> showIn = EnumSet.noneOf(GameState.class);
    private BroadcastType type = BroadcastType.CHAT;

    // CHAT fields
    private final List<String> messages = new ArrayList<>();

    // TITLE fields
    private String title;
    private String subtitle;
    private int fadeIn = 10;
    private int stay = 70;
    private int fadeOut = 20;

    // ACTIONBAR fields
    private String actionBar;

    // Metadata
    private String addonId;
    private final Map<String, Object> customFields = new HashMap<>();

    /**
     * Creates a new BroadcastBuilder instance.
     */
    public BroadcastBuilder() {}

    // ==========================================
    // Core properties
    // ==========================================

    /**
     * Sets the unique identifier for this broadcast.
     * <p>
     * Broadcast IDs must only contain lowercase letters, numbers, hyphens, and underscores.
     * </p>
     *
     * @param id The broadcast ID (e.g., "gameplay-tips", "phase_warning")
     * @return This builder for chaining
     * @throws IllegalArgumentException if the ID is null, empty, or contains invalid characters
     */
    @NotNull
    public BroadcastBuilder id(@NotNull String id) {
        Objects.requireNonNull(id, "Broadcast ID cannot be null");
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Broadcast ID cannot be empty");
        }
        String lowerId = id.toLowerCase();
        if (!lowerId.matches("[a-z0-9_-]+")) {
            throw new IllegalArgumentException(
                "Broadcast ID must only contain lowercase letters, numbers, hyphens, and underscores: " + id
            );
        }
        this.id = lowerId;
        return this;
    }

    /**
     * Sets whether this broadcast is enabled.
     *
     * @param enabled {@code true} to enable the broadcast
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * Sets the interval between broadcasts in seconds.
     *
     * @param seconds The interval in seconds (must be positive)
     * @return This builder for chaining
     * @throws IllegalArgumentException if interval is not positive
     */
    @NotNull
    public BroadcastBuilder interval(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Interval must be positive: " + seconds);
        }
        this.interval = seconds;
        return this;
    }

    /**
     * Sets the game states in which this broadcast should be shown.
     *
     * @param states The game states (varargs)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder showIn(@NotNull GameState... states) {
        Objects.requireNonNull(states, "States cannot be null");
        this.showIn.clear();
        for (GameState state : states) {
            if (state != null) {
                this.showIn.add(state);
            }
        }
        return this;
    }

    /**
     * Adds game states to show this broadcast in.
     *
     * @param state The game state to add
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder addShowIn(@NotNull GameState state) {
        Objects.requireNonNull(state, "State cannot be null");
        this.showIn.add(state);
        return this;
    }

    /**
     * Sets the broadcast type.
     *
     * @param type The broadcast type (CHAT, TITLE, or ACTIONBAR)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder type(@NotNull BroadcastType type) {
        Objects.requireNonNull(type, "Broadcast type cannot be null");
        this.type = type;
        return this;
    }

    // ==========================================
    // CHAT type configuration
    // ==========================================

    /**
     * Sets the messages for a CHAT type broadcast.
     * <p>
     * Replaces any previously set messages.
     * </p>
     *
     * @param messages The messages (varargs, supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder messages(@NotNull String... messages) {
        Objects.requireNonNull(messages, "Messages cannot be null");
        this.messages.clear();
        for (String message : messages) {
            if (message != null && !message.isEmpty()) {
                this.messages.add(message);
            }
        }
        return this;
    }

    /**
     * Adds a message to the CHAT type broadcast.
     *
     * @param message The message to add (supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder addMessage(@NotNull String message) {
        Objects.requireNonNull(message, "Message cannot be null");
        if (!message.isEmpty()) {
            this.messages.add(message);
        }
        return this;
    }

    // ==========================================
    // TITLE type configuration
    // ==========================================

    /**
     * Sets the title for a TITLE type broadcast.
     *
     * @param title The title text (supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder title(@NotNull String title) {
        Objects.requireNonNull(title, "Title cannot be null");
        this.title = title;
        return this;
    }

    /**
     * Sets the subtitle for a TITLE type broadcast.
     *
     * @param subtitle The subtitle text (supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder subtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    /**
     * Sets the timing for a TITLE type broadcast.
     *
     * @param fadeIn  Fade in time in ticks
     * @param stay    Stay time in ticks
     * @param fadeOut Fade out time in ticks
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder titleTiming(int fadeIn, int stay, int fadeOut) {
        if (fadeIn < 0 || stay < 0 || fadeOut < 0) {
            throw new IllegalArgumentException("Title timing values cannot be negative");
        }
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }

    // ==========================================
    // ACTIONBAR type configuration
    // ==========================================

    /**
     * Sets the message for an ACTIONBAR type broadcast.
     *
     * @param message The action bar message (supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder actionBar(@NotNull String message) {
        Objects.requireNonNull(message, "Action bar message cannot be null");
        this.actionBar = message;
        return this;
    }

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Sets the addon ID that owns this broadcast.
     *
     * @param addonId The addon identifier
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder addonId(@NotNull String addonId) {
        Objects.requireNonNull(addonId, "Addon ID cannot be null");
        this.addonId = addonId;
        return this;
    }

    /**
     * Adds a custom metadata field to this broadcast.
     *
     * @param key   The field key
     * @param value The field value (should be serializable)
     * @return This builder for chaining
     */
    @NotNull
    public BroadcastBuilder customField(@NotNull String key, @Nullable Object value) {
        Objects.requireNonNull(key, "Custom field key cannot be null");
        if (value == null) {
            this.customFields.remove(key);
        } else {
            this.customFields.put(key, value);
        }
        return this;
    }

    // ==========================================
    // Getters used by the service
    // ==========================================

    @Nullable
    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getInterval() {
        return interval;
    }

    @NotNull
    public Set<GameState> getShowIn() {
        return EnumSet.copyOf(showIn.isEmpty() ? EnumSet.of(GameState.LOBBY) : showIn);
    }

    @NotNull
    public BroadcastType getType() {
        return type;
    }

    @NotNull
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getSubtitle() {
        return subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    @Nullable
    public String getActionBar() {
        return actionBar;
    }

    @Nullable
    public String getAddonId() {
        return addonId;
    }

    @NotNull
    public Map<String, Object> getCustomFields() {
        return new HashMap<>(customFields);
    }

    // ==========================================
    // Validation
    // ==========================================

    /**
     * Validates that this builder has all required fields set correctly.
     *
     * @throws IllegalStateException if the builder is not valid
     */
    public void validate() throws IllegalStateException {
        if (id == null || id.isEmpty()) {
            throw new IllegalStateException("Broadcast ID is required");
        }
        if (interval <= 0) {
            throw new IllegalStateException("Broadcast interval must be positive");
        }
        if (showIn.isEmpty()) {
            throw new IllegalStateException("At least one game state must be specified for showIn");
        }

        switch (type) {
            case CHAT:
                if (messages.isEmpty()) {
                    throw new IllegalStateException("CHAT broadcasts require at least one message");
                }
                break;
            case TITLE:
                if (title == null || title.isEmpty()) {
                    throw new IllegalStateException("TITLE broadcasts require a title");
                }
                break;
            case ACTIONBAR:
                if (actionBar == null || actionBar.isEmpty()) {
                    throw new IllegalStateException("ACTIONBAR broadcasts require a message");
                }
                break;
        }
    }

    /**
     * Checks if this builder is valid without throwing an exception.
     *
     * @return {@code true} if the builder has all required fields
     */
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Copies this builder.
     *
     * @return A new BroadcastBuilder with the same configuration
     */
    @NotNull
    public BroadcastBuilder copy() {
        BroadcastBuilder copy = new BroadcastBuilder();
        copy.id = this.id;
        copy.enabled = this.enabled;
        copy.interval = this.interval;
        copy.showIn.addAll(this.showIn);
        copy.type = this.type;
        copy.messages.addAll(this.messages);
        copy.title = this.title;
        copy.subtitle = this.subtitle;
        copy.fadeIn = this.fadeIn;
        copy.stay = this.stay;
        copy.fadeOut = this.fadeOut;
        copy.actionBar = this.actionBar;
        copy.addonId = this.addonId;
        copy.customFields.putAll(this.customFields);
        return copy;
    }

    @Override
    public String toString() {
        return "BroadcastBuilder{" +
            "id='" + id + '\'' +
            ", enabled=" + enabled +
            ", interval=" + interval +
            ", showIn=" + showIn +
            ", type=" + type +
            ", addonId='" + addonId + '\'' +
            '}';
    }
}
