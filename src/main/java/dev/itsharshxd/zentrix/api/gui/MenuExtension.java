package dev.itsharshxd.zentrix.api.gui;

import org.jetbrains.annotations.NotNull;

/** Addon hook invoked after a built-in menu has rendered its own contents. */
@FunctionalInterface
public interface MenuExtension {
    void extend(@NotNull MenuExtensionContext context);
}
