package dev.itsharshxd.zentrix.api.deathmatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Where a match's deathmatch happens, when it is not the copied template arena.
 *
 * <p>Zentrix normally copies {@code deathmatch_template} into a fresh world for every deathmatch.
 * A scenario that wants the final fight somewhere else — the match's own End, its Nether, an arena
 * it built itself — answers the deathmatch-destination hook with one of these, and Zentrix runs its
 * entire ordinary deathmatch in that world instead: the teleport, the safe spawns, the freeze
 * countdown, the titles, the spectator transfer, the currency rewards, the border and its shrinkage,
 * block protection and decay, the game rules and the deathmatch loot pool.
 *
 * <p>The one thing that changes is ownership. A world Zentrix did not create is not Zentrix's to
 * delete, so {@link #manageWorldLifecycle()} defaults to false and deathmatch cleanup leaves the
 * world to whoever does own it.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * context.override(GameplayHooks.DEATHMATCH_DESTINATION, request -> {
 *     World end = context.world(GameWorldType.END).orElse(null);
 *     if (end == null) {
 *         return HookOutcome.pass();  // let Zentrix copy its template as usual
 *     }
 *     return HookOutcome.replace(DeathmatchDestination.builder(end)
 *             .bounds(remap(request.borderCorner1()), remap(request.borderCorner2()))
 *             .build());
 * });
 * }</pre>
 *
 * @since 1.9.0
 */
public final class DeathmatchDestination {

    private final World world;
    private final List<Location> spawns;
    private final Location borderCorner1;
    private final Location borderCorner2;
    private final boolean manageWorldLifecycle;
    private final boolean applyWorldPreparation;
    private final boolean applyBlockMechanics;

    private DeathmatchDestination(Builder builder) {
        this.world = builder.world;
        this.spawns = List.copyOf(builder.spawns);
        this.borderCorner1 = builder.borderCorner1;
        this.borderCorner2 = builder.borderCorner2;
        this.manageWorldLifecycle = builder.manageWorldLifecycle;
        this.applyWorldPreparation = builder.applyWorldPreparation;
        this.applyBlockMechanics = builder.applyBlockMechanics;
    }

    /** The world the deathmatch runs in. */
    @NotNull
    public World world() {
        return world;
    }

    /**
     * The exact places players are put, cycled through in order.
     *
     * <p>Empty hands the choice back to Zentrix, which then resolves spawns inside the declared
     * bounds exactly as it does for a template arena — including its safe-location fallback for a
     * position that turns out to be unusable.
     */
    @NotNull
    public List<Location> spawns() {
        return spawns;
    }

    /** One corner of the deathmatch border, empty to keep the configured one. */
    @NotNull
    public Optional<Location> borderCorner1() {
        return Optional.ofNullable(borderCorner1);
    }

    /** The opposite corner of the deathmatch border, empty to keep the configured one. */
    @NotNull
    public Optional<Location> borderCorner2() {
        return Optional.ofNullable(borderCorner2);
    }

    /** Whether both corners were supplied, which is what makes the remapped border usable. */
    public boolean hasBounds() {
        return borderCorner1 != null && borderCorner2 != null;
    }

    /**
     * Whether Zentrix owns this world and may unload and delete it when the deathmatch is cleaned
     * up. False for a world that belongs to something else — the match's End, for instance.
     */
    public boolean manageWorldLifecycle() {
        return manageWorldLifecycle;
    }

    /**
     * Whether Zentrix resets this world's weather, clock and game rules the way it prepares a fresh
     * deathmatch arena.
     *
     * <p>On by default, because a deathmatch is expected to start clean and because scenarios that
     * pin a world property are told about the preparation and re-apply themselves afterwards.
     */
    public boolean applyWorldPreparation() {
        return applyWorldPreparation;
    }

    /**
     * Whether deathmatch block protection and block decay are active in this world.
     *
     * <p>On by default; switching it off leaves the world's blocks under whatever rules already
     * governed them.
     */
    public boolean applyBlockMechanics() {
        return applyBlockMechanics;
    }

    @NotNull
    public static Builder builder(@NotNull World world) {
        return new Builder(world);
    }

    @Override
    public String toString() {
        return "DeathmatchDestination[" + world.getName() + ", spawns=" + spawns.size()
                + ", bounds=" + hasBounds() + "]";
    }

    /** Fluent builder for {@link DeathmatchDestination}. */
    public static final class Builder {

        private final World world;
        private final List<Location> spawns = new ArrayList<>();
        private Location borderCorner1;
        private Location borderCorner2;
        private boolean manageWorldLifecycle;
        private boolean applyWorldPreparation = true;
        private boolean applyBlockMechanics = true;

        private Builder(World world) {
            if (world == null) {
                throw new IllegalArgumentException("A deathmatch destination needs a world");
            }
            this.world = world;
        }

        /** Adds one spawn. Locations in another world are refused rather than silently ignored. */
        @NotNull
        public Builder spawn(@NotNull Location location) {
            if (location.getWorld() != null && !world.equals(location.getWorld())) {
                throw new IllegalArgumentException(
                        "Deathmatch spawn is in " + location.getWorld().getName()
                                + " rather than in the destination world " + world.getName());
            }
            Location copy = location.clone();
            copy.setWorld(world);
            spawns.add(copy);
            return this;
        }

        @NotNull
        public Builder spawns(@NotNull Collection<Location> locations) {
            locations.forEach(this::spawn);
            return this;
        }

        /** The two opposite corners of the deathmatch border in this world. */
        @NotNull
        public Builder bounds(@Nullable Location corner1, @Nullable Location corner2) {
            this.borderCorner1 = copyInto(corner1);
            this.borderCorner2 = copyInto(corner2);
            return this;
        }

        @NotNull
        public Builder manageWorldLifecycle(boolean value) {
            this.manageWorldLifecycle = value;
            return this;
        }

        @NotNull
        public Builder applyWorldPreparation(boolean value) {
            this.applyWorldPreparation = value;
            return this;
        }

        @NotNull
        public Builder applyBlockMechanics(boolean value) {
            this.applyBlockMechanics = value;
            return this;
        }

        @NotNull
        public DeathmatchDestination build() {
            if ((borderCorner1 == null) != (borderCorner2 == null)) {
                throw new IllegalArgumentException(
                        "A deathmatch destination needs both border corners or neither");
            }
            return new DeathmatchDestination(this);
        }

        private Location copyInto(Location location) {
            if (location == null) {
                return null;
            }
            Location copy = location.clone();
            copy.setWorld(world);
            return copy;
        }
    }
}
