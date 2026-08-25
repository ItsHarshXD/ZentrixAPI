package dev.itsharshxd.zentrix.api.gui;

import java.util.Set;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Built-in menu discovery, opening, and post-render addon extensions. Registered extensions are
 * reapplied after every render of the matching menu, including pages reached through Zentrix's
 * own navigation controls.
 *
 * @since 1.6.0
 */
public interface BuiltInMenuService {
    @NotNull Set<String> getMenuIds();
    boolean isBuiltIn(@NotNull String menuId);
    @NotNull MenuOpenResult open(@NotNull String menuId, @NotNull Player player);
    @NotNull MenuOpenResult open(
            @NotNull String menuId,
            @NotNull Player player,
            @NotNull BuiltInMenuContext context);
    boolean registerExtension(
            @NotNull String menuId,
            @NotNull String extensionId,
            @NotNull MenuExtension extension);
    boolean unregisterExtension(@NotNull String menuId, @NotNull String extensionId);
    int unregisterExtensions(@NotNull String extensionId);
}
