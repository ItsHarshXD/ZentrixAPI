package dev.itsharshxd.zentrix.api.block;

import org.jetbrains.annotations.NotNull;

/** Current protection and decay classification of one block. */
public record TrackedBlockState(
        @NotNull BlockMechanicsScope scope,
        boolean protectionEnabled,
        boolean originalProtected,
        boolean playerPlaced,
        boolean fluidTracked,
        boolean decayScheduled) {
}
