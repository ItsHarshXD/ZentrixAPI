package dev.itsharshxd.zentrix.api.glider;

import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Zentrix's built-in gliders: the catalog players choose from, the glides in progress and the
 * previews shown in the cosmetics menu.
 *
 * <p>Players glide into the map from the dragon bus or a sky drop. While gliding they wear their
 * glider - the one they chose and still own, or the server's default - on their back and drift
 * forward under Slow Falling until they land. Which gliders a player owns is managed by
 * {@link dev.itsharshxd.zentrix.api.cosmetics.CosmeticsService}.
 *
 * <p>The glider catalog is not part of the Free edition: there {@link #isCatalogAvailable()} is
 * {@code false}, the catalog is empty, and {@link #deploy} and {@link #preview} fail, while
 * {@link #isGliding} and {@link #stop} still cover the glides Zentrix starts itself. Every method
 * that changes something must be called on the main thread.
 *
 * @since 1.8.0
 */
public interface GliderService {

    /** Whether the glider catalog and previews are available on this server. */
    boolean isCatalogAvailable();

    /** Every glider in the catalog, in the server's order. */
    @NotNull
    List<GliderDefinition> getGliders();

    /** The glider with this ID. */
    @NotNull
    Optional<GliderDefinition> getGlider(@NotNull String id);

    /** The glider used by players who chose none. */
    @NotNull
    Optional<GliderDefinition> getDefaultGlider();

    /** The glider a player would fly with now: the one they chose and still own, or the default. */
    @NotNull
    Optional<GliderDefinition> getGliderFor(@NotNull Player player);

    /** Whether the player is gliding. */
    boolean isGliding(@NotNull Player player);

    /**
     * Opens the player's glider where they are, as the dragon bus does.
     * <p>
     * Spectators, dead players and players in spectator mode cannot glide. A
     * {@link dev.itsharshxd.zentrix.api.events.glider.GliderDeployEvent} listener may refuse.
     * </p>
     *
     * @return whether the player glides now
     */
    boolean deploy(@NotNull Player player);

    /**
     * Ends the player's glide, or their preview, taking the glider off their back.
     *
     * @return whether they were gliding
     */
    boolean stop(@NotNull Player player);

    /**
     * Shows a glider on the player's back where they stand, to them alone, for about five seconds,
     * as the cosmetics menu does. Moving away, teleporting or starting to glide ends it early.
     *
     * @return whether the preview shows
     */
    boolean preview(@NotNull Player player, @NotNull String gliderId);

    /** Ends the player's preview, if one shows. */
    void stopPreview(@NotNull Player player);
}
