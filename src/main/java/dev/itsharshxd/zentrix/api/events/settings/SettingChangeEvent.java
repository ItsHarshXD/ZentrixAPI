package dev.itsharshxd.zentrix.api.events.settings;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called after an editable {@code settings.yml} value was changed - in the settings menu or
 * through {@code SettingsService#set} - saved and applied to the running server.
 *
 * <p>A notification. Values are as stored; {@link org.bukkit.Location}s are the stored objects, so
 * copy them before changing them.
 *
 * <p>Main thread; only fired while a listener is registered.
 *
 * @since 1.8.0
 */
public final class SettingChangeEvent extends ZentrixEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final String path;
    private final Object previousValue;
    private final Object newValue;
    private final boolean applied;

    public SettingChangeEvent(@NotNull String path, @Nullable Object previousValue, @Nullable Object newValue,
                              boolean applied) {
        super(false);
        this.path = Objects.requireNonNull(path, "path");
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.applied = applied;
    }

    /** The setting's {@code settings.yml} path. */
    @NotNull
    public String getPath() {
        return path;
    }

    /** The value before, when there was one. */
    @NotNull
    public Optional<Object> getPreviousValue() {
        return Optional.ofNullable(previousValue);
    }

    /** The value now stored; empty when the setting was cleared. */
    @NotNull
    public Optional<Object> getNewValue() {
        return Optional.ofNullable(newValue);
    }

    /** Whether the running server was refreshed; false when that failed and a restart applies it. */
    public boolean isApplied() {
        return applied;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
