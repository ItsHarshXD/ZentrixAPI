# Configuration, persistence and frozen state

Which API changes are saved, which only last until a restart, and which matches they affect.
Covers API 1.8.0.

## 1. Three kinds of "the configuration"

| Layer | What it is | How to read it |
|---|---|---|
| **On disk** | The YAML files under `plugins/Zentrix/` as they are now. Owner edits that were not reloaded yet are visible here. | `DataService` file helpers, `GameLootService#getDiskConfiguration` |
| **Loaded** | What Zentrix read at startup or the last reload, plus runtime registrations. New lobbies use this. | Domain services: `SettingsService#getValue`, `GameLootService#getActiveConfiguration`, `PhaseService`, `GameRuleService#getEffectiveGlobalRules`, ... |
| **Frozen for a match** | The copy a match captured; later reloads and edits do not change it. | `PhaseService#getRuntimeState`, `GameLootService#getFrozenConfiguration`, `ScenarioInstance` settings, `CustomGameInfo` |

`DataService#getZentrixConfig` and its `getZentrixConfig...` helpers read the file from disk, not
the effective state: a value read there may not be the one Zentrix uses, and writing a Zentrix file
does not make Zentrix use it. Change behavior through the domain services below instead. The
add-on file helpers (`getOrCreateConfig`, `saveConfig`, ...) are your own storage under
`plugins/Zentrix/addons/<addon-id>/`.

## 2. Persistent or runtime-only

| Operation | Lasts | Notes |
|---|---|---|
| `SettingsService#set` | persistent (`settings.yml`) | Validated like the settings menu, then reloaded and applied live. |
| `CosmeticsService` grant / revoke / select | persistent (player storage) | Shared by every server using the same database. |
| `CurrencyService` transactions | persistent (player storage) | |
| `GameRuleService` global and arena mutations | persistent | Asynchronous; see its Javadoc. |
| `RecipeService` register-and-save, `saveRecipe` | persistent (recipe files) | Plain `registerRecipe` keeps the recipe in memory only. |
| `ItemService`, `PhaseService`, `GameTypeService`, `BroadcastService` `...Async` registrations | persistent (their YAML files) | The synchronous `register...` variants are memory-only, and removals affect memory only. |
| Loot pool `save` | persistent (loot files) | `reload` re-reads them. |
| Contribution registrations (hooks, sidebar contributors, item providers, placeholders, locale layers, compass filters) | until unregistered or the owner disables | Never written anywhere. |
| Event changes (cancelling, editing payloads) | that one occurrence | |
| `PhaseService#advancePhase`, `GameLootService#refill`, custom game cancel / terminate | that match | |

Runtime-only registrations disappear on restart; register them again in your `onEnable`.

## 3. What a match freezes, and when

| Content | Frozen at | Effect of a later change |
|---|---|---|
| Phase sequence and phase definitions | when the lobby is created | Running and waiting matches keep their captured phases; `PhaseService#getRuntimeState` reports the captured sequence. Global queries such as `getPhaseCount` describe the current configuration instead. |
| Scenario selection and scenario settings | selection lock, just before the match leaves the waiting lobby | A settings change applies to matches that lock later. |
| Game loot pools (main, Nether, End, deathmatch) | when the match starts | Pool edits and reloads apply to matches that start later. Refills reuse the frozen pools. |
| Cornucopia loot pool | when the Cornucopia is prepared | as above |
| Custom game definition (mode, arena, scenarios, settings, password, start time) | publication | Published games keep what their host chose. Only `SCHEDULED` and `WAITING` games are restored after a restart. |
| `settings.yml` values | not frozen | `SettingsService#set` applies live. Settings read when something is created (a lobby, a deathmatch) affect the next one created. |

## 4. Reloads

- `/zentrix reload` re-reads the files. Matches keep their frozen content (section 3).
- Content that refers to an addon - an item identifier in `content/items.yml` using an addon
  namespace, say - resolves when the file is read. Zentrix reads it at its own startup, before
  addons that depend on it enable, so such entries resolve from the next `/zentrix reload` after
  your provider registered. Identifiers resolved on demand (`ItemService#resolveItem`, class bonus
  items) work as soon as the provider is registered.
- Persistent registrations survive a reload and a restart because they were written to the file.
  Runtime-only ones never survive a restart; whether one survives `/zentrix reload` depends on its
  service, so check that service's Javadoc or register again after a reload.

## 5. Secrets and read-only settings

`SettingsService` exposes the settings menu's allow-list only. Storage credentials, licensing and
any other value the menu does not offer are not readable or writable through it. Generic file
helpers can still read files from disk; do not use them to copy secrets into your own storage or
messages.
