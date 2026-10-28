package dev.itsharshxd.zentrix.api.cosmetics;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A player's equipped cosmetic of an addon's type, triggered at a moment of a match.
 *
 * @param type     the type
 * @param moment   the moment
 * @param player   the player whose cosmetic it is; online
 * @param cosmetic the cosmetic the player has equipped
 * @param game     the match
 * @param other    the other player of a {@link CosmeticMoment#KILL kill} or
 *                 {@link CosmeticMoment#DEATH death}, when online; null otherwise
 * @param location where it happened: where the player died for a death, otherwise where the
 *                 player is
 * @since 1.11.0
 */
public record CosmeticTrigger(
        @NotNull CosmeticTypeKey type,
        @NotNull CosmeticMoment moment,
        @NotNull Player player,
        @NotNull CosmeticDefinition cosmetic,
        @NotNull ZentrixGame game,
        @Nullable Player other,
        @NotNull Location location) {

    public CosmeticTrigger {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(moment, "moment");
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(cosmetic, "cosmetic");
        Objects.requireNonNull(game, "game");
        location = Objects.requireNonNull(location, "location").clone();
    }

    /** The other player of a kill or death, when there is one online. */
    @NotNull
    public Optional<Player> getOther() {
        return Optional.ofNullable(other);
    }

    /** A copy of where it happened. */
    @Override
    @NotNull
    public Location location() {
        return location.clone();
    }
}
