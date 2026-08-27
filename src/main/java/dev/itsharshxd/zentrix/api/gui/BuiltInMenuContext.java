package dev.itsharshxd.zentrix.api.gui;

import dev.itsharshxd.zentrix.api.gamerule.GameRuleScope;
import dev.itsharshxd.zentrix.api.loot.LootPool;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** Typed context values used by built-in detail/editor menus. */
public final class BuiltInMenuContext {
    public static final String PAGE = "page";
    public static final String TARGET_PLAYER = "target-player";
    public static final String RECIPE_ID = "recipe-id";
    public static final String ARENA_ID = "arena-id";
    public static final String SECTION_ID = "section-id";
    public static final String GAME_RULE_SCOPE = "game-rule-scope";
    public static final String LOOT_POOL = "loot-pool";
    public static final String LOOT_ENTRY_ID = "loot-entry-id";
    public static final String ACCESS_CATEGORY = "access-category";

    private final Map<String, Object> values;

    private BuiltInMenuContext(Map<String, Object> values) {
        this.values = Map.copyOf(values);
    }

    @NotNull public static BuiltInMenuContext empty() {
        return new BuiltInMenuContext(Map.of());
    }
    @NotNull public static Builder builder() { return new Builder(); }
    @NotNull public Optional<Object> value(@NotNull String key) {
        return Optional.ofNullable(values.get(key));
    }
    @NotNull public <T> Optional<T> value(@NotNull String key, @NotNull Class<T> type) {
        Object value = values.get(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }
    public int page() { return value(PAGE, Number.class).map(Number::intValue).orElse(0); }
    @NotNull public Optional<Player> targetPlayer() { return value(TARGET_PLAYER, Player.class); }
    @NotNull public Optional<String> recipeId() { return value(RECIPE_ID, String.class); }
    @NotNull public Optional<String> arenaId() { return value(ARENA_ID, String.class); }
    @NotNull public Optional<String> sectionId() { return value(SECTION_ID, String.class); }
    @NotNull public Optional<GameRuleScope> gameRuleScope() {
        return value(GAME_RULE_SCOPE, GameRuleScope.class);
    }
    @NotNull public Optional<LootPool> lootPool() { return value(LOOT_POOL, LootPool.class); }
    @NotNull public Optional<String> lootEntryId() { return value(LOOT_ENTRY_ID, String.class); }
    @NotNull public Map<String, Object> values() { return values; }

    public static final class Builder {
        private final Map<String, Object> values = new LinkedHashMap<>();
        @NotNull public Builder value(@NotNull String key, @NotNull Object value) {
            values.put(key, value); return this;
        }
        @NotNull public Builder page(int page) { return value(PAGE, Math.max(0, page)); }
        @NotNull public Builder targetPlayer(@NotNull Player player) { return value(TARGET_PLAYER, player); }
        @NotNull public Builder recipeId(@NotNull String id) { return value(RECIPE_ID, id); }
        @NotNull public Builder arenaId(@NotNull String id) { return value(ARENA_ID, id); }
        @NotNull public Builder sectionId(@NotNull String id) { return value(SECTION_ID, id); }
        @NotNull public Builder gameRuleScope(@NotNull GameRuleScope scope) {
            return value(GAME_RULE_SCOPE, scope);
        }
        @NotNull public Builder lootPool(@NotNull LootPool pool) { return value(LOOT_POOL, pool); }
        @NotNull public Builder lootEntryId(@NotNull String id) { return value(LOOT_ENTRY_ID, id); }
        @NotNull public BuiltInMenuContext build() { return new BuiltInMenuContext(values); }
    }
}
