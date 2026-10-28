package dev.itsharshxd.zentrix.api.cosmetics;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Where one online player stands with one cosmetic, as last read. A snapshot.
 *
 * <p><b>Selected</b> is the player's stored choice; <b>equipped</b> is what is in effect: the
 * choice while the player can still use it, otherwise the type's default. A player who chose
 * nothing has the default equipped without having selected it.
 *
 * @param cosmetic    the cosmetic
 * @param owned       whether the player owns it, permanently or for a limited time
 * @param unlocked    whether the player may use it: owning it, holding its permission, or, for a
 *                    class or an addon's cosmetic, needing neither
 * @param selected    whether it is the player's stored choice
 * @param equipped    whether it is in effect for the player
 * @param expiry      when a cosmetic owned for a limited time expires, or null
 * @param purchasable whether it is for sale and the player cannot use it yet
 * @param affordable  whether it is purchasable and the player's balance, as last read, covers it
 * @since 1.11.0
 */
public record CosmeticState(
        @NotNull Cosmetic cosmetic,
        boolean owned,
        boolean unlocked,
        boolean selected,
        boolean equipped,
        @Nullable Instant expiry,
        boolean purchasable,
        boolean affordable) {

    public CosmeticState {
        Objects.requireNonNull(cosmetic, "cosmetic");
    }

    /** Whether the player may equip it. */
    public boolean canUse() {
        return unlocked;
    }

    /** Whether the player could buy it now, as far as their last-read balance tells. */
    public boolean canPurchase() {
        return purchasable && affordable;
    }

    /** When a cosmetic owned for a limited time expires; empty otherwise. */
    @NotNull
    public Optional<Instant> getExpiry() {
        return Optional.ofNullable(expiry);
    }
}
