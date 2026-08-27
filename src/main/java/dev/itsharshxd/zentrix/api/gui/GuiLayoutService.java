package dev.itsharshxd.zentrix.api.gui;

import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * GUI ID registration plus configured size, slot, filler, and pagination resolution. Runtime
 * registration is reserved for addon IDs and never replaces a shipped built-in definition.
 *
 * @since 1.6.0
 */
public interface GuiLayoutService {
    @NotNull Set<String> getRegisteredIds();
    boolean isRegistered(@NotNull String guiId);
    boolean isBuiltIn(@NotNull String guiId);
    boolean register(@NotNull GuiLayoutDefinition definition);
    boolean unregister(@NotNull String guiId);
    @NotNull GuiLayout resolve(@NotNull String guiId);
    @NotNull GuiLayout resolve(@NotNull String guiId, int automaticSize);
    void reload();
}
