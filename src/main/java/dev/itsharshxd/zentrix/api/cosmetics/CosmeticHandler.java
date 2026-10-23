package dev.itsharshxd.zentrix.api.cosmetics;

import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What an addon implements for a cosmetic type it registers with
 * {@link CosmeticsService#registerType}: the cosmetics there are, and what they do.
 *
 * <p>Zentrix does the rest as it does for its own categories: it stores who owns and chose what,
 * lists the type in {@code /cosmetics}, checks permissions, sells cosmetics that have a price, and
 * calls this handler when one of them should show.
 *
 * <p>Every method is called on the main thread. One that throws is logged and skipped, so a
 * failing handler cannot break a match or a menu.
 *
 * <pre>{@code
 * public final class HatHandler implements CosmeticHandler {
 *     public List<CosmeticDefinition> getCosmetics() {
 *         return hats.values().stream().map(Hat::definition).toList();
 *     }
 *
 *     public void onTrigger(CosmeticTrigger trigger) {
 *         switch (trigger.moment()) {
 *             case GAME_START -> wear(trigger.player(), trigger.cosmetic().getId());
 *             case GAME_LEAVE -> takeOff(trigger.player());
 *             default -> { }
 *         }
 *     }
 *
 *     public boolean startPreview(Player player, CosmeticDefinition cosmetic, Runnable onFinish) {
 *         wear(player, cosmetic.getId());
 *         Bukkit.getScheduler().runTaskLater(plugin, () -> {
 *             takeOff(player);
 *             onFinish.run();
 *         }, 100L);
 *         return true;
 *     }
 * }
 * }</pre>
 *
 * @since 1.11.0
 */
public interface CosmeticHandler {

    /**
     * Every cosmetic of the type, in the order menus list them. Asked whenever Zentrix needs them,
     * so a reload of the addon's own configuration shows at once; keep it quick.
     *
     * <p>A cosmetic removed from the list stays owned by those who own it, and counts again once
     * it is listed again. IDs must be unique; a repeated one is ignored.
     */
    @NotNull
    List<CosmeticDefinition> getCosmetics();

    /**
     * A player's equipped cosmetic of this type, triggered at one of the moments the type's
     * definition lists. A {@link dev.itsharshxd.zentrix.api.events.cosmetics.CosmeticTriggerEvent}
     * listener may have skipped it.
     */
    default void onTrigger(@NotNull CosmeticTrigger trigger) {
    }

    /**
     * What an online player has equipped of this type changed: they chose another, it was taken
     * away or expired, or their cosmetics were read as they joined. For a cosmetic that shows
     * outside matches, such as in the lobby. Not called as players leave the server.
     *
     * @param equipped the cosmetic now in effect, or null when there is none
     */
    default void onEquippedChange(@NotNull Player player, @Nullable CosmeticDefinition equipped) {
    }

    /**
     * Shows a cosmetic to a player for a while, as the cosmetics menu's right-click does.
     * Zentrix closes the menu first and opens it again once {@code onFinish} runs.
     *
     * @param onFinish to run once the preview has finished on its own; not after
     *                 {@link #stopPreview}
     * @return whether it shows; false, the default, when this type has no previews
     */
    default boolean startPreview(@NotNull Player player, @NotNull CosmeticDefinition cosmetic,
                                 @NotNull Runnable onFinish) {
        return false;
    }

    /**
     * Ends a player's preview early: they started another, left, or an addon asked. Called only
     * while a preview this handler started has not finished.
     */
    default void stopPreview(@NotNull Player player) {
    }

    /**
     * The type was unregistered, by its owner or because the owning plugin is disabling. Previews
     * still showing have been stopped; anything else the type keeps up, such as worn items,
     * should be undone here.
     */
    default void onUnregister() {
    }
}
