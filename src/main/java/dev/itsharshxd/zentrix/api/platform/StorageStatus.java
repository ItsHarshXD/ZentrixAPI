package dev.itsharshxd.zentrix.api.platform;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * The storage Zentrix keeps profiles and balances in, without any connection details.
 *
 * @param backend  the backend type: {@code "LOCAL"}, {@code "MYSQL"} or {@code "MONGODB"}, or
 *                 {@code "NONE"} while none is running
 * @param ready    whether it accepts reads and writes now
 * @param fallback whether this is only a stand-in for a configured database that could not be
 *                 reached; changes made meanwhile are kept for that database where Zentrix can
 * @since 1.8.0
 */
public record StorageStatus(@NotNull String backend, boolean ready, boolean fallback) {

    public StorageStatus {
        Objects.requireNonNull(backend, "backend");
    }
}
