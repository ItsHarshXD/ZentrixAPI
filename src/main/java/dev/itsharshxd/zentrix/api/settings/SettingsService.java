package dev.itsharshxd.zentrix.api.settings;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Reading and changing the {@code settings.yml} values of Zentrix's settings menu, with the same
 * validation and the same effect on the running server.
 *
 * <p>Only the settings the menu offers are available - an explicit allow-list without storage
 * credentials, licensing or anything else sensitive. A change is validated, written to
 * {@code settings.yml}, the file is reloaded and whatever depends on the setting is refreshed,
 * exactly as when an administrator changes it in the menu. Successful changes from either fire
 * {@link dev.itsharshxd.zentrix.api.events.settings.SettingChangeEvent}.
 *
 * <p>Every method must be called on the main thread. Changes are persistent: there is no
 * temporary overlay, and they survive restarts.
 *
 * @since 1.8.0
 */
public interface SettingsService {

    /** Every editable setting, in the settings menu's order. */
    @NotNull
    List<SettingDescriptor> getSettings();

    /** The editable setting at a {@code settings.yml} path. */
    @NotNull
    Optional<SettingDescriptor> getSetting(@NotNull String path);

    /**
     * The value an editable setting holds in the loaded {@code settings.yml}.
     *
     * @return the value; empty for an unknown path or an unset value
     */
    @NotNull
    Optional<Object> getValue(@NotNull String path);

    /**
     * Validates and saves a value, then applies it to the running server.
     * <p>
     * Completes on the main thread. Usually at once; a Cornucopia schematic that has to be read
     * first completes once it was. Pass null only for {@link SettingDescriptor.Type#NULLABLE_LOCATION}.
     * </p>
     *
     * @throws IllegalStateException off the main thread
     */
    @NotNull
    CompletableFuture<SettingChangeResult> set(@NotNull String path, @Nullable Object value);
}
