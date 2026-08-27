package dev.itsharshxd.zentrix.api.scenario;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import dev.itsharshxd.zentrix.api.scenario.vote.ScenarioVoteResult;
import dev.itsharshxd.zentrix.api.scenario.vote.ScenarioVoteSnapshot;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Registration, configuration, selection and voting for scenarios.
 *
 * <p>Registration is in-memory. A scenario registered here is immediately visible everywhere a
 * scenario can appear — {@code /zx scenarioes}, the management GUIs, voting pools, automatic
 * selection, validation and every lookup on this service — but nothing about it is written to
 * {@code scenarios.yml} until an administrator configures it through the GUI or through
 * {@link #setEnabled(String, boolean)} / {@link #setGlobalSetting(String, String, Object)}.
 *
 * <p>Zentrix unregisters every scenario a plugin owns when that plugin is disabled. Matches running
 * such a scenario have its instance deactivated with
 * {@link ScenarioInstance.DeactivationReason#PROVIDER_DISABLED} and everything it registered
 * released; the match itself carries on without it.
 *
 * @since 1.6.0
 */
public interface ScenarioService {

    // ==========================================
    // Registry
    // ==========================================

    /**
     * Adds a scenario to the runtime registry.
     *
     * @param owner    the plugin providing the scenario; its shutdown removes the scenario again
     * @param scenario the scenario
     * @return the registration handle
     * @throws IllegalArgumentException if the ID is already taken by a different provider or the
     *                                  descriptor is invalid
     */
    @NotNull
    ScenarioRegistration register(@NotNull Plugin owner, @NotNull Scenario scenario);

    /**
     * Removes a scenario from the registry.
     *
     * @return true when a scenario with this ID was removed
     */
    boolean unregister(@NotNull String scenarioId);

    /** Removes every scenario a plugin registered. */
    int unregisterAll(@NotNull Plugin owner);

    /** Whether a scenario with this ID is registered. */
    boolean isRegistered(@NotNull String scenarioId);

    /** Every registered scenario, ordered by descending priority then ID. */
    @NotNull
    Collection<ScenarioRegistration> getRegistrations();

    /** One registration by ID. */
    @NotNull
    Optional<ScenarioRegistration> getRegistration(@NotNull String scenarioId);

    /** One scenario's metadata by ID. */
    @NotNull
    Optional<ScenarioDescriptor> getDescriptor(@NotNull String scenarioId);

    /** Every scenario a given plugin registered. */
    @NotNull
    Collection<ScenarioRegistration> getRegistrations(@NotNull Plugin owner);

    /** Every scenario carrying a tag. */
    @NotNull
    Collection<ScenarioRegistration> getByTag(@NotNull String tag);

    /**
     * The start announcement a scenario would be introduced with.
     *
     * <p>Resolved exactly as Zentrix resolves it when a match starts: the locale entry written for
     * this scenario, then the lines the scenario
     * {@linkplain ScenarioDescriptor#announcement() declared itself}, then the locale's shared
     * fallback layout. Lines still carry {@code &} colour codes and {@code <center>} tags, so an
     * addon showing them elsewhere gets the same wording players hear.
     *
     * @return the raw lines, empty when the scenario is unknown or announces nothing
     */
    @NotNull
    List<String> getAnnouncement(@NotNull String scenarioId);

    /** How long Zentrix waits between two scenario announcements, in seconds. */
    int getAnnouncementDelaySeconds();

    /**
     * Every registered scenario that declares it takes charge of a capability.
     *
     * <p>Useful for an addon that wants to know what else on this server touches the same gameplay
     * area before deciding how to behave.
     */
    @NotNull
    Collection<ScenarioRegistration> getByCapability(@NotNull ScenarioCapability capability);

    /**
     * Every registered scenario that cannot run alongside this one right now.
     *
     * <p>Combines the conflicts both sides declared by ID with the ones derived from
     * {@link ScenarioCapability}, so the answer reflects what
     * {@link #validate(Collection)} would actually reject. It changes as scenarios are registered
     * and unregistered.
     *
     * @return the conflicting scenario IDs, empty when the scenario is unknown or conflicts with
     *         nothing
     */
    @NotNull
    java.util.Set<String> getConflicts(@NotNull String scenarioId);

    // ==========================================
    // Enablement and configuration
    // ==========================================

    /** Whether a scenario is switched on and its required plugins are present. */
    boolean isEnabled(@NotNull String scenarioId);

    /**
     * Switches a scenario on or off and persists the choice.
     *
     * <p>This is an explicit administrator action, so it does write the scenario's section to
     * {@code scenarios.yml} even for a dynamically registered scenario.
     *
     * @return a future completing with true once the change is stored
     */
    @NotNull
    CompletableFuture<Boolean> setEnabled(@NotNull String scenarioId, boolean enabled);

    /** The globally configured value of one setting, before any arena or lobby override. */
    @NotNull
    Optional<Object> getGlobalSetting(@NotNull String scenarioId, @NotNull String key);

    /**
     * Stores a global setting value and persists it.
     *
     * @return a future completing with true once the value validated and was stored
     */
    @NotNull
    CompletableFuture<Boolean> setGlobalSetting(
            @NotNull String scenarioId, @NotNull String key, @NotNull Object value);

    /** Removes a stored global value so the scenario's own default applies again. */
    @NotNull
    CompletableFuture<Boolean> clearGlobalSetting(@NotNull String scenarioId, @NotNull String key);

    /**
     * Stores an arena-level override.
     *
     * @param sourceArenaName the template arena, not a {@code game-*} runtime ID
     */
    @NotNull
    CompletableFuture<Boolean> setArenaSetting(
            @NotNull String sourceArenaName,
            @NotNull String scenarioId,
            @NotNull String key,
            @NotNull Object value);

    /** Removes an arena-level override. */
    @NotNull
    CompletableFuture<Boolean> clearArenaSetting(
            @NotNull String sourceArenaName, @NotNull String scenarioId, @NotNull String key);

    /**
     * Applies a temporary override to one match, ahead of its arena and the global configuration.
     *
     * <p>Overrides only take effect while the match is still in its lobby; once the selection is
     * locked, the settings snapshot the match runs on no longer changes. Nothing is persisted.
     *
     * @return true when the override was applied
     */
    boolean setLobbyOverride(
            @NotNull ZentrixGame game,
            @NotNull String scenarioId,
            @NotNull String key,
            @NotNull Object value);

    /** Drops every temporary override a match collected. */
    void clearLobbyOverrides(@NotNull ZentrixGame game);

    /** How a match decides its scenarios, resolved through the override chain. */
    @NotNull
    ScenarioSelectionMode getSelectionMode(@NotNull ZentrixGame game);

    /** The globally configured selection mode. */
    @NotNull
    ScenarioSelectionMode getGlobalSelectionMode();

    /** Sets and persists the global selection mode. */
    @NotNull
    CompletableFuture<Boolean> setGlobalSelectionMode(@NotNull ScenarioSelectionMode mode);

    /**
     * Zentrix's own scenario configuration, in the shape of a {@link ScenarioProfile}.
     *
     * <p>Every value is the resolved one, so a key nobody configured appears as the value that
     * would actually apply rather than being absent. That makes this the answer to "what happens if
     * I override nothing", which is what an addon offering overrides of its own needs in order to
     * show what it is overriding.
     *
     * <p>Covers the selection mode, the administrator set, the automatic and voting rules, and, for
     * every registered scenario, whether it is switched on, which game types it is kept out of, and
     * the settings that have a configured value. A scenario setting left at the scenario's own
     * default is not listed: {@link ScenarioDescriptor#settings()} already carries that default.
     *
     * <p>This is a read of the configuration as it stands, not a live view. Nothing about the
     * returned profile is attached to anything.
     *
     * @since 1.6.0
     */
    @NotNull
    ScenarioProfile getGlobalProfile();

    // ==========================================
    // Selection
    // ==========================================

    /** The scenarios currently registered, enabled, and usable in a selection. */
    @NotNull
    Collection<ScenarioDescriptor> getSelectablePool();

    /**
     * Checks a combination without applying it: resolves dependencies, rejects conflicts and drops
     * scenarios whose plugins are missing.
     */
    @NotNull
    ScenarioValidation validate(@NotNull Collection<String> scenarioIds);

    /** A match's selection, empty until it has one. */
    @NotNull
    Optional<ScenarioSelection> getSelection(@NotNull ZentrixGame game);

    /** The scenarios active in a match right now, in activation order. */
    @NotNull
    List<ScenarioDescriptor> getActiveScenarios(@NotNull ZentrixGame game);

    /** Whether one scenario is active in a match. */
    boolean isActive(@NotNull ZentrixGame game, @NotNull String scenarioId);

    /** A scenario's resolved settings for a match, empty when it is not active there. */
    @NotNull
    Optional<ScenarioSettings> getSettings(@NotNull ZentrixGame game, @NotNull String scenarioId);

    /**
     * Replaces a match's selection before it starts.
     *
     * <p>Rejected once the match locked its scenarios in, so a running match's rules cannot be
     * swapped underneath its players, and for a match whose selection mode is
     * {@link ScenarioSelectionMode#DISABLED}, which runs no scenario at all.
     *
     * @return the validation of the requested set; check {@link ScenarioValidation#valid()}
     */
    @NotNull
    ScenarioValidation setSelection(
            @NotNull ZentrixGame game, @NotNull Collection<String> scenarioIds);

    /**
     * Deactivates one scenario in a running match, releasing everything it owns there.
     *
     * <p>The rest of the match is untouched. This is the same path Zentrix uses to isolate a
     * scenario that keeps failing.
     */
    boolean deactivate(@NotNull ZentrixGame game, @NotNull String scenarioId);

    // ==========================================
    // Voting
    // ==========================================

    /** A match's vote as it stands, empty when the match never voted. */
    @NotNull
    Optional<ScenarioVoteSnapshot> getVote(@NotNull ZentrixGame game);

    /** Whether a match is currently accepting votes. */
    boolean isVotingOpen(@NotNull ZentrixGame game);

    /** Casts or, when {@code allow-changes} is on and the vote already exists, withdraws a vote. */
    @NotNull
    ScenarioVoteResult vote(@NotNull Player player, @NotNull String scenarioId);

    /** Takes back one of a player's votes. */
    @NotNull
    ScenarioVoteResult withdrawVote(@NotNull Player player, @NotNull String scenarioId);

    /**
     * Closes a running vote immediately and locks its winners in.
     *
     * <p>This is the administrator's escape hatch from a lobby that would otherwise wait for the
     * timer. The configured tie and no-vote rules still apply, so the result is always a valid set.
     *
     * @return the resulting selection, empty when the match was not voting
     */
    @NotNull
    Optional<ScenarioSelection> forceResolveVote(@NotNull ZentrixGame game);

    /** Opens the scenario voting menu for a player, if their match is voting. */
    boolean openVoteMenu(@NotNull Player player);

    // ==========================================
    // Diagnostics
    // ==========================================

    /**
     * How often each scenario failed in a match, keyed by scenario ID.
     *
     * <p>Useful for spotting a misbehaving addon scenario; Zentrix isolates one on its own once it
     * exceeds the configured failure limit.
     */
    @NotNull
    Map<String, Integer> getFailureCounts(@NotNull ZentrixGame game);
}
