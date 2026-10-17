package dev.itsharshxd.zentrix.api.platform;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One capability as it stands on this server.
 *
 * @param id       the capability id, such as {@link ZentrixCapabilities#GLIDER_DEPLOYMENT}
 * @param state    whether it can be used, and if not, why
 * @param reason   a human-readable explanation when it is not available; {@code null} otherwise
 * @param provider who reports it: {@code "Zentrix"} for built-in capabilities, otherwise the name
 *                 of the plugin that registered it
 * @since 1.8.0
 */
public record CapabilityStatus(
        @NotNull String id,
        @NotNull CapabilityState state,
        @Nullable String reason,
        @NotNull String provider) {

    /** The provider name of every capability Zentrix reports itself. */
    public static final String ZENTRIX = "Zentrix";

    public CapabilityStatus {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(provider, "provider");
    }

    /** A usable capability. */
    @NotNull
    public static CapabilityStatus available(@NotNull String id, @NotNull String provider) {
        return new CapabilityStatus(id, CapabilityState.AVAILABLE, null, provider);
    }

    /** A capability that cannot be used, and why. */
    @NotNull
    public static CapabilityStatus unavailable(
            @NotNull String id, @NotNull CapabilityState state, @Nullable String reason,
            @NotNull String provider) {
        return new CapabilityStatus(id, state, reason, provider);
    }

    /** An id this server does not know. */
    @NotNull
    public static CapabilityStatus unknown(@NotNull String id) {
        return new CapabilityStatus(id, CapabilityState.UNKNOWN, "Not provided by this server", ZENTRIX);
    }

    /** Whether the capability can be used now. */
    public boolean isAvailable() {
        return state.isAvailable();
    }
}
