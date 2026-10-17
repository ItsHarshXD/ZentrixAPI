package dev.itsharshxd.zentrix.api.scoreboard;

import dev.itsharshxd.zentrix.api.addon.Registration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Addon contributions to Zentrix's sidebars.
 *
 * <p>Zentrix renders the configured lobby and per-state sidebars as before, then passes each one to
 * the registered {@link SidebarContributor}s, highest priority first and equal priorities in
 * registration order, each seeing the previous one's result. Only what actually changed is sent to
 * the player, so a contributor that returns the same text costs no packets.
 *
 * <p>Without registrations nothing of this runs. Registrations end on their own when the owning
 * plugin disables.
 *
 * @since 1.8.0
 */
public interface ScoreboardService {

    /**
     * Registers a sidebar contributor.
     *
     * @param owner       the plugin the registration belongs to; it must be enabled
     * @param priority    the order among contributors, higher first
     * @param contributor the contributor
     * @return the registration, to unregister early
     * @throws IllegalArgumentException when the owner is not enabled
     */
    @NotNull
    Registration registerContributor(@NotNull Plugin owner, int priority, @NotNull SidebarContributor contributor);
}
