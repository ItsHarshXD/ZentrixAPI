package dev.itsharshxd.zentrix.api.settings;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The outcome of {@link SettingsService#set}.
 *
 * @param status what happened
 * @param value  the value now stored, as Zentrix normalised it; null unless it was saved
 * @param reason why the value was refused, for {@link Status#INVALID}; empty otherwise
 * @since 1.8.0
 */
public record SettingChangeResult(@NotNull Status status, @Nullable Object value, @NotNull String reason) {

    public SettingChangeResult {
        Objects.requireNonNull(status, "status");
        reason = reason == null ? "" : reason;
    }

    /** The stored value, when it was saved. */
    @NotNull
    public Optional<Object> getValue() {
        return Optional.ofNullable(value);
    }

    /** Whether the value is in {@code settings.yml} now. */
    public boolean isSaved() {
        return status == Status.SAVED || status == Status.SAVED_NOT_APPLIED || status == Status.UNCHANGED;
    }

    /** What a setting change did. */
    public enum Status {
        /** Saved to {@code settings.yml} and applied to the running server. */
        SAVED,
        /** The setting already held this value; nothing was written. */
        UNCHANGED,
        /** Saved, but refreshing the running server afterwards failed; it applies after a restart. */
        SAVED_NOT_APPLIED,
        /** The value was refused; see {@link SettingChangeResult#reason()}. */
        INVALID,
        /** No editable setting has this path. */
        UNKNOWN_SETTING,
        /** {@code settings.yml} could not be written; nothing changed. */
        FAILED
    }
}
