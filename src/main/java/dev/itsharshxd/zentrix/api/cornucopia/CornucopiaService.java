package dev.itsharshxd.zentrix.api.cornucopia;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Schematic validation, podium assignment, cage overrides, and release lifecycle. Placement and
 * player operations must run on the Bukkit main thread.
 *
 * @since 1.6.0
 */
public interface CornucopiaService {

    @NotNull CornucopiaSchematicStatus validateSchematic(
            @NotNull String schematic,
            @NotNull Material podiumBlock);

    /** Starts preparation if the game does not already own preparation state. */
    void prepare(@NotNull ZentrixGame game);

    @NotNull CornucopiaPreparation getPreparation(@NotNull ZentrixGame game);
    boolean shouldUse(@NotNull ZentrixGame game);
    boolean teleportPlayersToPodiums(@NotNull ZentrixGame game);
    @NotNull Map<UUID, Location> getPodiumAssignments(@NotNull ZentrixGame game);

    /**
     * Runtime override of the head-level wall, used for every subsequently created cage until
     * cleared. Cages that already stand are repainted immediately, and clearing the override
     * repaints them back to their design's wall. Per-player overrides always win over the global
     * one.
     *
     * <p>Only cage designs that accept it are repainted: the built-in Classic design does, and
     * server owners choose for every other design ({@code material-override} in
     * {@code content/cosmetics/cage-designs.yml}, or Addon Wall Override in the cage editor). Any
     * other design keeps its own wall, so a carefully built cage is never changed by an addon.
     */
    void setGlobalCageMaterial(@NotNull Material material);
    void clearGlobalCageMaterial();
    @NotNull Optional<Material> getGlobalCageMaterial();

    /**
     * Runtime override of the head-level wall of the cage assigned to one player. Like the global
     * override, only a design that accepts it is repainted.
     */
    void setPlayerCageMaterial(@NotNull UUID playerId, @NotNull Material material);
    void clearPlayerCageMaterial(@NotNull UUID playerId);
    @NotNull Optional<Material> getPlayerCageMaterial(@NotNull UUID playerId);

    /** Starts the configured physical release countdown and invokes the callback on release. */
    boolean startReleaseCountdown(@NotNull ZentrixGame game, @NotNull Runnable onRelease);
    void removeFromReleaseCountdown(@NotNull ZentrixGame game, @NotNull Player player);
}
