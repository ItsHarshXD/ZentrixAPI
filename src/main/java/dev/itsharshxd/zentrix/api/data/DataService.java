package dev.itsharshxd.zentrix.api.data;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Service for accessing Zentrix's data folder and managing addon configurations.
 * <p>
 * This service allows addon developers to:
 * <ul>
 *   <li>Access the main Zentrix data folder</li>
 *   <li>Create and manage addon-specific data folders</li>
 *   <li>Read and write configuration files</li>
 *   <li>Store addon data persistently</li>
 * </ul>
 * </p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * DataService dataService = api.getDataService();
 *
 * // Get your addon's data folder (creates if not exists)
 * File myAddonFolder = dataService.getAddonDataFolder("my-addon");
 *
 * // Create a config file in your addon folder
 * File configFile = new File(myAddonFolder, "config.yml");
 * FileConfiguration config = dataService.loadYamlConfiguration(configFile);
 *
 * // Read/write values
 * config.set("my-setting", true);
 * dataService.saveYamlConfiguration(config, configFile);
 *
 * // Or use the helper method for quick config access
 * FileConfiguration quickConfig = dataService.getOrCreateConfig("my-addon", "settings.yml");
 * }</pre>
 *
 * <h2>Directory Structure</h2>
 * <pre>
 * plugins/Zentrix/
 * ├── config.yml          (main plugin config - READ ONLY for addons)
 * ├── settings.yml        (plugin settings - READ ONLY for addons)
 * ├── addons/             (addon data folder root)
 * │   ├── my-addon/       (your addon's folder)
 * │   │   ├── config.yml
 * │   │   └── data/
 * │   └── other-addon/
 * └── ...
 * </pre>
 *
 * @author ItsHarshXD
 * @since 1.0.1
 */
public interface DataService {

    // ==========================================
    // Data Folder Access
    // ==========================================

    /**
     * Gets the main Zentrix plugin data folder.
     * <p>
     * This is the root folder where Zentrix stores all its data.
     * Addons should generally use {@link #getAddonDataFolder(String)}
     * instead to keep their data organized.
     * </p>
     *
     * <p><b>Warning:</b> Modifying core Zentrix config files may cause
     * unexpected behavior. Use with caution.</p>
     *
     * @return The main plugin data folder (e.g., plugins/Zentrix/)
     */
    @NotNull
    File getPluginDataFolder();

    /**
     * Gets the root folder where all addon data is stored.
     * <p>
     * This folder is: {@code plugins/Zentrix/addons/}
     * </p>
     *
     * @return The addons data folder root
     */
    @NotNull
    File getAddonsFolder();

    /**
     * Gets (or creates) a dedicated data folder for a specific addon.
     * <p>
     * This creates a folder at: {@code plugins/Zentrix/addons/{addonId}/}
     * </p>
     *
     * <p>The folder is automatically created if it doesn't exist.</p>
     *
     * @param addonId The unique identifier for your addon (lowercase, no spaces)
     * @return The addon's dedicated data folder
     * @throws IllegalArgumentException if addonId is null, empty, or contains invalid characters
     */
    @NotNull
    File getAddonDataFolder(@NotNull String addonId);

    /**
     * Gets a subfolder within an addon's data folder.
     * <p>
     * Example: {@code getAddonSubfolder("my-addon", "cache")}
     * returns: {@code plugins/Zentrix/addons/my-addon/cache/}
     * </p>
     *
     * @param addonId   The addon identifier
     * @param subfolder The subfolder name (can include path separators)
     * @return The subfolder (created if not exists)
     */
    @NotNull
    File getAddonSubfolder(@NotNull String addonId, @NotNull String subfolder);

    // ==========================================
    // Configuration File Management
    // ==========================================

    /**
     * Loads a YAML configuration from a file.
     * <p>
     * If the file doesn't exist, returns an empty configuration.
     * </p>
     *
     * @param file The file to load
     * @return The loaded configuration (never null)
     */
    @NotNull
    YamlConfiguration loadYamlConfiguration(@NotNull File file);

    /**
     * Saves a YAML configuration to a file.
     *
     * @param config The configuration to save
     * @param file   The destination file
     * @return true if saved successfully, false otherwise
     */
    boolean saveYamlConfiguration(@NotNull FileConfiguration config, @NotNull File file);

    /**
     * Gets or creates a configuration file for an addon.
     * <p>
     * This is a convenience method that:
     * <ol>
     *   <li>Gets the addon's data folder</li>
     *   <li>Creates the config file if it doesn't exist</li>
     *   <li>Loads and returns the configuration</li>
     * </ol>
     * </p>
     *
     * @param addonId  The addon identifier
     * @param fileName The config file name (e.g., "config.yml")
     * @return The loaded configuration
     */
    @NotNull
    YamlConfiguration getOrCreateConfig(@NotNull String addonId, @NotNull String fileName);

    /**
     * Saves an addon's configuration file.
     *
     * @param addonId  The addon identifier
     * @param fileName The config file name
     * @param config   The configuration to save
     * @return true if saved successfully
     */
    boolean saveConfig(@NotNull String addonId, @NotNull String fileName, @NotNull FileConfiguration config);

    /**
     * Copies a default configuration from an addon's resources.
     * <p>
     * Use this to copy a default config.yml from your addon's JAR
     * to the addon data folder if it doesn't already exist.
     * </p>
     *
     * @param addonId      The addon identifier
     * @param resourcePath The path to the resource in the JAR
     * @param destination  The destination file name in the addon folder
     * @param replace      Whether to replace existing file
     * @return true if copied successfully or file already exists (when replace=false)
     */
    boolean copyDefaultConfig(
        @NotNull String addonId,
        @NotNull InputStream resourcePath,
        @NotNull String destination,
        boolean replace
    );

    // ==========================================
    // File Operations
    // ==========================================

    /**
     * Gets a file from an addon's data folder.
     *
     * @param addonId  The addon identifier
     * @param fileName The file name or relative path
     * @return The file object (may not exist yet)
     */
    @NotNull
    File getAddonFile(@NotNull String addonId, @NotNull String fileName);

    /**
     * Checks if a file exists in an addon's data folder.
     *
     * @param addonId  The addon identifier
     * @param fileName The file name or relative path
     * @return true if the file exists
     */
    boolean addonFileExists(@NotNull String addonId, @NotNull String fileName);

    /**
     * Deletes a file from an addon's data folder.
     *
     * @param addonId  The addon identifier
     * @param fileName The file name or relative path
     * @return true if deleted successfully or file didn't exist
     */
    boolean deleteAddonFile(@NotNull String addonId, @NotNull String fileName);

    /**
     * Lists all files in an addon's data folder.
     *
     * @param addonId The addon identifier
     * @return Array of file names (empty if folder doesn't exist or is empty)
     */
    @NotNull
    String[] listAddonFiles(@NotNull String addonId);

    /**
     * Lists all files in a subfolder of an addon's data folder.
     *
     * @param addonId   The addon identifier
     * @param subfolder The subfolder path
     * @return Array of file names (empty if folder doesn't exist or is empty)
     */
    @NotNull
    String[] listAddonFiles(@NotNull String addonId, @NotNull String subfolder);

    // ==========================================
    // Zentrix Config Access (Read-Only)
    // ==========================================

    /**
     * Gets a read-only copy of a Zentrix configuration file.
     * <p>
     * Available config names:
     * <ul>
     *   <li>{@code "settings"} - General plugin settings</li>
     *   <li>{@code "phases"} - Game phase configurations</li>
     *   <li>{@code "game-types"} - Game mode configurations</li>
     *   <li>{@code "teams"} - Team configurations</li>
     *   <li>{@code "classes"} - Player class configurations</li>
     *   <li>{@code "currency"} - Currency system configuration</li>
     *   <li>{@code "broadcasts"} - Broadcast message configurations</li>
     * </ul>
     * </p>
     *
     * <p><b>Note:</b> These are read-only snapshots. Modifications won't
     * affect the actual plugin configuration.</p>
     *
     * @param configName The config name (without .yml extension)
     * @return The configuration, or empty if not found
     */
    @NotNull
    Optional<YamlConfiguration> getZentrixConfig(@NotNull String configName);

    /**
     * Gets a value from a Zentrix configuration file.
     * <p>
     * Convenience method for quick config value access.
     * </p>
     *
     * @param configName The config name (without .yml extension)
     * @param path       The configuration path
     * @return The value, or null if not found
     */
    @Nullable
    Object getZentrixConfigValue(@NotNull String configName, @NotNull String path);

    /**
     * Gets a string value from a Zentrix configuration file.
     *
     * @param configName   The config name
     * @param path         The configuration path
     * @param defaultValue Default value if not found
     * @return The string value or default
     */
    @NotNull
    String getZentrixConfigString(
        @NotNull String configName,
        @NotNull String path,
        @NotNull String defaultValue
    );

    /**
     * Gets an int value from a Zentrix configuration file.
     *
     * @param configName   The config name
     * @param path         The configuration path
     * @param defaultValue Default value if not found
     * @return The int value or default
     */
    int getZentrixConfigInt(@NotNull String configName, @NotNull String path, int defaultValue);

    /**
     * Gets a boolean value from a Zentrix configuration file.
     *
     * @param configName   The config name
     * @param path         The configuration path
     * @param defaultValue Default value if not found
     * @return The boolean value or default
     */
    boolean getZentrixConfigBoolean(
        @NotNull String configName,
        @NotNull String path,
        boolean defaultValue
    );
}
