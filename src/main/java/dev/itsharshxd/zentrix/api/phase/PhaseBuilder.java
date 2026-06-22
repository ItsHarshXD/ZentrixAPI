package dev.itsharshxd.zentrix.api.phase;

import dev.itsharshxd.zentrix.api.nether.NetherToggleRequest;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Fluent builder for creating custom game phases dynamically.
 * <p>
 * This builder allows addon developers to create phases that integrate
 * with the Zentrix phase system, including border configuration and
 * on-start actions.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * PhaseBuilder bloodMoon = new PhaseBuilder()
 *     .name("blood_moon")
 *     .displayName("&#990000&l BLOOD MOON")
 *     .duration(90)
 *     .border(border -> border
 *         .shrinkTo(75)
 *         .shrinkDuration(90)
 *         .damagePerBlock(2.0))
 *     .onStart(actions -> actions
 *         .announce("&#990000The Blood Moon rises!")
 *         .title("&#990000&lBLOOD MOON", "&#FF6666Damage doubled!")
 *         .sound(Sound.ENTITY_WITHER_SPAWN, 0.8f, 0.5f)
 *         .giveEffect(PotionEffectType.STRENGTH, 1800, 0))
 *     .addonId(getAddonId());
 *
 * phaseService.registerPhaseAt(bloodMoon, 2);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 * @see PhaseService#registerPhase(PhaseBuilder)
 */
public class PhaseBuilder {

    private String name;
    private String displayName;
    private int duration = 60;
    private BorderConfigBuilder borderConfig;
    private final List<PhaseAction> onStartActions = new ArrayList<>();
    private String addonId;
    private final Map<String, Object> customFields = new HashMap<>();

    /**
     * Creates a new PhaseBuilder instance.
     */
    public PhaseBuilder() {}

    // ==========================================
    // Core Properties
    // ==========================================

    /**
     * Sets the internal name of the phase.
     * <p>
     * This is the unique identifier used in configuration and queries.
     * Must only contain lowercase letters, numbers, and underscores.
     * </p>
     *
     * @param name The phase name (e.g., "blood_moon", "final_shrink")
     * @return This builder for chaining
     * @throws IllegalArgumentException if the name is null, empty, or contains invalid characters
     */
    @NotNull
    public PhaseBuilder name(@NotNull String name) {
        Objects.requireNonNull(name, "Phase name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Phase name cannot be empty");
        }
        String lowerName = name.toLowerCase(Locale.ROOT);
        if (!lowerName.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException(
                "Phase name must only contain lowercase letters, numbers, and underscores: " + name
            );
        }
        this.name = lowerName;
        return this;
    }

    /**
     * Sets the display name of the phase.
     * <p>
     * This is the formatted name shown to players, with color code support
     * (both legacy {@code &c} and hex {@code &#RRGGBB} formats).
     * </p>
     *
     * @param displayName The display name (for example, {@code &#990000&l BLOOD MOON})
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder displayName(@NotNull String displayName) {
        Objects.requireNonNull(displayName, "Display name cannot be null");
        this.displayName = displayName;
        return this;
    }

    /**
     * Sets the duration of the phase in seconds.
     *
     * @param seconds The duration in seconds (must be positive)
     * @return This builder for chaining
     * @throws IllegalArgumentException if duration is not positive
     */
    @NotNull
    public PhaseBuilder duration(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive: " + seconds);
        }
        this.duration = seconds;
        return this;
    }

    // ==========================================
    // Border Configuration
    // ==========================================

    /**
     * Configures the border behavior for this phase using a fluent consumer pattern.
     *
     * <pre>{@code
     * builder.border(border -> border
     *     .shrinkTo(75)
     *     .shrinkDuration(90)
     *     .damagePerBlock(2.0)
     *     .warningTime(10));
     * }</pre>
     *
     * @param config A consumer to configure border settings
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder border(@NotNull Consumer<BorderConfigBuilder> config) {
        Objects.requireNonNull(config, "Border config consumer cannot be null");
        this.borderConfig = new BorderConfigBuilder();
        config.accept(this.borderConfig);
        return this;
    }

    /**
     * Disables border shrinkage for this phase.
     * <p>
     * Creates a border config with {@code doShrinkage = false}.
     * </p>
     *
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder noBorderShrink() {
        this.borderConfig = new BorderConfigBuilder().doShrinkage(false);
        return this;
    }

    // ==========================================
    // On-Start Actions (Consumer Pattern)
    // ==========================================

    /**
     * Configures the on-start actions for this phase using a fluent consumer pattern.
     *
     * <pre>{@code
     * builder.onStart(actions -> actions
     *     .announce("Phase has started!")
     *     .title("Phase!", "Get ready")
     *     .sound(Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f)
     *     .togglePvP(true));
     * }</pre>
     *
     * @param config A consumer to configure on-start actions
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder onStart(@NotNull Consumer<PhaseActionsBuilder> config) {
        Objects.requireNonNull(config, "Actions config consumer cannot be null");
        PhaseActionsBuilder actionsBuilder = new PhaseActionsBuilder();
        config.accept(actionsBuilder);
        this.onStartActions.addAll(actionsBuilder.getActions());
        return this;
    }

    // ==========================================
    // Direct Action Methods
    // ==========================================

    /**
     * Adds an announcement message action to be sent when the phase starts.
     *
     * @param message The message to announce (supports color codes)
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addAnnounce(@NotNull String message) {
        Objects.requireNonNull(message, "Announce message cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("value", message);
        this.onStartActions.add(new PhaseAction(PhaseActionType.ANNOUNCE, params));
        return this;
    }

    /**
     * Adds a title action to be displayed when the phase starts.
     *
     * @param main     The main title text
     * @param subtitle The subtitle text
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addTitle(@NotNull String main, @Nullable String subtitle) {
        Objects.requireNonNull(main, "Title main text cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("main", main);
        if (subtitle != null) {
            params.put("sub", subtitle);
        }
        this.onStartActions.add(new PhaseAction(PhaseActionType.TITLE, params));
        return this;
    }

    /**
     * Adds a sound action to be played when the phase starts.
     *
     * @param sound  The sound to play
     * @param volume The volume (0.0-1.0)
     * @param pitch  The pitch (0.5-2.0)
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addSound(@NotNull Sound sound, float volume, float pitch) {
        Objects.requireNonNull(sound, "Sound cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("name", soundName(sound));
        params.put("volume", volume);
        params.put("pitch", pitch);
        this.onStartActions.add(new PhaseAction(PhaseActionType.SOUND, params));
        return this;
    }

    @NotNull
    public PhaseBuilder addSound(@NotNull Sound sound, float volume, float pitch, int delaySeconds) {
        if (delaySeconds < 0) throw new IllegalArgumentException("Sound delay cannot be negative");
        Objects.requireNonNull(sound, "Sound cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("name", soundName(sound));
        params.put("volume", volume);
        params.put("pitch", pitch);
        params.put("delay", delaySeconds);
        onStartActions.add(new PhaseAction(PhaseActionType.SOUND, params));
        return this;
    }

    /**
     * Adds a PvP toggle action when the phase starts.
     *
     * @param enable {@code true} to enable PvP, {@code false} to disable
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addTogglePvP(boolean enable) {
        Map<String, Object> params = new HashMap<>();
        params.put("enable", enable);
        this.onStartActions.add(new PhaseAction(PhaseActionType.TOGGLE_PVP, params));
        return this;
    }

    /**
     * Adds a potion effect to be given to all players when the phase starts.
     *
     * @param type      The potion effect type
     * @param duration  The duration in ticks (20 ticks = 1 second)
     * @param amplifier The effect amplifier (0 = level 1)
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addGiveEffect(@NotNull PotionEffectType type, int duration, int amplifier) {
        Objects.requireNonNull(type, "Potion effect type cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("effect", type.getName());
        params.put("duration", duration);
        params.put("amplifier", amplifier);
        this.onStartActions.add(new PhaseAction(PhaseActionType.GIVE_EFFECT, params));
        return this;
    }

    /**
     * Adds a command to be executed when the phase starts.
     * <p>
     * Commands are executed as the console. Use placeholders like %player% if supported.
     * </p>
     *
     * @param command The command to execute (without leading slash)
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addCommand(@NotNull String command) {
        Objects.requireNonNull(command, "Command cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("value", command);
        this.onStartActions.add(new PhaseAction(PhaseActionType.COMMAND, params));
        return this;
    }

    /**
     * Adds a deathmatch start action when the phase starts.
     *
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addStartDeathmatch() {
        this.onStartActions.add(new PhaseAction(PhaseActionType.START_DEATHMATCH, new HashMap<>()));
        return this;
    }

    /**
     * Adds an item to be given to all players when the phase starts.
     *
     * @param itemId The item identifier (supports custom:, itemsadder:, nexo:, or vanilla)
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addGiveItem(@NotNull String itemId) {
        Objects.requireNonNull(itemId, "Item ID cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("itemId", itemId);
        this.onStartActions.add(new PhaseAction(PhaseActionType.GIVE_ITEM, params));
        return this;
    }

    @NotNull
    public PhaseBuilder addToggleNether(@NotNull NetherToggleRequest request) {
        this.onStartActions.add(new PhaseAction(PhaseActionType.TOGGLE_NETHER, netherParameters(request)));
        return this;
    }

    private static Map<String, Object> netherParameters(NetherToggleRequest request) {
        Objects.requireNonNull(request, "request");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("enable", request.enabled());
        request.pvp().ifPresent(value -> params.put("pvp", value));
        request.border().ifPresent(border -> {
            Map<String, Object> values = new LinkedHashMap<>();
            border.getShrink().ifPresent(value -> values.put("doShrinkage", value));
            border.getTargetRadius().ifPresent(value -> values.put("shrinkTo", value));
            border.getDurationSeconds().ifPresent(value -> values.put("duration", value));
            border.getDamageAmount().ifPresent(value -> values.put("damagePerBlock", value));
            border.getDamageBuffer().ifPresent(value -> values.put("damageBuffer", value));
            border.getWarningDistance().ifPresent(value -> values.put("warningDistance", value));
            border.getWarningTime().ifPresent(value -> values.put("warningTime", value));
            params.put("border", values);
        });
        return params;
    }

    private static String soundName(Sound sound) {
        for (Field field : Sound.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == Sound.class) {
                try {
                    if (field.get(null) == sound) return field.getName();
                } catch (IllegalAccessException ignored) {
                    // Public Bukkit sound fields are accessible; continue for custom implementations.
                }
            }
        }
        return sound.toString();
    }

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Sets the addon ID that owns this phase.
     * <p>
     * This is used for tracking which addon registered the phase and for
     * querying phases by addon.
     * </p>
     *
     * @param addonId The addon identifier
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder addonId(@NotNull String addonId) {
        Objects.requireNonNull(addonId, "Addon ID cannot be null");
        this.addonId = addonId;
        return this;
    }

    /**
     * Adds a custom metadata field to this phase.
     *
     * @param key   The field key
     * @param value The field value (should be serializable)
     * @return This builder for chaining
     */
    @NotNull
    public PhaseBuilder customField(@NotNull String key, @Nullable Object value) {
        Objects.requireNonNull(key, "Custom field key cannot be null");
        if (value == null) {
            this.customFields.remove(key);
        } else {
            this.customFields.put(key, value);
        }
        return this;
    }

    // ==========================================
    // Getters (for Service implementation)
    // ==========================================

    /**
     * Gets the phase name.
     *
     * @return The phase name, or null if not set
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Gets the display name.
     *
     * @return The display name, or null if not set
     */
    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the duration in seconds.
     *
     * @return The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets the border configuration.
     *
     * @return The border config builder, or null if not set
     */
    @Nullable
    public BorderConfigBuilder getBorderConfig() {
        return borderConfig;
    }

    /**
     * Gets the on-start actions.
     *
     * @return An unmodifiable list of actions
     */
    @NotNull
    public List<PhaseAction> getOnStartActions() {
        return Collections.unmodifiableList(onStartActions);
    }

    /**
     * Gets the addon ID.
     *
     * @return The addon ID, or null if not set
     */
    @Nullable
    public String getAddonId() {
        return addonId;
    }

    /**
     * Gets all custom fields.
     *
     * @return A copy of the custom fields map
     */
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
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Phase name is required");
        }
        if (duration <= 0) {
            throw new IllegalStateException("Phase duration must be positive");
        }
        if (borderConfig != null) {
            borderConfig.validate();
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
     * Creates a copy of this builder.
     *
     * @return A new PhaseBuilder with the same configuration
     */
    @NotNull
    public PhaseBuilder copy() {
        PhaseBuilder copy = new PhaseBuilder();
        copy.name = this.name;
        copy.displayName = this.displayName;
        copy.duration = this.duration;
        if (this.borderConfig != null) {
            copy.borderConfig = this.borderConfig.copy();
        }
        copy.onStartActions.addAll(this.onStartActions);
        copy.addonId = this.addonId;
        copy.customFields.putAll(this.customFields);
        return copy;
    }

    @Override
    public String toString() {
        return "PhaseBuilder{" +
            "name='" + name + '\'' +
            ", displayName='" + displayName + '\'' +
            ", duration=" + duration +
            ", hasBorder=" + (borderConfig != null) +
            ", actionsCount=" + onStartActions.size() +
            ", addonId='" + addonId + '\'' +
            '}';
    }

    // ==========================================
    // Nested Classes
    // ==========================================

    /**
     * Enum representing the types of phase actions.
     */
    public enum PhaseActionType {
        ANNOUNCE,
        TITLE,
        SOUND,
        GIVE_EFFECT,
        GIVE_ITEM,
        COMMAND,
        TOGGLE_PVP,
        TOGGLE_NETHER,
        START_DEATHMATCH
    }

    /**
     * Represents a single phase action with its type and parameters.
     */
    public static class PhaseAction {
        private final PhaseActionType type;
        private final Map<String, Object> parameters;

        public PhaseAction(@NotNull PhaseActionType type, @NotNull Map<String, Object> parameters) {
            this.type = Objects.requireNonNull(type, "type");
            this.parameters = deepMutableMap(Objects.requireNonNull(parameters, "parameters"));
        }

        @NotNull
        public PhaseActionType getType() {
            return type;
        }

        @NotNull
        public Map<String, Object> getParameters() {
            return deepImmutableMap(parameters);
        }

        /** Resolves a canonical parameter while accepting legacy aliases. */
        @NotNull
        public Optional<Object> getParameter(@NotNull String canonical, @NotNull String... aliases) {
            if (parameters.containsKey(canonical)) return Optional.ofNullable(deepImmutableValue(parameters.get(canonical)));
            for (String alias : aliases) {
                if (parameters.containsKey(alias)) return Optional.ofNullable(deepImmutableValue(parameters.get(alias)));
            }
            return Optional.empty();
        }

        private static Map<String, Object> deepMutableMap(Map<?, ?> source) {
            Map<String, Object> copy = new LinkedHashMap<>();
            source.forEach((key, value) -> copy.put(String.valueOf(key), deepMutableValue(value)));
            return copy;
        }

        private static Object deepMutableValue(Object value) {
            if (value instanceof Map<?, ?> map) return deepMutableMap(map);
            if (value instanceof List<?> list) return list.stream().map(PhaseAction::deepMutableValue).toList();
            if (value instanceof Set<?> set) return set.stream().map(PhaseAction::deepMutableValue)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
            return value;
        }

        private static Map<String, Object> deepImmutableMap(Map<?, ?> source) {
            Map<String, Object> copy = new LinkedHashMap<>();
            source.forEach((key, value) -> copy.put(String.valueOf(key), deepImmutableValue(value)));
            return Collections.unmodifiableMap(copy);
        }

        private static Object deepImmutableValue(Object value) {
            if (value instanceof Map<?, ?> map) return deepImmutableMap(map);
            if (value instanceof List<?> list) return list.stream().map(PhaseAction::deepImmutableValue).toList();
            if (value instanceof Set<?> set) {
                LinkedHashSet<Object> copy = set.stream()
                    .map(PhaseAction::deepImmutableValue)
                    .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
                return Collections.unmodifiableSet(copy);
            }
            return value;
        }
    }

    /**
     * Builder for configuring border settings.
     */
    public static class BorderConfigBuilder {
        private boolean doShrinkage = true;
        private double shrinkTo = 50;
        private int shrinkDuration = 60;
        private double damagePerBlock = 1.0;
        private double damageBuffer = 5.0;
        private int warningTime = 0;

        /**
         * Sets whether the border shrinks during this phase.
         *
         * @param doShrinkage {@code true} to enable shrinkage
         * @return This builder for chaining
         */
        @NotNull
        public BorderConfigBuilder doShrinkage(boolean doShrinkage) {
            this.doShrinkage = doShrinkage;
            return this;
        }

        /**
         * Sets the target border size after shrinkage.
         *
         * @param size The target radius in blocks
         * @return This builder for chaining
         */
        @NotNull
        public BorderConfigBuilder shrinkTo(double size) {
            if (size <= 0) {
                throw new IllegalArgumentException("Border size must be positive: " + size);
            }
            this.shrinkTo = size;
            this.doShrinkage = true;
            return this;
        }

        /**
         * Sets the duration of the border shrink animation.
         *
         * @param seconds The shrink duration in seconds
         * @return This builder for chaining
         */
        @NotNull
        public BorderConfigBuilder shrinkDuration(int seconds) {
            if (seconds < 0) {
                throw new IllegalArgumentException("Shrink duration cannot be negative: " + seconds);
            }
            this.shrinkDuration = seconds;
            return this;
        }

        /**
         * Sets the damage dealt per block when outside the border.
         *
         * @param damage Damage per block per second
         * @return This builder for chaining
         */
        @NotNull
        public BorderConfigBuilder damagePerBlock(double damage) {
            if (damage < 0) {
                throw new IllegalArgumentException("Damage cannot be negative: " + damage);
            }
            this.damagePerBlock = damage;
            return this;
        }

        @NotNull
        public BorderConfigBuilder damageBuffer(double blocks) {
            if (blocks < 0) throw new IllegalArgumentException("Damage buffer cannot be negative");
            this.damageBuffer = blocks;
            return this;
        }

        /**
         * Sets the warning time before the phase ends.
         *
         * @param seconds Seconds before phase ends to show warning
         * @return This builder for chaining
         */
        @NotNull
        public BorderConfigBuilder warningTime(int seconds) {
            if (seconds < 0) {
                throw new IllegalArgumentException("Warning time cannot be negative: " + seconds);
            }
            this.warningTime = seconds;
            return this;
        }

        /**
         * Validates the border configuration.
         *
         * @throws IllegalStateException if the configuration is invalid
         */
        public void validate() throws IllegalStateException {
            if (doShrinkage && shrinkTo <= 0) {
                throw new IllegalStateException("Border shrinkTo must be positive when shrinkage is enabled");
            }
        }

        /**
         * Creates a copy of this border config builder.
         *
         * @return A new BorderConfigBuilder with the same settings
         */
        @NotNull
        public BorderConfigBuilder copy() {
            BorderConfigBuilder copy = new BorderConfigBuilder();
            copy.doShrinkage = this.doShrinkage;
            copy.shrinkTo = this.shrinkTo;
            copy.shrinkDuration = this.shrinkDuration;
            copy.damagePerBlock = this.damagePerBlock;
            copy.damageBuffer = this.damageBuffer;
            copy.warningTime = this.warningTime;
            return copy;
        }

        // Getters for implementation
        public boolean isDoShrinkage() { return doShrinkage; }
        public double getShrinkTo() { return shrinkTo; }
        public int getShrinkDuration() { return shrinkDuration; }
        public double getDamagePerBlock() { return damagePerBlock; }
        public double getDamageBuffer() { return damageBuffer; }
        public int getWarningTime() { return warningTime; }
    }

    /**
     * Builder for configuring on-start actions.
     */
    public static class PhaseActionsBuilder {
        private final List<PhaseAction> actions = new ArrayList<>();

        /**
         * Adds an announcement message action.
         *
         * @param message The message to announce
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder announce(@NotNull String message) {
            Objects.requireNonNull(message, "Announce message cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("value", message);
            actions.add(new PhaseAction(PhaseActionType.ANNOUNCE, params));
            return this;
        }

        /**
         * Adds a title action with default timing.
         *
         * @param main     The main title text
         * @param subtitle The subtitle text
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder title(@NotNull String main, @Nullable String subtitle) {
            return title(main, subtitle, 10, 70, 20);
        }

        /**
         * Adds a title action with custom timing.
         *
         * @param main     The main title text
         * @param subtitle The subtitle text
         * @param fadeIn   Fade in time in ticks
         * @param stay     Stay time in ticks
         * @param fadeOut  Fade out time in ticks
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder title(@NotNull String main, @Nullable String subtitle,
                                         int fadeIn, int stay, int fadeOut) {
            Objects.requireNonNull(main, "Title main text cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("main", main);
            if (subtitle != null) {
                params.put("sub", subtitle);
            }
            params.put("fadeIn", fadeIn);
            params.put("stay", stay);
            params.put("fadeOut", fadeOut);
            actions.add(new PhaseAction(PhaseActionType.TITLE, params));
            return this;
        }

        /**
         * Adds a sound action.
         *
         * @param sound  The sound to play
         * @param volume The volume (0.0-1.0)
         * @param pitch  The pitch (0.5-2.0)
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder sound(@NotNull Sound sound, float volume, float pitch) {
            Objects.requireNonNull(sound, "Sound cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("name", soundName(sound));
            params.put("volume", volume);
            params.put("pitch", pitch);
            actions.add(new PhaseAction(PhaseActionType.SOUND, params));
            return this;
        }

        @NotNull
        public PhaseActionsBuilder sound(@NotNull Sound sound, float volume, float pitch, int delaySeconds) {
            if (delaySeconds < 0) throw new IllegalArgumentException("Sound delay cannot be negative");
            Objects.requireNonNull(sound, "Sound cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("name", soundName(sound));
            params.put("volume", volume);
            params.put("pitch", pitch);
            params.put("delay", delaySeconds);
            actions.add(new PhaseAction(PhaseActionType.SOUND, params));
            return this;
        }

        /**
         * Adds a PvP toggle action.
         *
         * @param enable {@code true} to enable PvP
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder togglePvP(boolean enable) {
            Map<String, Object> params = new HashMap<>();
            params.put("enable", enable);
            actions.add(new PhaseAction(PhaseActionType.TOGGLE_PVP, params));
            return this;
        }

        /**
         * Adds a potion effect action.
         *
         * @param type      The potion effect type
         * @param duration  Duration in ticks
         * @param amplifier The amplifier (0 = level 1)
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder giveEffect(@NotNull PotionEffectType type, int duration, int amplifier) {
            Objects.requireNonNull(type, "Potion effect type cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("effect", type.getName());
            params.put("duration", duration);
            params.put("amplifier", amplifier);
            actions.add(new PhaseAction(PhaseActionType.GIVE_EFFECT, params));
            return this;
        }

        /**
         * Adds an item give action.
         *
         * @param itemId The item identifier
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder giveItem(@NotNull String itemId) {
            Objects.requireNonNull(itemId, "Item ID cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("itemId", itemId);
            actions.add(new PhaseAction(PhaseActionType.GIVE_ITEM, params));
            return this;
        }

        /**
         * Adds a command action.
         *
         * @param command The command to execute
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder command(@NotNull String command) {
            Objects.requireNonNull(command, "Command cannot be null");
            Map<String, Object> params = new HashMap<>();
            params.put("value", command);
            actions.add(new PhaseAction(PhaseActionType.COMMAND, params));
            return this;
        }

        /**
         * Adds a deathmatch start action.
         *
         * @return This builder for chaining
         */
        @NotNull
        public PhaseActionsBuilder startDeathmatch() {
            actions.add(new PhaseAction(PhaseActionType.START_DEATHMATCH, new HashMap<>()));
            return this;
        }

        @NotNull
        public PhaseActionsBuilder toggleNether(@NotNull NetherToggleRequest request) {
            actions.add(new PhaseAction(PhaseActionType.TOGGLE_NETHER, netherParameters(request)));
            return this;
        }

        /**
         * Gets all configured actions.
         *
         * @return List of actions
         */
        @NotNull
        List<PhaseAction> getActions() {
            return new ArrayList<>(actions);
        }
    }
}
