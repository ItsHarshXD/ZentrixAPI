package dev.itsharshxd.zentrix.api.events.gamerule;

import dev.itsharshxd.zentrix.api.events.ZentrixEvent;
import dev.itsharshxd.zentrix.api.gamerule.GameRuleMutationResult;
import dev.itsharshxd.zentrix.api.gamerule.GameRuleScope;
import java.util.Optional;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class GameRuleChangeEvent extends ZentrixEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameRuleScope scope;
    private final String ruleName;
    private final String sourceArenaName;
    private final GameRuleMutationResult result;
    public GameRuleChangeEvent(@NotNull GameRuleScope scope, String ruleName,
                               String sourceArenaName, @NotNull GameRuleMutationResult result) {
        this.scope = scope; this.ruleName = ruleName; this.sourceArenaName = sourceArenaName; this.result = result;
    }
    @NotNull public GameRuleScope getScope() { return scope; }
    @NotNull public Optional<String> getRuleName() { return Optional.ofNullable(ruleName); }
    @NotNull public Optional<String> getSourceArenaName() { return Optional.ofNullable(sourceArenaName); }
    @NotNull public GameRuleMutationResult getResult() { return result; }
    @Override @NotNull public HandlerList getHandlers() { return HANDLERS; }
    @NotNull public static HandlerList getHandlerList() { return HANDLERS; }
}
