package dev.itsharshxd.zentrix.api.perk;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * A timed boost that applies to every player, such as one extra host slot for an event weekend.
 * A snapshot.
 *
 * <p>Server-wide boosts stack with personal ones: a number boost adds {@code amount}, and a
 * toggle boost allows the perk. They are stored in the shared database, so they last across
 * restarts and apply on every server using it.
 *
 * @param id        identifies the boost, for ending it early
 * @param perkKey   the key of the perk it boosts, built-in or addon
 * @param amount    added to a number perk; ignored by a toggle
 * @param startedAt when it started
 * @param expiresAt when it ends
 * @param source    who or what started it, such as an administrator's name
 * @since 1.16.0
 */
public record ServerBoost(
        @NotNull String id, @NotNull String perkKey, int amount, @NotNull Instant startedAt,
        @NotNull Instant expiresAt, @NotNull String source) {

    public ServerBoost {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(perkKey, "perkKey");
        Objects.requireNonNull(startedAt, "startedAt");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(source, "source");
    }

    /** The built-in perk it boosts, or empty for an addon perk. */
    @NotNull
    public Optional<Perk> getPerk() {
        for (Perk perk : Perk.values()) {
            if (perk.getKey().equals(perkKey)) {
                return Optional.of(perk);
            }
        }
        return Optional.empty();
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
