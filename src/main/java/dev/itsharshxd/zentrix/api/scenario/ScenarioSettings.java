package dev.itsharshxd.zentrix.api.scenario;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * A scenario's settings as they apply to one match, already resolved.
 *
 * <p>Resolution runs in a fixed order and stops at the first source that supplies a value:
 *
 * <ol>
 *   <li>the temporary lobby override, set for this one match;</li>
 *   <li>the source arena's override;</li>
 *   <li>the global configuration in {@code scenarios.yml};</li>
 *   <li>the scenario's own default from its {@link ScenarioSetting} declaration.</li>
 * </ol>
 *
 * <p>The view is a snapshot taken when the match locked its scenarios in, so a reload or a GUI edit
 * during a running match never changes the rules underneath the players.
 *
 * <p>Every accessor falls back to the declared default when a stored value is missing or unusable,
 * so a broken configuration degrades to the scenario's defaults instead of failing the match.
 *
 * @since 1.6.0
 */
public interface ScenarioSettings {

    /** The scenario these settings belong to. */
    @NotNull
    String scenarioId();

    /** Whether the setting key was declared by the scenario. */
    boolean has(@NotNull String key);

    /** The raw resolved value, empty when the key was never declared. */
    @NotNull
    Optional<Object> raw(@NotNull String key);

    /** Where the resolved value came from. */
    @NotNull
    ScenarioSettingSource source(@NotNull String key);

    boolean getBoolean(@NotNull String key);

    int getInt(@NotNull String key);

    double getDouble(@NotNull String key);

    @NotNull
    String getString(@NotNull String key);

    @NotNull
    List<String> getStringList(@NotNull String key);

    /**
     * The setting read as a material list, skipping names that no longer resolve on this server
     * version.
     */
    @NotNull
    List<Material> getMaterials(@NotNull String key);

    /** The setting read as a single material, empty when the name does not resolve. */
    @NotNull
    Optional<Material> getMaterial(@NotNull String key);

    /**
     * The setting read as a list of structured entries.
     *
     * <p>Every entry is shaped by the {@link ScenarioEntrySchema} the setting declared, and entries
     * that no longer fit that schema are left out rather than handed over half-formed. A key that is
     * not a {@link ScenarioSettingType#STRUCTURED_LIST} yields an empty list.
     *
     * @since 1.6.0
     */
    @NotNull
    List<ScenarioEntry> getEntries(@NotNull String key);

    /** Every resolved value, keyed by setting key. */
    @NotNull
    Map<String, Object> asMap();
}
