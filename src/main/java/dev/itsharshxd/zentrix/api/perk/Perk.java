package dev.itsharshxd.zentrix.api.perk;

import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A limit of Zentrix that a perk tier in {@code perks.yml} can raise or lower for the players
 * holding its permission.
 *
 * <p>A {@link Type#NUMBER} perk is a count, such as how many games a host may have at once. A
 * {@link Type#TOGGLE} perk is allowed or not, such as using the team changer.
 *
 * @since 1.9.0
 */
public enum Perk {

    /** How many scheduled, waiting or running custom games one host may have at once. */
    CUSTOM_GAMES_MAX_HOSTED("custom-games.max-games", Type.NUMBER),
    /** Whether a host may protect their custom games with a password. */
    CUSTOM_GAMES_PRIVATE("custom-games.private-games", Type.TOGGLE),
    /** Whether a host may change the settings of the scenarios they pick. */
    CUSTOM_GAMES_SCENARIO_SETTINGS("custom-games.scenario-settings", Type.TOGGLE),
    /** Whether a host's publications skip the server-wide publish limit. */
    CUSTOM_GAMES_BYPASS_PUBLISH_LIMIT("custom-games.bypass-publish-limit", Type.TOGGLE),
    /** The largest a party may grow, decided by its current leader. */
    PARTY_MAX_SIZE("party.max-size", Type.NUMBER),
    /** Whether a player may use the team changer in a waiting lobby. */
    TEAM_CHANGER("team-changer", Type.TOGGLE);

    /** What kind of value a perk holds. */
    public enum Type {
        /** A whole number. Boosts add to it. */
        NUMBER,
        /** Allowed or not. Any active boost allows it. */
        TOGGLE
    }

    private final String key;
    private final Type type;

    Perk(String key, Type type) {
        this.key = key;
        this.type = type;
    }

    /** The key this perk is written under in {@code perks.yml}, such as {@code party.max-size}. */
    @NotNull
    public String getKey() {
        return key;
    }

    /** What kind of value the perk holds. */
    @NotNull
    public Type getType() {
        return type;
    }

    /**
     * The perk a key names: its {@code perks.yml} key, or its enum name, ignoring case. Dashes,
     * dots and underscores are interchangeable, so {@code party_max_size} names
     * {@link #PARTY_MAX_SIZE} as well.
     */
    @NotNull
    public static Optional<Perk> fromKey(@Nullable String key) {
        if (key == null) {
            return Optional.empty();
        }
        String normalized = normalize(key);
        for (Perk perk : values()) {
            if (normalize(perk.key).equals(normalized) || normalize(perk.name()).equals(normalized)) {
                return Optional.of(perk);
            }
        }
        return Optional.empty();
    }

    private static String normalize(String key) {
        return key.trim().toLowerCase(Locale.ROOT).replace('-', '_').replace('.', '_');
    }
}
