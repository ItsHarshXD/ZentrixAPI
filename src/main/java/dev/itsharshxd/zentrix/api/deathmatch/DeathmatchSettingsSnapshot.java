package dev.itsharshxd.zentrix.api.deathmatch;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The deathmatch configuration of {@code settings.yml}, read at one moment. A snapshot whose
 * locations are copies.
 *
 * <p>This is the default deathmatch. A scenario or addon answering
 * {@code GameplayHooks#DEATHMATCH_DESTINATION} - through a scenario or
 * {@code GameplayHookService} - can send a match elsewhere.
 *
 * @param configured                  whether a template world is set, which a deathmatch needs
 * @param templateWorld               the world copied for each deathmatch
 * @param spawnType                   {@code RANDOM} or {@code CUSTOM}
 * @param freezeSeconds               how long players are frozen after arriving
 * @param blockDecayEnabled           whether blocks players place decay
 * @param blockDecayStartDelaySeconds the delay before a placed block starts decaying
 * @param blockDecayDurationSeconds   how long a placed block takes to decay
 * @param protectOriginalBlocks       whether blocks of the arena itself cannot be broken
 * @param customSpawns                the configured spawns, used with {@code CUSTOM}
 * @param borderPos1                  one corner of the configured bounds, or null
 * @param borderPos2                  the other corner of the configured bounds, or null
 * @since 1.8.0
 */
public record DeathmatchSettingsSnapshot(
        boolean configured,
        @NotNull String templateWorld,
        @NotNull String spawnType,
        int freezeSeconds,
        boolean blockDecayEnabled,
        double blockDecayStartDelaySeconds,
        double blockDecayDurationSeconds,
        boolean protectOriginalBlocks,
        @NotNull List<Location> customSpawns,
        @Nullable Location borderPos1,
        @Nullable Location borderPos2) {

    public DeathmatchSettingsSnapshot {
        Objects.requireNonNull(templateWorld, "templateWorld");
        Objects.requireNonNull(spawnType, "spawnType");
        customSpawns = customSpawns.stream().map(Location::clone).toList();
        borderPos1 = borderPos1 == null ? null : borderPos1.clone();
        borderPos2 = borderPos2 == null ? null : borderPos2.clone();
    }

    /** Copies of the configured spawns. */
    @Override
    @NotNull
    public List<Location> customSpawns() {
        return customSpawns.stream().map(Location::clone).toList();
    }

    /** The configured bounds' corners, when both are set. */
    @NotNull
    public Optional<Location[]> getBounds() {
        return borderPos1 == null || borderPos2 == null
                ? Optional.empty()
                : Optional.of(new Location[] {borderPos1.clone(), borderPos2.clone()});
    }

    @Override
    @Nullable
    public Location borderPos1() {
        return borderPos1 == null ? null : borderPos1.clone();
    }

    @Override
    @Nullable
    public Location borderPos2() {
        return borderPos2 == null ? null : borderPos2.clone();
    }
}
