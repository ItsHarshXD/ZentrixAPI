package dev.itsharshxd.zentrix.api.cornucopia;

import org.jetbrains.annotations.NotNull;

/** Result of validating both the configured schematic and its podium marker. */
public record CornucopiaSchematicStatus(
        @NotNull String schematic,
        @NotNull String podiumBlock,
        boolean readable,
        boolean usable) {
}
