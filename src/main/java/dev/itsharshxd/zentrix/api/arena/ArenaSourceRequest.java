package dev.itsharshxd.zentrix.api.arena;

import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What Zentrix wants a source arena for.
 *
 * <p>A provider may use the game type to pick a suitable source, but is never required to: Zentrix
 * applies the requested game type to whatever source it is handed.
 *
 * @param purpose      why the source is needed
 * @param gameTypeName the {@code game-types.yml} name the source will be played under
 * @param requesterId  the player the request is made for, or null for a server-side request
 * @param requesterName the display name of that player, empty when there is none
 */
public record ArenaSourceRequest(
        @NotNull ArenaSourcePurpose purpose,
        @NotNull String gameTypeName,
        @Nullable UUID requesterId,
        @NotNull String requesterName) {

    public ArenaSourceRequest {
        if (purpose == null) throw new IllegalArgumentException("purpose cannot be null");
        gameTypeName = gameTypeName == null ? "" : gameTypeName.trim();
        requesterName = requesterName == null ? "" : requesterName;
    }

    public @NotNull Optional<UUID> getRequesterId() {
        return Optional.ofNullable(requesterId);
    }
}
