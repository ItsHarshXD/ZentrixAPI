package dev.itsharshxd.zentrix.api.gamerule;

import dev.itsharshxd.zentrix.api.game.ZentrixGame;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

/**
 * Typed game-rule resolution and serialized persistence operations. Effective
 * values follow defaults, then custom globals, then source-arena overrides.
 * Mutation futures perform persistence asynchronously; successful change
 * events are fired synchronously on the Bukkit main thread.
 *
 * @since 1.3.0
 */
public interface GameRuleService {
    @NotNull Collection<GameRuleDefinition> getSupportedDefinitions();
    @NotNull Optional<GameRuleDefinition> getDefinition(@NotNull String ruleName);
    @NotNull Optional<Object> parseValue(@NotNull GameRuleDefinition definition, @NotNull String value);
    @NotNull Collection<ResolvedGameRule> getEffectiveGlobalRules(@NotNull GameRuleScope scope);
    @NotNull Collection<ResolvedGameRule> getEffectiveRules(
        @NotNull GameRuleScope scope, @NotNull String sourceArenaName);
    @NotNull Optional<ResolvedGameRule> resolveGlobal(
        @NotNull GameRuleScope scope, @NotNull String ruleName);
    @NotNull Optional<ResolvedGameRule> resolve(
        @NotNull GameRuleScope scope, @NotNull String sourceArenaName, @NotNull String ruleName);
    @NotNull CompletableFuture<GameRuleMutationResult> setGlobal(
        @NotNull GameRuleScope scope, @NotNull String ruleName, @NotNull Object value);
    @NotNull CompletableFuture<GameRuleMutationResult> removeGlobal(
        @NotNull GameRuleScope scope, @NotNull String ruleName);
    @NotNull CompletableFuture<GameRuleMutationResult> resetGlobal(@NotNull GameRuleScope scope);
    @NotNull CompletableFuture<GameRuleMutationResult> setArenaOverride(
        @NotNull String sourceArenaName, @NotNull GameRuleScope scope,
        @NotNull String ruleName, @NotNull Object value);
    @NotNull CompletableFuture<GameRuleMutationResult> removeArenaOverride(
        @NotNull String sourceArenaName, @NotNull GameRuleScope scope, @NotNull String ruleName);
    @NotNull CompletableFuture<GameRuleMutationResult> resetArenaOverrides(
        @NotNull String sourceArenaName, @NotNull GameRuleScope scope);
    @NotNull CompletableFuture<GameRuleMutationResult> applyToRuntime(
        @NotNull ZentrixGame game, @NotNull GameRuleScope scope);
}
