package dev.itsharshxd.zentrix.api.gui.background;

import java.time.ZonedDateTime;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * What an add-on condition, value or resolver is asked about: one viewer, the menu they are
 * opening or looking at, and the moment the menu is being decided.
 *
 * <p>A context is only valid during the call it is passed to, which always runs on the main
 * thread. Keep the work quick: it runs every time the menu opens or redraws.
 *
 * @since 1.15.0
 */
public interface MenuConditionContext {

    /** The player looking at the menu. */
    @NotNull
    Player getPlayer();

    /**
     * What the menu shows right now. Its {@link MenuView#background() background} is null, because
     * the background is what is being decided.
     */
    @NotNull
    MenuView getMenu();

    /** The menu's ID in {@code guis.yml}; the same as {@code getMenu().menuId()}. */
    @NotNull
    default String getMenuId() {
        return getMenu().menuId();
    }

    /**
     * The moment the menu is being decided, in the time zone of {@code backgrounds.timezone} in
     * {@code guis.yml} (the server's own when unset). It is the same for every condition of one
     * decision.
     */
    @NotNull
    ZonedDateTime getNow();
}
