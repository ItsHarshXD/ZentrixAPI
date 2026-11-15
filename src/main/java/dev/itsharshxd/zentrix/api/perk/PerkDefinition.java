package dev.itsharshxd.zentrix.api.perk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes a perk: its key, the kind of value it holds, its server-wide default and bounds, and
 * how menus show it.
 *
 * <p>Every built-in {@link Perk} has a definition, and addons register their own with
 * {@link PerkService#registerPerk}. Both resolve through the same path: tiers in
 * {@code perks.yml} set a value under the perk's key, boosts add to it, and {@code /perks}, the
 * tier editor and placeholders show it.
 *
 * <pre>{@code
 * PerkDefinition kits = PerkDefinition.number("myaddon.kit-slots")
 *         .defaultValue(1).minimum(0).unlimited(true)
 *         .displayName("&bKit Slots").icon(Material.CHEST)
 *         .description("How many kits a player may save.")
 *         .build();
 * ZentrixAPI.get().getPerkService().registerPerk(plugin, kits);
 * }</pre>
 *
 * <p>Two definitions are equal when their keys are.
 *
 * @since 1.16.0
 */
public final class PerkDefinition {

    /** The value of a {@link Perk.Type#NUMBER} perk that means unlimited, where it is supported. */
    public static final int UNLIMITED = -1;

    /**
     * What an addon perk's key looks like: lowercase words of letters, digits, {@code -} or
     * {@code _}, joined by dots, with at least one dot so it is namespaced by the addon, such as
     * {@code myaddon.kit-slots}.
     */
    private static final Pattern ADDON_KEY = Pattern.compile("[a-z0-9_-]{1,32}(\\.[a-z0-9_-]{1,32}){1,3}");

    private final String key;
    private final Perk.Type type;
    private final int defaultNumber;
    private final boolean defaultAllowed;
    private final int minimum;
    private final int maximum;
    private final boolean unlimited;
    private final String displayName;
    private final Material icon;
    private final List<String> description;
    private final Perk builtIn;

    private PerkDefinition(Builder builder) {
        this.key = builder.key;
        this.type = builder.type;
        this.defaultNumber = builder.defaultNumber;
        this.defaultAllowed = builder.defaultAllowed;
        this.minimum = builder.minimum;
        this.maximum = builder.maximum;
        this.unlimited = builder.unlimited;
        this.displayName = builder.displayName == null ? builder.key : builder.displayName;
        this.icon = builder.icon == null ? Material.PAPER : builder.icon;
        this.description = Collections.unmodifiableList(new ArrayList<>(builder.description));
        this.builtIn = builder.builtIn;
    }

    /** Starts a {@link Perk.Type#NUMBER} perk under an addon key such as {@code myaddon.kit-slots}. */
    @NotNull
    public static Builder number(@NotNull String key) {
        return new Builder(key, Perk.Type.NUMBER);
    }

    /** Starts a {@link Perk.Type#TOGGLE} perk under an addon key such as {@code myaddon.fly}. */
    @NotNull
    public static Builder toggle(@NotNull String key) {
        return new Builder(key, Perk.Type.TOGGLE);
    }

    /**
     * Whether text can be an addon perk's key: lowercase words joined by one to three dots, such
     * as {@code myaddon.kit-slots}. A key also has to be free of built-in perks to be registered.
     */
    public static boolean isValidAddonKey(@Nullable String key) {
        return key != null && ADDON_KEY.matcher(key).matches();
    }

    /** The key a tier sets the perk under in {@code perks.yml}, such as {@code party.max-size}. */
    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public Perk.Type getType() {
        return type;
    }

    /**
     * The server-wide default of a number perk, for players whose tiers leave it out; may be
     * {@link #UNLIMITED}. For a built-in perk it is the default when the definition was taken,
     * since several come from {@code settings.yml}.
     */
    public int getDefaultNumber() {
        return defaultNumber;
    }

    /** The server-wide default of a toggle perk. */
    public boolean isAllowedByDefault() {
        return defaultAllowed;
    }

    /** The lowest a number perk can be, boosts included. */
    public int getMinimum() {
        return minimum;
    }

    /** The highest a number perk can be, boosts included. */
    public int getMaximum() {
        return maximum;
    }

    /** Whether a number perk can be {@link #UNLIMITED}, written {@code -1} in {@code perks.yml}. */
    public boolean supportsUnlimited() {
        return unlimited;
    }

    /** The name players read, with its color codes. */
    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    /** The item the perk wears in menus. */
    @NotNull
    public Material getIcon() {
        return icon;
    }

    /** What the perk does, one line each, as menus show it. */
    @NotNull
    public List<String> getDescription() {
        return description;
    }

    /** The built-in perk this defines, or empty for an addon perk. */
    @NotNull
    public Optional<Perk> getBuiltIn() {
        return Optional.ofNullable(builtIn);
    }

    /** Whether this is an addon perk rather than a built-in one. */
    public boolean isAddon() {
        return builtIn == null;
    }

    /** Whether a value of this number perk means unlimited. */
    public boolean isUnlimited(int value) {
        return unlimited && (value == UNLIMITED || value == Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PerkDefinition definition && definition.key.equals(key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "PerkDefinition[" + key + ", " + type + "]";
    }

    /** Builds a {@link PerkDefinition}. */
    public static final class Builder {

        private final String key;
        private final Perk.Type type;
        private int defaultNumber;
        private boolean defaultAllowed;
        private int minimum;
        private int maximum = Integer.MAX_VALUE;
        private boolean unlimited;
        private String displayName;
        private Material icon;
        private final List<String> description = new ArrayList<>();
        private Perk builtIn;

        private Builder(String key, Perk.Type type) {
            this.key = Objects.requireNonNull(key, "key").trim().toLowerCase(Locale.ROOT);
            this.type = Objects.requireNonNull(type, "type");
        }

        /** The server-wide default of a number perk; {@link #UNLIMITED} where supported. */
        @NotNull
        public Builder defaultValue(int value) {
            this.defaultNumber = value;
            return this;
        }

        /** The server-wide default of a toggle perk. */
        @NotNull
        public Builder defaultValue(boolean allowed) {
            this.defaultAllowed = allowed;
            return this;
        }

        /** The lowest a number perk can be; 0 unless set. */
        @NotNull
        public Builder minimum(int minimum) {
            this.minimum = minimum;
            return this;
        }

        /** The highest a number perk can be; unbounded unless set. */
        @NotNull
        public Builder maximum(int maximum) {
            this.maximum = maximum;
            return this;
        }

        /** Whether a number perk accepts {@link #UNLIMITED}. */
        @NotNull
        public Builder unlimited(boolean supported) {
            this.unlimited = supported;
            return this;
        }

        /** The name players read; color codes are allowed. The key unless set. */
        @NotNull
        public Builder displayName(@NotNull String displayName) {
            this.displayName = Objects.requireNonNull(displayName, "displayName");
            return this;
        }

        /** The item the perk wears in menus; paper unless set. */
        @NotNull
        public Builder icon(@NotNull Material icon) {
            this.icon = Objects.requireNonNull(icon, "icon");
            return this;
        }

        /** What the perk does, one line each. */
        @NotNull
        public Builder description(@NotNull String... lines) {
            for (String line : lines) {
                description.add(Objects.requireNonNull(line, "line"));
            }
            return this;
        }

        /** Marks the definition as a built-in perk's. Used by Zentrix itself. */
        @NotNull
        public Builder builtIn(@NotNull Perk perk) {
            this.builtIn = Objects.requireNonNull(perk, "perk");
            return this;
        }

        /**
         * Builds the definition.
         *
         * @throws IllegalArgumentException when an addon key is not namespaced as
         *                                  {@link #isValidAddonKey} describes, or the bounds
         *                                  or default do not fit together
         */
        @NotNull
        public PerkDefinition build() {
            if (builtIn == null && !isValidAddonKey(key)) {
                throw new IllegalArgumentException("Perk key '" + key + "' must be lowercase words joined by dots, "
                        + "namespaced by the addon, such as myaddon.kit-slots");
            }
            if (type == Perk.Type.NUMBER) {
                if (minimum < 0 || maximum < minimum) {
                    throw new IllegalArgumentException("Perk '" + key + "' needs 0 <= minimum <= maximum");
                }
                boolean unlimitedDefault = unlimited && defaultNumber == UNLIMITED;
                if (!unlimitedDefault && (defaultNumber < minimum || defaultNumber > maximum)) {
                    throw new IllegalArgumentException("Perk '" + key + "' has a default outside its bounds");
                }
            }
            return new PerkDefinition(this);
        }
    }
}
