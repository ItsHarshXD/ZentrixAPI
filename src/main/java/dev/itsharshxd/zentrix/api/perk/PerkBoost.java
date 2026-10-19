package dev.itsharshxd.zentrix.api.perk;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A temporary perk boost given to one player, such as one extra host slot for 24 hours. A
 * snapshot.
 *
 * <p>A {@link Perk.Type#NUMBER} boost adds {@code amount} to the player's value, on top of their
 * tier; several boosts of one perk add up. A {@link Perk.Type#TOGGLE} boost allows the perk while
 * it lasts. A boost ends on its own when it expires, on every server sharing the database.
 *
 * @param id        identifies the boost, for removing it
 * @param perk      the perk it boosts
 * @param amount    added to a number perk; ignored by a toggle
 * @param expiresAt when it ends
 * @param source    who or what gave it, such as an administrator's name or a shop's ID
 * @since 1.9.0
 */
public record PerkBoost(
        @NotNull String id, @NotNull Perk perk, int amount, @NotNull Instant expiresAt,
        @NotNull String source) {

    public PerkBoost {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(perk, "perk");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(source, "source");
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
