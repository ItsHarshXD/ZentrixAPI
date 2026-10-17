package dev.itsharshxd.zentrix.api.platform;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * One optional plugin Zentrix integrates with, as Zentrix found it.
 *
 * <p>Reading this never loads the integrated plugin's classes, so an addon can decide on its own
 * prerequisites without linking against them.
 *
 * @param name           the plugin's name, such as {@code "PlaceholderAPI"}
 * @param installed      whether the plugin is present on the server at all
 * @param version        its installed version, or {@code null} when it is not installed
 * @param minimumVersion the oldest version Zentrix hooks into, or {@code null} when any will do
 * @param state          {@link CapabilityState#AVAILABLE} when Zentrix's hook is active
 * @param reason         why the hook is not active; {@code null} when it is
 * @since 1.8.0
 */
public record IntegrationStatus(
        @NotNull String name,
        boolean installed,
        @Nullable String version,
        @Nullable String minimumVersion,
        @NotNull CapabilityState state,
        @Nullable String reason) {

    public IntegrationStatus {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(state, "state");
    }

    /** Whether Zentrix's hook into this plugin is active. */
    public boolean isHooked() {
        return state.isAvailable();
    }
}
