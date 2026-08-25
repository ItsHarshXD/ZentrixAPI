package dev.itsharshxd.zentrix.api.gui;

import java.util.Set;

/** Stable IDs of every built-in Zentrix menu. */
public final class BuiltInMenuIds {
    private BuiltInMenuIds() {}

    public static final String PROFILE = "profile";
    public static final String RECIPE_BROWSER = "recipe-browser";
    public static final String RECIPE_PREVIEW = "recipe-preview";
    public static final String SPECTATOR_TELEPORT = "spectator-teleport";
    public static final String TEAM_CHANGER = "team-changer";
    public static final String CLASS_SELECTION = "class-selection";
    public static final String REVIVAL = "revival";
    public static final String MANAGEMENT_MAIN = "management.main";
    public static final String MANAGEMENT_GENERAL_SETTINGS = "management.general-settings";
    public static final String MANAGEMENT_SETTINGS_SECTION = "management.settings-section";
    public static final String MANAGEMENT_ARENA_LIST = "management.arena-list";
    public static final String MANAGEMENT_ARENA_CREATE = "management.arena-create";
    public static final String MANAGEMENT_ARENA_DETAILS = "management.arena-details";
    public static final String MANAGEMENT_ARENA_DELETE = "management.arena-delete";
    public static final String MANAGEMENT_ARENA_GAME_RULES = "management.arena-game-rules";
    public static final String MANAGEMENT_CONFIGURED_GAME_RULES = "management.configured-game-rules";
    public static final String MANAGEMENT_CUSTOM_GAME_RULES = "management.custom-game-rules";
    public static final String MANAGEMENT_DEATHMATCH_SPAWNS = "management.deathmatch-spawns";
    public static final String RECIPES_MENU = "recipes.menu";
    public static final String RECIPES_MANAGE = "recipes.manage";
    public static final String RECIPES_CREATE = "recipes.create";
    public static final String RECIPES_EDIT = "recipes.edit";
    public static final String RECIPES_ACCESS = "recipes.access";
    public static final String RECIPES_ACCESS_LIST = "recipes.access-list";
    public static final String RECIPES_DELETE = "recipes.delete";
    public static final String LOOT_MENU = "loot.menu";
    public static final String LOOT_GAME_CATEGORIES = "loot.game-categories";
    public static final String LOOT_POOL = "loot.pool";
    public static final String LOOT_ENTRY_EDITOR = "loot.entry-editor";
    public static final String LOOT_ITEM_PICKER = "loot.item-picker";
    public static final String LOOT_CONTAINER_ELIGIBILITY = "loot.container-eligibility";
    public static final String LOOT_DELETE = "loot.delete";
    public static final String LOOT_PREVIEW = "loot.preview";
    public static final String LOOT_TABLE_IMPORT = "loot.table-import";

    private static final Set<String> ALL = Set.of(
            PROFILE, RECIPE_BROWSER, RECIPE_PREVIEW, SPECTATOR_TELEPORT, TEAM_CHANGER,
            CLASS_SELECTION, REVIVAL, MANAGEMENT_MAIN, MANAGEMENT_GENERAL_SETTINGS,
            MANAGEMENT_SETTINGS_SECTION, MANAGEMENT_ARENA_LIST, MANAGEMENT_ARENA_CREATE,
            MANAGEMENT_ARENA_DETAILS, MANAGEMENT_ARENA_DELETE, MANAGEMENT_ARENA_GAME_RULES,
            MANAGEMENT_CONFIGURED_GAME_RULES, MANAGEMENT_CUSTOM_GAME_RULES,
            MANAGEMENT_DEATHMATCH_SPAWNS, RECIPES_MENU, RECIPES_MANAGE, RECIPES_CREATE,
            RECIPES_EDIT, RECIPES_ACCESS, RECIPES_ACCESS_LIST, RECIPES_DELETE, LOOT_MENU,
            LOOT_GAME_CATEGORIES, LOOT_POOL, LOOT_ENTRY_EDITOR, LOOT_ITEM_PICKER,
            LOOT_CONTAINER_ELIGIBILITY, LOOT_DELETE, LOOT_PREVIEW, LOOT_TABLE_IMPORT);

    public static Set<String> all() { return ALL; }
}
