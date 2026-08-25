package dev.itsharshxd.zentrix.api.scenario;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/**
 * A gameplay area a scenario takes charge of, named rather than enumerated.
 *
 * <p>Conflicts between scenarios are normally declared by ID, which only works between scenarios
 * that already know about each other. A capability is the open-ended version of the same idea: a
 * scenario {@linkplain ScenarioDescriptor.Builder#provides(ScenarioCapability...) declares} what it
 * takes charge of, and another scenario
 * {@linkplain ScenarioDescriptor.Builder#conflictsWithCapability(ScenarioCapability...) declares}
 * what it cannot live alongside. Validation then rejects the pair without either of them naming the
 * other, so a scenario written today stays incompatible with one written years later.
 *
 * <p>A scenario that both provides and conflicts with the same capability forms a mutual-exclusion
 * group: any two members of the group conflict, while the group as a whole stays open for new
 * members. That is how, for example, two scenarios that each pin the world clock keep each other
 * out.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * // Grants enchantments, so anything that bans them will not run alongside it.
 * ScenarioDescriptor.builder("toolsmiths-edge")
 *         .provides(ScenarioCapability.ENCHANTMENTS)
 *         .build();
 *
 * // Bans enchantments, whoever grants them.
 * ScenarioDescriptor.builder("no-enchants")
 *         .conflictsWithCapability(ScenarioCapability.ENCHANTMENTS)
 *         .build();
 *
 * // A mutual-exclusion group only one member of which may run.
 * ScenarioDescriptor.builder("permanent-day")
 *         .provides(ScenarioCapability.WORLD_TIME)
 *         .conflictsWithCapability(ScenarioCapability.WORLD_TIME)
 *         .build();
 * }</pre>
 *
 * <p>Addon capabilities should be namespaced — {@code myaddon:gravity} — so two addons cannot
 * collide. The {@code zentrix:} namespace is reserved for the constants below.
 *
 * @since 1.8.0
 */
public final class ScenarioCapability {

    /**
     * Adding, upgrading or transferring vanilla enchantments on items players end up holding.
     *
     * <p>Declare this whenever the scenario can put an enchantment on a player's item — through
     * crafting, loot, kits or an ability — so scenarios that ban enchantments can keep clear of it.
     */
    public static final ScenarioCapability ENCHANTMENTS = of("zentrix:enchantments");

    /** Pinning or driving the world clock of the match's worlds. */
    public static final ScenarioCapability WORLD_TIME = of("zentrix:world-time");

    /** Pinning or driving the weather of the match's worlds. */
    public static final ScenarioCapability WORLD_WEATHER = of("zentrix:world-weather");

    /** Deciding when the match is over, in place of Zentrix's own alive-team count. */
    public static final ScenarioCapability WIN_CONDITION = of("zentrix:win-condition");

    /**
     * Changing who players appear to be — their name, their skin, or both.
     *
     * <p>Declare this whenever the scenario masks, scrambles or hides player identities, so anything
     * that depends on players recognising each other can keep clear of it.
     *
     * @since 1.9.0
     */
    public static final ScenarioCapability PLAYER_IDENTITY = of("zentrix:player-identity");

    /**
     * Deciding where the deathmatch happens, in place of Zentrix's copied template arena.
     *
     * <p>Only one scenario can move the final fight, so every scenario that answers the
     * deathmatch-destination hook should both provide and conflict with this.
     *
     * @since 1.9.0
     */
    public static final ScenarioCapability DEATHMATCH_LOCATION = of("zentrix:deathmatch-location");

    /**
     * Taking a player's death over from Zentrix's own elimination handling.
     *
     * <p>Declare this whenever the scenario answers
     * {@link dev.itsharshxd.zentrix.api.scenario.hook.GameplayHooks#PLAYER_DEATH} with a
     * replacement, which tells Zentrix to leave the death alone entirely. Only one scenario can own
     * a death, so anything that does should both provide and conflict with this and let validation
     * keep the pair apart.
     *
     * @since 1.10.0
     */
    public static final ScenarioCapability DEATH_HANDLING = of("zentrix:death-handling");

    private final String id;

    private ScenarioCapability(String id) {
        this.id = id;
    }

    /**
     * Creates a capability key.
     *
     * @param id the capability identifier, lower-cased and trimmed; use a {@code namespace:name}
     *           form for anything an addon defines
     * @throws IllegalArgumentException when the identifier is blank or malformed
     */
    @NotNull
    public static ScenarioCapability of(@NotNull String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("A scenario capability needs an id");
        }
        String normalized = id.trim().toLowerCase(Locale.ROOT);
        if (!normalized.matches("[a-z0-9][a-z0-9_-]{0,63}(:[a-z0-9][a-z0-9_-]{0,63})?")) {
            throw new IllegalArgumentException("Invalid scenario capability '" + id
                    + "': use 'name' or 'namespace:name' with lower-case letters, digits, '-' or '_'");
        }
        return new ScenarioCapability(normalized);
    }

    /** The normalized identifier this capability is compared by. */
    @NotNull
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ScenarioCapability capability && id.equals(capability.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
