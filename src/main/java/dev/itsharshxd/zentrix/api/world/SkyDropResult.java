package dev.itsharshxd.zentrix.api.world;

/**
 * What became of an attempt to put a player back into the sky.
 *
 * @since 1.10.0
 */
public enum SkyDropResult {

    /** The player was placed and their glider was deployed. */
    SUCCESS,

    /**
     * The player was placed, but no glider was deployed.
     *
     * <p>Either none was asked for, or Matrix Gliders is not installed. The drop itself stands
     * either way, so a caller only has to react to this if the fall matters to it.
     */
    PLACED_WITHOUT_GLIDER,

    /** Nothing happened: the player was offline, or the teleport was refused. */
    FAILED;

    /** Whether the player ended up at the drop point. */
    public boolean isPlaced() {
        return this != FAILED;
    }
}
