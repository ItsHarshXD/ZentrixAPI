package dev.itsharshxd.zentrix.api.world;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * Where a player should be put back into the sky above a world.
 *
 * <p>A request describes an area rather than a point: an anchor to stay near, how far from it a
 * point may be chosen, how high above the ground the player should appear, and how much free air
 * has to be underneath them. {@link SkyDropService} turns that into an actual location, or into
 * nothing at all when the area cannot offer one.
 *
 * <p>Instances are immutable and the anchor is copied on the way in and out, so a request can be
 * held on to and reused across matches without a later teleport changing what it meant.
 *
 * @param anchor              the point to stay near; its world is the world the drop happens in
 * @param horizontalRadius    how far from the anchor a point may be chosen, in blocks; {@code 0}
 *                            keeps the drop directly above the anchor
 * @param altitude            how far above the ground the player should appear, in blocks
 * @param clearance           how many free blocks have to sit under the drop point, which is what
 *                            stops a player being handed a glide with nowhere to glide
 * @param attempts            how many candidate points to try before giving up
 * @param requireInsideBorder whether the point has to lie inside the world's current border
 * @since 1.10.0
 */
public record SkyDropRequest(
        @NotNull Location anchor,
        double horizontalRadius,
        int altitude,
        int clearance,
        int attempts,
        boolean requireInsideBorder) {

    public SkyDropRequest {
        if (anchor == null || anchor.getWorld() == null) {
            throw new IllegalArgumentException("A sky drop needs an anchor with a world");
        }
        anchor = anchor.clone();
        horizontalRadius = Math.max(0.0D, horizontalRadius);
        altitude = Math.max(1, altitude);
        clearance = Math.max(1, clearance);
        attempts = Math.max(1, attempts);
    }

    @Override
    public Location anchor() {
        return anchor.clone();
    }

    /**
     * A drop directly above one point, high enough to glide from.
     *
     * <p>The defaults suit a player re-entering a match from the air; narrow them with the
     * {@code with*} methods rather than calling the canonical constructor.
     */
    @NotNull
    public static SkyDropRequest above(@NotNull Location anchor) {
        return new SkyDropRequest(anchor, 0.0D, 60, 24, 8, true);
    }

    /** How far from the anchor a point may be chosen. */
    @NotNull
    public SkyDropRequest withinRadius(double radius) {
        return new SkyDropRequest(
                anchor, radius, altitude, clearance, attempts, requireInsideBorder);
    }

    /** How far above the ground the player appears. */
    @NotNull
    public SkyDropRequest atAltitude(int blocks) {
        return new SkyDropRequest(
                anchor, horizontalRadius, blocks, clearance, attempts, requireInsideBorder);
    }

    /** How much free air the drop point needs beneath it. */
    @NotNull
    public SkyDropRequest withClearance(int blocks) {
        return new SkyDropRequest(
                anchor, horizontalRadius, altitude, blocks, attempts, requireInsideBorder);
    }

    /** How many candidate points to try before giving up. */
    @NotNull
    public SkyDropRequest withAttempts(int value) {
        return new SkyDropRequest(
                anchor, horizontalRadius, altitude, clearance, value, requireInsideBorder);
    }

    /** Whether the point has to lie inside the world's current border. */
    @NotNull
    public SkyDropRequest insideBorder(boolean require) {
        return new SkyDropRequest(
                anchor, horizontalRadius, altitude, clearance, attempts, require);
    }
}
