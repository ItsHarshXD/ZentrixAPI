package dev.itsharshxd.zentrix.api.end;

import java.util.Optional;

/**
 * Immutable request to open or close a game's End access.
 *
 * <p>Access is an entry gate. Closing it stops new arrivals but never ejects the
 * players and spectators already inside the runtime End, who can still leave
 * through its exit portal.
 */
public record EndToggleRequest(
    boolean enabled,
    Optional<Boolean> pvp,
    Optional<EndBorderSettings> border
) {
    public EndToggleRequest {
        pvp = pvp == null ? Optional.empty() : pvp;
        border = border == null ? Optional.empty() : border;
    }

    public EndToggleRequest(boolean enabled) {
        this(enabled, Optional.empty(), Optional.empty());
    }

    public static EndToggleRequest open() { return new EndToggleRequest(true); }
    public static EndToggleRequest close() { return new EndToggleRequest(false); }
}
