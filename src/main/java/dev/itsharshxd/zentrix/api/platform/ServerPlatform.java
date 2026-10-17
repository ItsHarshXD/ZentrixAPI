package dev.itsharshxd.zentrix.api.platform;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * The server Zentrix runs on.
 *
 * @param minecraftVersion the Minecraft version, such as {@code "1.21.10"}
 * @param serverName       the server software, such as {@code "Paper"}
 * @param serverVersion    the server software's full version string
 * @param javaVersion      the feature version of the running Java, such as {@code 21}
 * @param supported        whether Zentrix supports this Minecraft version and software
 * @param beta             whether support for this Minecraft version is still in beta
 * @since 1.8.0
 */
public record ServerPlatform(
        @NotNull String minecraftVersion,
        @NotNull String serverName,
        @NotNull String serverVersion,
        int javaVersion,
        boolean supported,
        boolean beta) {

    public ServerPlatform {
        Objects.requireNonNull(minecraftVersion, "minecraftVersion");
        Objects.requireNonNull(serverName, "serverName");
        Objects.requireNonNull(serverVersion, "serverVersion");
    }
}
