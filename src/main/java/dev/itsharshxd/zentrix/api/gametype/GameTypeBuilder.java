package dev.itsharshxd.zentrix.api.gametype;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Fluent builder for creating custom game types.
 * <p>
 * Use it to define team sizes, player limits, and scoreboards for each game
 * state.
 * </p>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * GameTypeBuilder trios = new GameTypeBuilder()
 *     .name("trios")
 *     .teamSize(3)
 *     .minimumPlayers(6)
 *     .maximumPlayers(30)
 *     .startTime(45)
 *     .scoreboard(GameTypeState.WAITING, sb -> sb
 *         .title("&#FFD700&lTRIOS")
 *         .line("&#AAAAAA Map: &#FFFFFF%arena%")
 *         .line("")
 *         .line("&#AAAAAA Players: &#55FF55%players%/%max%"))
 *     .scoreboard(GameTypeState.PLAYING, sb -> sb
 *         .title("&#FFD700&lTRIOS")
 *         .line("&#AAAAAA Phase: &#FFFFFF%phase%")
 *         .line("&#AAAAAA Alive: &#55FF55%alive%")
 *         .line("&#AAAAAA Kills: &#FF5555%kills%"))
 *     .addonId(getAddonId());
 *
 * gameTypeService.registerGameType(trios);
 * }</pre>
 *
 * @author ItsHarshXD
 * @since 1.1.0
 * @see GameTypeService#registerGameType(GameTypeBuilder)
 */
public class GameTypeBuilder {

    private String name;
    private int teamSize = 1;
    private int minimumPlayers = 2;
    private int maximumPlayers = 100;
    private int startTime = 30;
    private final Map<ZentrixGameType.GameTypeState, ScoreboardConfig> scoreboards = new EnumMap<>(ZentrixGameType.GameTypeState.class);
    private String addonId;
    private final Map<String, Object> customFields = new HashMap<>();

    /**
     * Creates a new GameTypeBuilder instance.
     */
    public GameTypeBuilder() {}

    // ==========================================
    // Core properties
    // ==========================================

    /**
     * Sets the name of the game type.
     * <p>
     * This is the unique identifier used for selection and configuration.
     * Must only contain lowercase letters, numbers, and underscores.
     * </p>
     *
     * @param name The game type name (e.g., "trios", "solo", "duos")
     * @return This builder for chaining
     * @throws IllegalArgumentException if the name is null, empty, or contains invalid characters
     */
    @NotNull
    public GameTypeBuilder name(@NotNull String name) {
        Objects.requireNonNull(name, "Game type name cannot be null");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Game type name cannot be empty");
        }
        String lowerName = name.toLowerCase();
        if (!lowerName.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException(
                "Game type name must only contain lowercase letters, numbers, and underscores: " + name
            );
        }
        this.name = lowerName;
        return this;
    }

    /**
     * Sets the team size for this game type.
     *
     * @param size The team size (must be at least 1)
     * @return This builder for chaining
     * @throws IllegalArgumentException if size is less than 1
     */
    @NotNull
    public GameTypeBuilder teamSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Team size must be at least 1: " + size);
        }
        this.teamSize = size;
        return this;
    }

    /**
     * Sets the minimum number of players required to start a game.
     *
     * @param min The minimum player count (must be at least 1)
     * @return This builder for chaining
     * @throws IllegalArgumentException if min is less than 1
     */
    @NotNull
    public GameTypeBuilder minimumPlayers(int min) {
        if (min < 1) {
            throw new IllegalArgumentException("Minimum players must be at least 1: " + min);
        }
        this.minimumPlayers = min;
        return this;
    }

    /**
     * Sets the maximum number of players allowed in a game.
     *
     * @param max The maximum player count (must be at least 1)
     * @return This builder for chaining
     * @throws IllegalArgumentException if max is less than 1
     */
    @NotNull
    public GameTypeBuilder maximumPlayers(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("Maximum players must be at least 1: " + max);
        }
        this.maximumPlayers = max;
        return this;
    }

    /**
     * Sets the start time countdown duration in seconds.
     *
     * @param seconds The start time in seconds (must be positive)
     * @return This builder for chaining
     * @throws IllegalArgumentException if seconds is not positive
     */
    @NotNull
    public GameTypeBuilder startTime(int seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Start time must be positive: " + seconds);
        }
        this.startTime = seconds;
        return this;
    }

    // ==========================================
    // Scoreboard configuration
    // ==========================================

    /**
     * Configures the scoreboard for a specific game state.
     *
     * <pre>{@code
     * builder.scoreboard(GameTypeState.PLAYING, sb -> sb
     *     .title("&#FFD700&lGAME")
     *     .line("&#AAAAAA Alive: &#55FF55%alive%")
     *     .line("&#AAAAAA Kills: &#FF5555%kills%"));
     * }</pre>
     *
     * @param state  The game state for this scoreboard
     * @param config A consumer to configure the scoreboard
     * @return This builder for chaining
     */
    @NotNull
    public GameTypeBuilder scoreboard(@NotNull ZentrixGameType.GameTypeState state, @NotNull Consumer<ScoreboardBuilder> config) {
        Objects.requireNonNull(state, "Game state cannot be null");
        Objects.requireNonNull(config, "Scoreboard config consumer cannot be null");
        ScoreboardBuilder builder = new ScoreboardBuilder();
        config.accept(builder);
        this.scoreboards.put(state, builder.build());
        return this;
    }

    /**
     * Removes the scoreboard for a specific game state.
     *
     * @param state The game state to remove the scoreboard for
     * @return This builder for chaining
     */
    @NotNull
    public GameTypeBuilder noScoreboard(@NotNull ZentrixGameType.GameTypeState state) {
        Objects.requireNonNull(state, "Game state cannot be null");
        this.scoreboards.remove(state);
        return this;
    }

    // ==========================================
    // Metadata
    // ==========================================

    /**
     * Sets the addon ID that owns this game type.
     *
     * @param addonId The addon identifier
     * @return This builder for chaining
     */
    @NotNull
    public GameTypeBuilder addonId(@NotNull String addonId) {
        Objects.requireNonNull(addonId, "Addon ID cannot be null");
        this.addonId = addonId;
        return this;
    }

    /**
     * Adds a custom metadata field to this game type.
     *
     * @param key   The field key
     * @param value The field value (should be serializable)
     * @return This builder for chaining
     */
    @NotNull
    public GameTypeBuilder customField(@NotNull String key, @Nullable Object value) {
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
    public String getName() {
        return name;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public int getStartTime() {
        return startTime;
    }

    @NotNull
    public Map<ZentrixGameType.GameTypeState, ScoreboardConfig> getScoreboards() {
        return new EnumMap<>(scoreboards);
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
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Game type name is required");
        }
        if (teamSize < 1) {
            throw new IllegalStateException("Team size must be at least 1");
        }
        if (minimumPlayers < 1) {
            throw new IllegalStateException("Minimum players must be at least 1");
        }
        if (maximumPlayers < minimumPlayers) {
            throw new IllegalStateException("Maximum players cannot be less than minimum players");
        }
        if (startTime <= 0) {
            throw new IllegalStateException("Start time must be positive");
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
     * @return A new GameTypeBuilder with the same configuration
     */
    @NotNull
    public GameTypeBuilder copy() {
        GameTypeBuilder copy = new GameTypeBuilder();
        copy.name = this.name;
        copy.teamSize = this.teamSize;
        copy.minimumPlayers = this.minimumPlayers;
        copy.maximumPlayers = this.maximumPlayers;
        copy.startTime = this.startTime;
        copy.scoreboards.putAll(this.scoreboards);
        copy.addonId = this.addonId;
        copy.customFields.putAll(this.customFields);
        return copy;
    }

    @Override
    public String toString() {
        return "GameTypeBuilder{" +
            "name='" + name + '\'' +
            ", teamSize=" + teamSize +
            ", players=" + minimumPlayers + "-" + maximumPlayers +
            ", startTime=" + startTime +
            ", addonId='" + addonId + '\'' +
            '}';
    }

    // ==========================================
    // Nested classes
    // ==========================================

    /**
     * Holds the configuration for a single scoreboard.
     */
    public static class ScoreboardConfig {
        private final String title;
        private final List<String> lines;

        public ScoreboardConfig(String title, List<String> lines) {
            this.title = title;
            this.lines = new ArrayList<>(lines);
        }

        public String getTitle() {
            return title;
        }

        public List<String> getLines() {
            return Collections.unmodifiableList(lines);
        }
    }

    /**
     * Builder for configuring scoreboards.
     */
    public static class ScoreboardBuilder {
        private String title = "";
        private final List<String> lines = new ArrayList<>();

        /**
         * Sets the scoreboard title.
         *
         * @param title The title text (supports color codes and placeholders)
         * @return This builder for chaining
         */
        @NotNull
        public ScoreboardBuilder title(@NotNull String title) {
            Objects.requireNonNull(title, "Title cannot be null");
            this.title = title;
            return this;
        }

        /**
         * Adds a line to the scoreboard.
         *
         * @param line The line text (supports color codes and placeholders)
         * @return This builder for chaining
         */
        @NotNull
        public ScoreboardBuilder line(@NotNull String line) {
            Objects.requireNonNull(line, "Line cannot be null");
            this.lines.add(line);
            return this;
        }

        /**
         * Sets all lines at once, replacing any existing lines.
         *
         * @param lines The lines (varargs)
         * @return This builder for chaining
         */
        @NotNull
        public ScoreboardBuilder lines(@NotNull String... lines) {
            Objects.requireNonNull(lines, "Lines cannot be null");
            this.lines.clear();
            for (String line : lines) {
                if (line != null) {
                    this.lines.add(line);
                }
            }
            return this;
        }

        /**
         * Builds the scoreboard configuration.
         *
         * @return The scoreboard config
         */
        @NotNull
        ScoreboardConfig build() {
            return new ScoreboardConfig(title, lines);
        }
    }
}
