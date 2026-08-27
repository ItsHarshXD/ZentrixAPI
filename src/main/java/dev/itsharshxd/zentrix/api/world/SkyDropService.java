package dev.itsharshxd.zentrix.api.world;

import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Putting a player into the air above a world, the way a match drops its players in.
 *
 * <p>This is the reusable half of Zentrix's own arrival: find a point with real ground under it,
 * inside the world border, with enough free air beneath to glide through, then place the player
 * there and start their glider. Anything that wants to insert a player from above — a late join, a
 * revival, a scenario that scatters the survivors again — can use it instead of inventing its own
 * search and hoping the point it picked is not inside a mountain or outside the border.
 *
 * <p>Every method touches worlds, chunks and players and must be called on the Bukkit main thread.
 * Searching can load chunks, so keep {@link SkyDropRequest#attempts()} modest for points far from
 * anything already loaded.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * SkyDropService drops = ZentrixAPI.get().getSkyDropService();
 * SkyDropRequest request = SkyDropRequest.above(arena.getWorldBorder().getCenter())
 *         .withinRadius(64)
 *         .atAltitude(80);
 *
 * drops.findDropPoint(request).ifPresent(point -> drops.drop(player, point, true));
 * }</pre>
 *
 * @since 1.6.0
 */
public interface SkyDropService {

    /**
     * Looks for a point matching the request.
     *
     * <p>The anchor's own column is tried first, so a request with no radius either yields the
     * point above the anchor or nothing. Empty means the area cannot offer a drop at all: no
     * ground, not enough air above it, or nothing inside the border.
     *
     * @param request the area to drop into
     * @return a drop point, or empty when the area offers none
     */
    @NotNull
    Optional<Location> findDropPoint(@NotNull SkyDropRequest request);

    /**
     * Places a player at a drop point and optionally starts their glider.
     *
     * <p>The player is taken off any vehicle first, so the drop is never swallowed by whatever they
     * were riding. Asking for a glider on a server without Matrix Gliders places the player anyway
     * and reports {@link SkyDropResult#PLACED_WITHOUT_GLIDER}.
     *
     * @param player       the player to move
     * @param dropPoint    where to put them, normally from {@link #findDropPoint(SkyDropRequest)}
     * @param deployGlider whether to start their glider once they are there
     * @return what happened
     */
    @NotNull
    SkyDropResult drop(
            @NotNull Player player, @NotNull Location dropPoint, boolean deployGlider);

    /**
     * Finds a drop point and places the player at it in one step.
     *
     * @return {@link SkyDropResult#FAILED} when the request yielded no point
     */
    @NotNull
    default SkyDropResult drop(
            @NotNull Player player, @NotNull SkyDropRequest request, boolean deployGlider) {
        return findDropPoint(request)
                .map(point -> drop(player, point, deployGlider))
                .orElse(SkyDropResult.FAILED);
    }
}
