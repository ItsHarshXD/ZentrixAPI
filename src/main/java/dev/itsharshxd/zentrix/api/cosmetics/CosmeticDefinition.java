package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One cosmetic of an addon's type, as its {@link CosmeticHandler#getCosmetics() handler} lists it.
 * Immutable; build one with {@link #builder(String)}.
 *
 * <p>Who may use it:
 * <ul>
 *   <li>a player who owns it, granted or bought;</li>
 *   <li>a player who holds its {@linkplain Builder#permission permission}, when it has one;</li>
 *   <li>everyone, when it is {@linkplain Builder#unlockedByDefault unlocked by default}.</li>
 * </ul>
 * One with a {@linkplain Builder#price price} can be bought with Zentrix's currency by players who
 * cannot use it yet.
 *
 * <pre>{@code
 * CosmeticDefinition crown = CosmeticDefinition.builder("crown")
 *     .name("&6Golden Crown")
 *     .icon(new ItemStack(Material.GOLDEN_HELMET))
 *     .lore(List.of("&7Fit for a champion."))
 *     .rarity("&6Legendary")
 *     .price(2500)
 *     .build();
 * }</pre>
 *
 * @since 1.11.0
 */
public final class CosmeticDefinition {

    private static final Pattern ID = Pattern.compile("[a-z0-9_-]+");

    private final String id;
    private final String name;
    private final ItemStack icon;
    private final List<String> lore;
    private final String rarity;
    private final String permission;
    private final Double price;
    private final boolean unlockedByDefault;

    private CosmeticDefinition(Builder builder) {
        this.id = builder.id;
        this.name = builder.name == null ? builder.id : builder.name;
        this.icon = builder.icon.clone();
        this.lore = List.copyOf(builder.lore);
        this.rarity = builder.rarity;
        this.permission = builder.permission;
        this.price = builder.price;
        this.unlockedByDefault = builder.unlockedByDefault;
    }

    /**
     * Starts a cosmetic.
     *
     * @param id the stable ID ownership is stored under: {@code a-z}, {@code 0-9}, {@code _} and
     *           {@code -}
     */
    @NotNull
    public static Builder builder(@NotNull String id) {
        return new Builder(id);
    }

    /** The stable ID ownership is stored under. */
    @NotNull
    public String getId() {
        return id;
    }

    /** The display name, in Zentrix's colour format. */
    @NotNull
    public String getName() {
        return name;
    }

    /** A copy of the icon menus show it with. */
    @NotNull
    public ItemStack getIcon() {
        return icon.clone();
    }

    /** The description lines, in Zentrix's colour format. */
    @NotNull
    public List<String> getLore() {
        return lore;
    }

    /** The rarity label, in Zentrix's colour format, such as {@code &6Legendary}. */
    @NotNull
    public Optional<String> getRarity() {
        return Optional.ofNullable(rarity);
    }

    /** The permission that unlocks it without owning it. */
    @NotNull
    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    /** What it costs in Zentrix's currency; empty when it is not for sale. */
    @NotNull
    public OptionalDouble getPrice() {
        return price == null ? OptionalDouble.empty() : OptionalDouble.of(price);
    }

    /** Whether every player may use it without owning it. */
    public boolean isUnlockedByDefault() {
        return unlockedByDefault;
    }

    /** Builds a {@link CosmeticDefinition}. */
    public static final class Builder {

        private final String id;
        private String name;
        private ItemStack icon = new ItemStack(Material.PAPER);
        private List<String> lore = new ArrayList<>();
        private String rarity;
        private String permission;
        private Double price;
        private boolean unlockedByDefault;

        private Builder(String id) {
            Objects.requireNonNull(id, "id");
            if (!ID.matcher(id).matches()) {
                throw new IllegalArgumentException("Cosmetic IDs use a-z, 0-9, _ and -: " + id);
            }
            this.id = id;
        }

        /** The display name, in Zentrix's colour format; the ID when not set. */
        @NotNull
        public Builder name(@NotNull String name) {
            this.name = Objects.requireNonNull(name, "name");
            return this;
        }

        /** The icon menus show; copied. Paper when not set. */
        @NotNull
        public Builder icon(@NotNull ItemStack icon) {
            Objects.requireNonNull(icon, "icon");
            if (icon.getType().isAir()) {
                throw new IllegalArgumentException("A cosmetic's icon cannot be air");
            }
            this.icon = icon.clone();
            return this;
        }

        /** The description lines, in Zentrix's colour format. */
        @NotNull
        public Builder lore(@NotNull List<String> lore) {
            this.lore = new ArrayList<>(Objects.requireNonNull(lore, "lore"));
            return this;
        }

        /** The rarity label, in Zentrix's colour format, or null for none. */
        @NotNull
        public Builder rarity(@Nullable String rarity) {
            this.rarity = rarity == null || rarity.isBlank() ? null : rarity;
            return this;
        }

        /** The permission that unlocks it without owning it, or null for none. */
        @NotNull
        public Builder permission(@Nullable String permission) {
            this.permission = permission == null || permission.isBlank() ? null : permission;
            return this;
        }

        /**
         * What it costs in Zentrix's currency.
         *
         * @param price zero or more; zero makes it free to claim
         * @throws IllegalArgumentException when the price is negative or not a finite number
         */
        @NotNull
        public Builder price(double price) {
            if (!Double.isFinite(price) || price < 0) {
                throw new IllegalArgumentException("A cosmetic's price must be zero or more: " + price);
            }
            this.price = price;
            return this;
        }

        /** Takes it off sale; the default. */
        @NotNull
        public Builder notForSale() {
            this.price = null;
            return this;
        }

        /** Whether every player may use it without owning it; false when not set. */
        @NotNull
        public Builder unlockedByDefault(boolean unlockedByDefault) {
            this.unlockedByDefault = unlockedByDefault;
            return this;
        }

        @NotNull
        public CosmeticDefinition build() {
            return new CosmeticDefinition(this);
        }
    }
}
