package dev.itsharshxd.zentrix.api.events.revival;

import dev.itsharshxd.zentrix.api.events.ZentrixGameEvent;
import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Arrays;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Fired after an elimination becomes eligible for teammate revival. */
public final class PlayerEliminationRecordedEvent extends ZentrixGameEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String teamId;
    private final ItemStack[] lostLoot;

    public PlayerEliminationRecordedEvent(
            @NotNull ZentrixGame game,
            @NotNull Player player,
            @NotNull String teamId,
            @Nullable ItemStack[] lostLoot) {
        super(game);
        this.player = player;
        this.teamId = teamId;
        this.lostLoot = copy(lostLoot);
    }

    @NotNull public Player getPlayer() { return player; }
    @NotNull public String getTeamId() { return teamId; }
    @Nullable public ItemStack[] getLostLoot() { return copy(lostLoot); }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }

    private static ItemStack[] copy(ItemStack[] items) {
        return items == null ? null : Arrays.stream(items)
                .map(item -> item == null ? null : item.clone())
                .toArray(ItemStack[]::new);
    }
}
