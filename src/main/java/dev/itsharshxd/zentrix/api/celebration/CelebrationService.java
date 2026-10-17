package dev.itsharshxd.zentrix.api.celebration;

import java.util.List;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Victory celebrations: the catalog winners choose from and the previews of the cosmetics menu.
 *
 * <p>When a match is won, each winner still in it celebrates with the celebration they chose and
 * own - see {@link dev.itsharshxd.zentrix.api.cosmetics.CosmeticsService} - or the server's
 * default. {@link dev.itsharshxd.zentrix.api.events.celebration.VictoryCelebrationEvent} lets an
 * addon change or suppress those choices before playback starts. A celebration that fails never
 * delays the match's restart.
 *
 * <p>Celebrations are not part of the Free edition: there {@link #isAvailable()} is {@code false},
 * the catalog is empty and previews fail. Methods that change something must be called on the main
 * thread.
 *
 * @since 1.8.0
 */
public interface CelebrationService {

    /** Whether celebrations exist on this server. */
    boolean isAvailable();

    /** Every celebration, in the server's order. */
    @NotNull
    List<CelebrationDefinition> getCelebrations();

    /** The celebration with this ID. */
    @NotNull
    Optional<CelebrationDefinition> getCelebration(@NotNull String id);

    /** The celebration winners play when they chose none. */
    @NotNull
    Optional<CelebrationDefinition> getDefaultCelebration();

    /**
     * Plays a celebration for one player only, wherever they are, as the cosmetics menu previews
     * it; nobody else sees or hears it.
     *
     * @return whether anything plays
     */
    boolean preview(@NotNull Player player, @NotNull String celebrationId);

    /** Ends the player's preview, if one plays. */
    void stopPreview(@NotNull Player player);
}
