package dev.itsharshxd.zentrix.api.scoreboard;

import org.jetbrains.annotations.NotNull;

/**
 * Adjusts sidebars before Zentrix shows them: adds, removes or rewrites lines, or replaces the
 * title.
 *
 * <p>Called on the main thread on every sidebar update of every player - the lobby sidebar and each
 * state of a match - so it must be quick. A contributor that throws has its changes discarded for
 * that update and the next contributor sees the sidebar as it was.
 *
 * @since 1.8.0
 */
@FunctionalInterface
public interface SidebarContributor {

    /** Changes one sidebar about to be shown. */
    void contribute(@NotNull SidebarRender render);
}
