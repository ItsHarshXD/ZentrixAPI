package dev.itsharshxd.zentrix.api.dragon;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/** Chunk containing one route endpoint and its current load/ticket status. */
public record DragonEndpointChunk(
        @NotNull UUID worldId,
        @NotNull String worldName,
        int x,
        int z,
        boolean loaded,
        boolean pluginTicketed) {
}
