package dev.itsharshxd.zentrix.api.perk;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A temporary perk boost given to one player, such as one extra host slot for 24 hours. A
 * snapshot.
 *
 * <p>A {@link Perk.Type#NUMBER} boost adds {@code amount} to the player's value, on top of their
 * tier; several boosts of one perk add up. A {@link Perk.Type#TOGGLE} boost allows the perk while
 * it lasts. A boost ends on its own when it expires, on every server sharing the database.
 *
 * @param id        identifies the boost, for removing it
 * @param perk      the built-in perk it boosts; null for a boost of an addon perk, see
 *                  {@code perkKey}
 * @param amount    added to a number perk; ignored by a toggle
 * @param expiresAt when it ends
 * @param source    who or what gave it, such as an administrator's name or a shop's ID
 * @param perkKey   the key of the perk it boosts, built-in or addon (since 1.16.0)
 * @since 1.9.0
 */
public record PerkBoost(
        @NotNull String id, @Nullable Perk perk, int amount, @NotNull Instant expiresAt,
        @NotNull String source, @NotNull String perkKey) {

    public PerkBoost {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(source, "source");
        if (perkKey == null) {
            perkKey = Objects.requireNonNull(perk, "perk").getKey();
        }
        if (perk == null) {
            perk = builtIn(perkKey);
        }
    }

    /** A boost of a built-in perk. */
    public PerkBoost(@NotNull String id, @NotNull Perk perk, int amount, @NotNull Instant expiresAt,
                     @NotNull String source) {
        this(id, Objects.requireNonNull(perk, "perk"), amount, expiresAt, source, perk.getKey());
    }

    /**
     * A boost of any perk by its key.
     *
     * @since 1.16.0
     */
    public PerkBoost(@NotNull String id, @NotNull String perkKey, int amount, @NotNull Instant expiresAt,
                     @NotNull String source) {
        this(id, null, amount, expiresAt, source, Objects.requireNonNull(perkKey, "perkKey"));
    }

    private static Perk builtIn(String key) {
        for (Perk perk : Perk.values()) {
            if (perk.getKey().equals(key)) {
                return perk;
            }
        }
        return null;
    }

    /**
     * Whether the boost is of an addon perk, whose {@link #perk()} is null.
     *
     * @since 1.16.0
     */
    public boolean isAddonPerk() {
        return perk == null;
    }

    /** Whether the boost still applies. */
    public boolean isActive() {
        return expiresAt.isAfter(Instant.now());
    }

    /** How long the boost lasts from now; zero once it ended. */
    @NotNull
    public Duration getRemaining() {
        Duration remaining = Duration.between(Instant.now(), expiresAt);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }
}
