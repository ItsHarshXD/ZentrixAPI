package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A player's cosmetics of one category before and after a change, as stored.
 *
 * <p>A request that changed nothing, such as granting a cosmetic the player already owns or
 * choosing one they do not own, has equal {@code before} and {@code after}.
 *
 * @param before the cosmetics before the change
 * @param after  the cosmetics after it
 * @since 1.8.0
 */
public record CosmeticChange(@NotNull CosmeticOwnership before, @NotNull CosmeticOwnership after) {

    public CosmeticChange {
        Objects.requireNonNull(before, "before");
        Objects.requireNonNull(after, "after");
    }

    /** Whether anything changed. */
    public boolean changed() {
        return !before.equals(after);
    }
}
