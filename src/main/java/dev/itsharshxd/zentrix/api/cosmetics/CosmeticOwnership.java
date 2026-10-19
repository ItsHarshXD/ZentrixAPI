package dev.itsharshxd.zentrix.api.cosmetics;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What one player owns and chose in one cosmetic category. A snapshot.
 *
 * <p>Owning a cosmetic an administrator later deleted is kept, and counts again once a cosmetic
 * with that ID exists.
 *
 * <p>A cosmetic granted for a limited time has an entry in {@code expiries}; one without an entry
 * is owned permanently. Expired cosmetics are never listed: Zentrix removes them as they expire,
 * also for players who were offline at the time.
 *
 * @param category the category
 * @param owned    the owned cosmetic IDs, in the order they were granted
 * @param selected the chosen cosmetic's ID, or null for the server's default
 * @param expiries when each timed cosmetic expires, by ID; empty when every one is permanent
 * @since 1.8.0
 */
public record CosmeticOwnership(
        @NotNull CosmeticCategory category, @NotNull Set<String> owned, @Nullable String selected,
        @NotNull Map<String, Instant> expiries) {

    public CosmeticOwnership {
        Objects.requireNonNull(category, "category");
        Set<String> ids = Collections.unmodifiableSet(new LinkedHashSet<>(owned));
        Map<String, Instant> timed = new LinkedHashMap<>();
        if (expiries != null) {
            expiries.forEach((id, expiry) -> {
                if (id != null && expiry != null && ids.contains(id)) {
                    timed.put(id, expiry);
                }
            });
        }
        owned = ids;
        expiries = Collections.unmodifiableMap(timed);
    }

    /** Ownership without timed cosmetics, as API 1.8.0 described it. */
    public CosmeticOwnership(
            @NotNull CosmeticCategory category, @NotNull Set<String> owned, @Nullable String selected) {
        this(category, owned, selected, Map.of());
    }

    /** Whether the player owns a cosmetic. */
    public boolean owns(@NotNull String id) {
        return owned.contains(id);
    }

    /** The chosen cosmetic while the player still owns it; empty means the server's default. */
    @NotNull
    public Optional<String> getEffectiveSelection() {
        return selected != null && owned.contains(selected) ? Optional.of(selected) : Optional.empty();
    }

    /**
     * When a timed cosmetic expires.
     *
     * @return empty when the cosmetic is owned permanently or not owned at all
     * @since 1.9.0
     */
    @NotNull
    public Optional<Instant> getExpiry(@NotNull String id) {
        return Optional.ofNullable(expiries.get(id));
    }

    /**
     * Whether a cosmetic is owned for a limited time.
     *
     * @since 1.9.0
     */
    public boolean isTimed(@NotNull String id) {
        return expiries.containsKey(id);
    }

    /**
     * How long the player keeps a timed cosmetic from now.
     *
     * @return empty when the cosmetic is owned permanently or not owned at all; zero once it is due
     * @since 1.9.0
     */
    @NotNull
    public Optional<Duration> getRemaining(@NotNull String id) {
        Instant expiry = expiries.get(id);
        if (expiry == null) {
            return Optional.empty();
        }
        Duration remaining = Duration.between(Instant.now(), expiry);
        return Optional.of(remaining.isNegative() ? Duration.ZERO : remaining);
    }
}
