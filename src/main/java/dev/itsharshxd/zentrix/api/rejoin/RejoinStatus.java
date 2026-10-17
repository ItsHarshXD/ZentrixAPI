package dev.itsharshxd.zentrix.api.rejoin;

import dev.itsharshxd.zentrix.api.corpse.ZentrixCorpse;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Whether a player can return to the match they left, and through which corpse.
 *
 * @param playerId    the player
 * @param eligibility whether they can rejoin now, and if not, why
 * @param game        the match they would return to, when they still hold a return
 * @param corpse      the corpse that holds their return, when there is one
 * @since 1.8.0
 */
public record RejoinStatus(
        @NotNull UUID playerId,
        @NotNull RejoinEligibility eligibility,
        @Nullable ZentrixGame game,
        @Nullable ZentrixCorpse corpse) {

    public RejoinStatus {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(eligibility, "eligibility");
    }

    /** Whether the player can rejoin now. */
    public boolean isEligible() {
        return eligibility.isEligible();
    }

    /** The match they would return to. */
    @NotNull
    public Optional<ZentrixGame> gameIfAny() {
        return Optional.ofNullable(game);
    }

    /** The corpse that holds their return. */
    @NotNull
    public Optional<ZentrixCorpse> corpseIfAny() {
        return Optional.ofNullable(corpse);
    }
}
