package dev.itsharshxd.zentrix.api.identity;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * One applied mask, and the way to take it off again.
 *
 * <p>A handle is what {@link IdentityService#mask} hands back. Releasing it restores the player's
 * real profile immediately; Zentrix releases it on its own when the match ends, the player leaves,
 * the providing plugin is disabled or the server shuts down, so a caller that forgets can still not
 * strand a player behind somebody else's face.
 *
 * <p>Releasing twice is harmless.
 *
 * @since 1.6.0
 */
public interface IdentityHandle {

    /** The masked player. */
    @NotNull
    UUID playerId();

    /** The face this handle applied. */
    @NotNull
    MaskedIdentity identity();

    /** Whether the mask is still on. */
    boolean isActive();

    /**
     * Takes the mask off and puts the real profile back.
     *
     * @return true when this call was the one that removed it
     */
    boolean release();
}
