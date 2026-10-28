package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * How an addon's cosmetic type shows in {@code /cosmetics} and when it triggers. Immutable; build
 * one with {@link #builder(String)} and register it with {@link CosmeticsService#registerType}.
 *
 * <pre>{@code
 * CosmeticTypeDefinition hats = CosmeticTypeDefinition.builder("hats")
 *     .name("&eHats")
 *     .description(List.of("&fChoose the hat you wear", "&fin every match."))
 *     .icon(new ItemStack(Material.LEATHER_HELMET))
 *     .triggers(CosmeticMoment.GAME_START, CosmeticMoment.GAME_LEAVE)
 *     .build();
 * }</pre>
 *
 * @since 1.11.0
 */
public final class CosmeticTypeDefinition {

    private static final Pattern KEY = Pattern.compile("[a-z0-9_-]+");

    private final String key;
    private final String name;
    private final List<String> description;
    private final ItemStack icon;
    private final String defaultId;
    private final Set<CosmeticMoment> triggers;
    private final boolean showLocked;
    private final boolean previews;

    private CosmeticTypeDefinition(Builder builder) {
        this.key = builder.key;
        this.name = builder.name == null ? builder.key : builder.name;
        this.description = List.copyOf(builder.description);
        this.icon = builder.icon.clone();
        this.defaultId = builder.defaultId;
        this.triggers = Set.copyOf(builder.triggers);
        this.showLocked = builder.showLocked;
        this.previews = builder.previews;
    }

    /**
     * Starts a type.
     *
     * @param key the type within the registering plugin's namespace, such as {@code hats}: {@code
     *            a-z}, {@code 0-9}, {@code _} and {@code -}. Ownership is stored under it, so keep
     *            it stable.
     */
    @NotNull
    public static Builder builder(@NotNull String key) {
        return new Builder(key);
    }

    /** The type within the registering plugin's namespace. */
    @NotNull
    public String getKey() {
        return key;
    }

    /** The display name, in Zentrix's colour format. */
    @NotNull
    public String getName() {
        return name;
    }

    /** The description lines of its tile, in Zentrix's colour format. */
    @NotNull
    public List<String> getDescription() {
        return description;
    }

    /** A copy of its tile's icon. */
    @NotNull
    public ItemStack getIcon() {
        return icon.clone();
    }

    /** The cosmetic used by players who chose none; empty when they have none. */
    @NotNull
    public Optional<String> getDefaultId() {
        return Optional.ofNullable(defaultId);
    }

    /** The moments the type triggers at. */
    @NotNull
    public Set<CosmeticMoment> getTriggers() {
        return triggers;
    }

    /** Whether {@code /cosmetics} also lists the cosmetics a player can neither use nor buy. */
    public boolean isShowLocked() {
        return showLocked;
    }

    /** Whether its handler shows previews, so {@code /cosmetics} offers right-click to preview. */
    public boolean hasPreviews() {
        return previews;
    }

    /** Builds a {@link CosmeticTypeDefinition}. */
    public static final class Builder {

        private final String key;
        private String name;
        private List<String> description = new ArrayList<>();
        private ItemStack icon = new ItemStack(Material.CHEST);
        private String defaultId;
        private final Set<CosmeticMoment> triggers = EnumSet.noneOf(CosmeticMoment.class);
        private boolean showLocked;
        private boolean previews;

        private Builder(String key) {
            Objects.requireNonNull(key, "key");
            if (!KEY.matcher(key).matches()) {
                throw new IllegalArgumentException("Cosmetic type keys use a-z, 0-9, _ and -: " + key);
            }
            this.key = key;
        }

        /** The display name, in Zentrix's colour format; the key when not set. */
        @NotNull
        public Builder name(@NotNull String name) {
            this.name = Objects.requireNonNull(name, "name");
            return this;
        }

        /** The description lines of its tile, in Zentrix's colour format. */
        @NotNull
        public Builder description(@NotNull List<String> description) {
            this.description = new ArrayList<>(Objects.requireNonNull(description, "description"));
            return this;
        }

        /** Its tile's icon; copied. A chest when not set. */
        @NotNull
        public Builder icon(@NotNull ItemStack icon) {
            Objects.requireNonNull(icon, "icon");
            if (icon.getType().isAir()) {
                throw new IllegalArgumentException("A cosmetic type's icon cannot be air");
            }
            this.icon = icon.clone();
            return this;
        }

        /**
         * The cosmetic used by players who chose none, which every player may use; null, the
         * default, for none, so players without a choice have nothing equipped.
         */
        @NotNull
        public Builder defaultCosmetic(@Nullable String id) {
            this.defaultId = id == null || id.isBlank() ? null : id;
            return this;
        }

        /** The moments the type triggers at; none when not set. */
        @NotNull
        public Builder triggers(@NotNull CosmeticMoment... moments) {
            return triggers(List.of(moments));
        }

        /** The moments the type triggers at; none when not set. */
        @NotNull
        public Builder triggers(@NotNull Collection<CosmeticMoment> moments) {
            triggers.clear();
            triggers.addAll(moments);
            return this;
        }

        /**
         * Whether {@code /cosmetics} also lists, marked locked, the cosmetics a player can neither
         * use nor buy. False when not set: those are hidden, as Zentrix hides gliders a player
         * does not own.
         */
        @NotNull
        public Builder showLocked(boolean showLocked) {
            this.showLocked = showLocked;
            return this;
        }

        /**
         * Whether the handler implements {@link CosmeticHandler#startPreview}, so
         * {@code /cosmetics} offers right-click to preview. False when not set.
         * {@link CosmeticsService#startPreview} asks the handler either way.
         */
        @NotNull
        public Builder previews(boolean previews) {
            this.previews = previews;
            return this;
        }

        @NotNull
        public CosmeticTypeDefinition build() {
            return new CosmeticTypeDefinition(this);
        }
    }
}
