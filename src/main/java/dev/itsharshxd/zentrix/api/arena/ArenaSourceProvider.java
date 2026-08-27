package dev.itsharshxd.zentrix.api.arena;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/**
 * Supplies source arenas on request, for the features that need one Zentrix cannot pick itself.
 *
 * <p>Sources installed through {@link ArenaSourceService#registerSource(String)} belong to the addon
 * that installed them, so Zentrix never picks one on its own. A provider is how an addon offers them
 * anyway: Zentrix asks, the addon decides which source to give out, and the answer is a lease the
 * addon can account for.
 *
 * <p>Zentrix asks for each {@link ArenaSourcePurpose}, and always as a last resort — a queue is
 * offered every waiting game, every game still being created, and every arena this server
 * configured itself before a provider hears about it.
 *
 * <p>Only one provider is registered at a time, and it is called on the main thread. Returning an
 * empty result is a normal answer meaning "nothing available right now"; the caller reports that to
 * the player rather than falling back to a source the addon owns. A provider that expects to have
 * something shortly may instead return a future it completes later: Zentrix keeps the player's
 * matchmaking title up meanwhile, and abandons the request — releasing a lease that arrives
 * afterwards — if the wait runs past two minutes.
 */
public interface ArenaSourceProvider {

    /**
     * Reserves a source arena for one request.
     *
     * @return a lease over a registered source arena, or an empty result when none can be supplied
     */
    @NotNull CompletableFuture<Optional<ArenaSourceLease>> reserveSource(
            @NotNull ArenaSourceRequest request);
}
