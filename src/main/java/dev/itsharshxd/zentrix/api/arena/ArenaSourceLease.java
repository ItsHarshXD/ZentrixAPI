package dev.itsharshxd.zentrix.api.arena;

import org.jetbrains.annotations.NotNull;

/**
 * A source arena a provider has set aside for Zentrix.
 *
 * <p>A lease is a promise in both directions. The provider keeps the source registered and does not
 * hand it to anything else — including its own matchmaking — until the lease ends, and Zentrix ends
 * every lease it takes out exactly once: {@link #release()} when the source turned out not to be
 * needed, {@link #consume(String)} when a match actually started from it. After
 * {@code consume} the provider owns the source again and may clean it up under its ordinary rules,
 * because the runtime world is a copy that no longer depends on it.
 *
 * <p>A lease survives a restart only as the source arena name Zentrix stored. A provider that keeps
 * its own reservation state across restarts should ask
 * {@link ArenaSourceService#isSourceBusy(String)} before reclaiming a source it believes is idle:
 * Zentrix reports a source held by a published custom game as busy.
 */
public interface ArenaSourceLease {

    /** The registered source arena this lease covers. */
    @NotNull String sourceArenaName();

    /**
     * The scenario overrides matches created from this source should run under.
     *
     * <p>Read once, when Zentrix takes the lease, and kept for as long as the match created from
     * this source lasts. That is what makes the answer a promise about one match rather than a
     * setting: a provider that changes its mind afterwards changes the next match, and never the
     * one already playing.
     *
     * <p>The profile is read ahead of the arena's stored overrides and the global configuration,
     * and nothing in it is written to {@code scenarios.yml}. An empty profile — the default — means
     * matches on this source read the server's own scenario configuration, exactly as they would
     * from any other arena.
     *
     * <p>Because nothing is written down, a profile lives only as long as the server runs. A source
     * whose lease was taken out before a restart — a published custom game holds one for as long as
     * its schedule lasts — is adopted afterwards without one, and the match it eventually creates
     * reads the ordinary configuration. A provider that needs its overrides to survive that has to
     * store them itself and offer them again on the next lease.
     *
     * @since 1.6.0
     */
    @NotNull
    default java.util.Optional<dev.itsharshxd.zentrix.api.scenario.ScenarioProfile>
            scenarioProfile() {
        return java.util.Optional.empty();
    }

    /** Gives the source back unused. Calling this more than once has no further effect. */
    void release();

    /**
     * Reports that a match started from this source.
     *
     * @param runtimeId the runtime game created from it, for the provider's own bookkeeping
     */
    void consume(@NotNull String runtimeId);
}
