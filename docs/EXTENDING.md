# Extending Zentrix: ownership, composition and coexistence

This guide explains how addon contributions are owned, ordered and cleaned up, so several addons
can customize the same Zentrix feature without depending on load order or on Zentrix internals.
It covers API 1.8.0. For threading and result semantics see [THREADING.md](THREADING.md).

## 1. Ways to customize a feature

Zentrix offers four kinds of extension point. Each service documents which one it uses.

| Kind | What it does | Examples |
|---|---|---|
| **Observe** | A notification after something happened. It cannot be cancelled. | `SpectatorStartEvent`, `CosmeticOwnershipChangeEvent`, `LootRefillEvent`, `CustomGameStateChangeEvent`, `SettingChangeEvent`, `IdentityUnmaskEvent` |
| **Intercept** | A Bukkit event fired *before* Zentrix applies a decision. Cancelling it or changing its documented mutable fields changes the outcome. | `ChatMessageEvent`, `ClassAbilityProcEvent`, `ClassKitGrantEvent`, `LootContainerFillEvent`, `RevivalItemConsumeEvent`, `GliderDeployEvent`, `VictoryCelebrationEvent`, `IdentityMaskEvent`, `CosmeticSelectEvent`, `SpectatorCameraEvent` |
| **Contribute** | An owned registration that Zentrix consults every time a decision is made. | `GameplayHookService`, `ScoreboardService` sidebar contributors, `ItemService#registerItemProvider`, `PlaceholderService`, `LocaleService#registerMessages`, `CompassTrackerService#registerTargetFilter` |
| **Operate** | A service method that performs the same operation a command or menu performs, with a result. | `CustomGameService#join`, `PhaseService#advancePhase`, `SettingsService#set`, `GameLootService#refill`, `RevivalService#checkRevival`, `CosmeticsService#grant` |

Prefer the narrowest kind that does the job: observe when you only react, intercept when you need
to change one occurrence, contribute when you supply behavior for every occurrence.

## 2. Ownership and automatic release

Contribution registrations take the owning `Plugin` and return a
`dev.itsharshxd.zentrix.api.addon.Registration`:

- `owner()` is the plugin the registration belongs to. It must be enabled when registering.
- `isActive()` reports whether it is still in effect.
- `unregister()` ends it. Calling it twice is harmless.
- A registration ends on its own when its owner disables, even if the addon never unregisters, so
  no code of a disabled plugin is called afterwards and no stale class loader is kept.

Scenario registrations (`ScenarioService#register`) and identity masks (`IdentityHandle`) follow the
same rule with their own handle types.

Older registrations identified by an addon ID string - dynamic items, phases, game types,
broadcasts and recipes registered through their builders - are not owner-scoped. Remove them in
your `onDisable` if they must not outlive your addon.

### Namespaces

Identifiers you contribute live in your own namespace:

| Registry | Form | Reserved |
|---|---|---|
| Placeholders | `%zentrix_<namespace>:<key>%` | `zentrix` |
| Item providers | `<namespace>:<id>` | `custom`, `itemsadder`, `nexo`, `minecraft`, `zentrix` |
| Locale messages | keys you register under your plugin | built-in keys can only be overridden through `registerOverrides` |

A namespace belongs to one active registration at a time; registering a taken namespace throws
`IllegalArgumentException` naming the current owner. Pick a namespace derived from your plugin name.

## 3. Ordering and conflicts

Every contribution point declares how several contributions combine.

| Extension point | Composition |
|---|---|
| Bukkit events | Bukkit priorities. Zentrix reads cancellation and mutable fields after every listener ran, so a `MONITOR` listener sees the final values and must not change them. |
| `GameplayHookService` | Active **scenarios of the match first**, in their usual descending-priority-then-ID order. Then addon handlers, higher `priority` first, equal priorities in registration order. The first answer that does not pass decides; later handlers are not asked. |
| `ScoreboardService` contributors | Higher `priority` first, equal priorities in registration order. Each contributor sees the previous one's result (a pipeline). |
| Item providers | Built-in prefixes (`custom:`, `itemsadder:`, `nexo:`) always win; then the provider owning the namespace; then vanilla materials. |
| Compass target filters | All filters must accept a target. |
| Locale overrides | Layered over the active locale file by `priority`; removing a layer restores the next value without rewriting the file. |

Rules of thumb:

- Never rely on plugin load order. Use priorities where a point offers them.
- Return `HookOutcome.pass()` when you have no opinion, so other addons still get asked.
- A contributor that throws is logged under its owner and treated as having passed or changed
  nothing; the sidebar pipeline discards a failed contributor's partial changes.

## 4. Built-in defaults stay authoritative

Every extension point is opt-in. With no listener and no registration, Zentrix behaves exactly as it
does without addons, and most points cost a single check - events are only built while a listener
is registered. Defaults you change through an event apply to that one occurrence only; nothing
is persisted unless the operation says so (`SettingsService#set` and `CosmeticsService` changes are
persistent; events and contributions are not).

Some things stay with Zentrix whatever an addon does:

- Spectators stay isolated from gameplay. Chat recipients and broadcast audiences can be narrowed,
  never widened; spectator cameras only view living players of the watched match.
- Forced cleanup cannot be vetoed: leaving spectator mode, unmasking at match end, world cleanup
  and shutdown are notifications.
- Accounting stays single-sourced. Reward and statistic events change amounts or withhold them;
  they never let an addon credit the same outcome twice.
- Edition limits and licensing are not policy hooks. Check `PlatformService#getCapability` before
  relying on a Premium feature; Free-edition services report themselves unavailable.

## 5. Scenario versus ordinary addon

Write a **scenario** (`ScenarioService`) when the mechanic is something players vote on or a match
selects: it gets per-match settings, a store, owned tasks/listeners and failure isolation that
disables it for one match after repeated errors.

Write an **ordinary addon** with `GameplayHookService` and events when the mechanic applies to every
match, such as server-wide rules or integrations. It runs after the match's scenarios, so selected
scenarios keep deciding first.

## 6. Supported surface

Only `dev.itsharshxd.zentrix.api` is supported. Classes under `dev.itsharshxd.plugin.zentrix`, and
relocated libraries shaded into the Zentrix jar, are internal: they change without notice, may be
obfuscated in releases, and bypass the validation, ownership and cleanup described here.
