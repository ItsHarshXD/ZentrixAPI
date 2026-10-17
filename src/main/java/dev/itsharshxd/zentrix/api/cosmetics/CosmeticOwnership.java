package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.Collections;
import java.util.LinkedHashSet;
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
 * @param category the category
 * @param owned    the owned cosmetic IDs, in the order they were granted
 * @param selected the chosen cosmetic's ID, or null for the server's default
 * @since 1.8.0
 */
public record CosmeticOwnership(
        @NotNull CosmeticCategory category, @NotNull Set<String> owned, @Nullable String selected) {

    public CosmeticOwnership {
        Objects.requireNonNull(category, "category");
        owned = Collections.unmodifiableSet(new LinkedHashSet<>(owned));
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
}
