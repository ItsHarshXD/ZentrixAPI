package dev.itsharshxd.zentrix.api.revival;

import org.jetbrains.annotations.NotNull;

/**
 * What a revival actually handed back to a player.
 *
 * <p>Both options are off unless {@code arena-management.revival} switches them on, so
 * {@link #NONE} is the ordinary answer on a default configuration.
 *
 * @param classItemsRestored whether the player's selected class kit was granted again
 * @param lostLootRestored   whether the inventory they died with was put back
 * @since 1.6.0
 */
public record RevivalRestoration(boolean classItemsRestored, boolean lostLootRestored) {

    /** Nothing was handed back. */
    public static final RevivalRestoration NONE = new RevivalRestoration(false, false);

    /** Whether the player got anything at all. */
    public boolean restoredAnything() {
        return classItemsRestored || lostLootRestored;
    }

    @Override
    @NotNull
    public String toString() {
        return "RevivalRestoration[classItems=" + classItemsRestored
                + ", lostLoot=" + lostLootRestored + "]";
    }
}
