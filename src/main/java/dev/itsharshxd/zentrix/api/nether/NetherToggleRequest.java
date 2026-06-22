package dev.itsharshxd.zentrix.api.nether;

import java.util.Optional;

/** Immutable request to open or close a game's Nether access. */
public record NetherToggleRequest(
    boolean enabled,
    Optional<Boolean> pvp,
    Optional<NetherBorderSettings> border
) {
    public NetherToggleRequest {
        pvp = pvp == null ? Optional.empty() : pvp;
        border = border == null ? Optional.empty() : border;
    }

    public NetherToggleRequest(boolean enabled) {
        this(enabled, Optional.empty(), Optional.empty());
    }

    public static NetherToggleRequest open() { return new NetherToggleRequest(true); }
    public static NetherToggleRequest close() { return new NetherToggleRequest(false); }
}
