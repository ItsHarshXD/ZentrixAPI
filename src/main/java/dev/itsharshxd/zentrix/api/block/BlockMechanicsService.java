package dev.itsharshxd.zentrix.api.block;

import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Original-block protection, placed-block/fluid tracking, and automatic decay in Cornucopia and
 * deathmatch regions. All methods must run on the Bukkit main thread.
 *
 * @since 1.6.0
 */
public interface BlockMechanicsService {

    @NotNull Optional<BlockMechanicsScope> resolveScope(@NotNull Location location);
    @NotNull Optional<TrackedBlockState> getState(@NotNull Block block);
    @NotNull TrackedBlockState getState(@NotNull BlockMechanicsScope scope, @NotNull Block block);
    void trackPlayerPlaced(@NotNull BlockMechanicsScope scope, @NotNull Block block);
    void trackPlayerPlaced(@NotNull BlockMechanicsScope scope, @NotNull List<Block> blocks);
    void trackFluidSpread(
            @NotNull BlockMechanicsScope scope,
            @NotNull Block source,
            @NotNull Block destination);
    void forgetPlayerPlaced(@NotNull BlockMechanicsScope scope, @NotNull Block block);
    boolean shouldPreventBreak(@NotNull BlockMechanicsScope scope, @NotNull Block block);
    boolean isProtected(@NotNull Location location);
    void dropPlayerPlaced(
            @NotNull BlockMechanicsScope scope,
            @NotNull Block block,
            @NotNull Player player);
}
