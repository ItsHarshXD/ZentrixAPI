# Threads, results and safety

What you may call from where, what a result means, and how long returned data stays valid. Covers
API 1.8.0. Each service's Javadoc is authoritative where it is more specific.

## 1. Threads

Zentrix runs its game logic on the Bukkit main thread, and so must anything that changes a match,
a player or a world.

- **Changing state requires the main thread.** Methods that move players, change a match, a
  world, an inventory or a setting check this and throw `IllegalStateException` when called from
  another thread. Schedule onto the main thread with `Bukkit.getScheduler().runTask(...)`.
- **Futures say where they complete.** `CosmeticsService` and `SettingsService#set` do their storage
  work off the main thread and complete on it. `CurrencyService` transactions and `ProfileService`
  reads may complete on any thread: hop back with the scheduler before touching Bukkit state in a
  callback. Never block the main thread waiting for a future (`join()` / `get()`): many of them are
  completed on the main thread, so waiting there deadlocks the server.
- **Events fire on the main thread.** Every Zentrix event is synchronous except where its Javadoc
  says otherwise; currency events raised by asynchronous storage work are queued to the main thread.
  Paper's `AsyncChatEvent` is the only chat event fired off the main thread, and Zentrix only
  narrows its viewers - `ChatMessageEvent` is main-thread.
- **Contributions are called on the thread of the decision**, which is the main thread for every
  built-in gameplay hook, sidebar update, item resolution and placeholder used in menus. A
  PlaceholderAPI request from another plugin may resolve placeholders on its own thread; keep
  `PlaceholderResolver`s thread-safe and free of world access.
- **Queries** that return snapshots (records such as `CustomGameInfo`, `PhaseRuntimeState`,
  `CosmeticOwnership`) are safe to read anywhere once you have them. Queries returning live Bukkit
  objects (`Player`, `World`, `Entity`) follow Bukkit's rules: use them on the main thread.

Main-thread-only operations added in 1.8.0, for reference:

| Service | Methods |
|---|---|
| `CustomGameService` | all |
| `GliderService` | `deploy`, `stop`, `preview`, `stopPreview` |
| `CelebrationService` | `preview`, `stopPreview` |
| `PhaseService` | `advancePhase` |
| `PlayerService` | `setSpectatorCameraTarget`, `clearSpectatorCameraTarget` |
| `RevivalService` | `checkRevival` (and `revive`, as before) |
| `GameLootService` | `refill` (and the other world/inventory operations, as before) |
| `DeathmatchService` | `unfreeze` |
| `SettingsService` | `set` |

## 2. Results

Operations report what actually happened rather than what was requested.

- **Enum and record results** (`SettingChangeResult`, `CustomGameService.JoinResult`,
  `RevivalResult`, `DeathmatchStartResult`, `CosmeticChange`) distinguish refusal, invalid input,
  missing data and failure. Read the specific status instead of treating every non-success alike.
- **Accepted is not finished** where a result says so: `CustomGameService.JoinResult#JOINING` means
  the join is underway (players are being seated), and a `GameLootService#refill` only starts a new
  cycle - containers are filled when opened.
- **Booleans** on older methods mean "did it happen now". A `false` can mean refused, not
  applicable or unavailable; use the newer result-returning method where one exists, for instance
  `ChatChannelService#switchChannel` instead of `setChannel`, or `RevivalService#checkRevival`
  before `revive`.
- **Unavailable features** are reported, not faked: a Free-edition service answers `false` or empty
  (`GliderService#isCatalogAvailable`, `CelebrationService#isAvailable`); check
  `PlatformService#getCapability` to tell restriction from failure. A method a running Zentrix is
  too old to implement throws `UnsupportedOperationException` naming the API version it needs.

## 3. Snapshots, live views and editors

| Kind | Examples | Valid for |
|---|---|---|
| **Snapshot** - immutable copy of one moment | records: `CustomGameInfo`, `PhaseRuntimeState`, `GliderDefinition`, `CelebrationDefinition`, `DeathmatchSettingsSnapshot`, `CosmeticOwnership`, `LootPlacement` | forever; it never changes. Items and locations inside are copies. |
| **Live view** - reads current state on each call | `ZentrixGame`, `ZentrixPlayer`, `ZentrixTeam` | while the match exists. After it ends, queries return its final or empty state; do not keep them across matches. |
| **Mutable event payload** | `ChatMessageEvent#getRecipients`, `LootContainerFilledEvent#getDrops`, `ClassKitGrantEvent#getItems`, `SidebarRender#getLines` | only while the event or contribution is being handled. Zentrix reads it afterwards; keeping and changing it later has no effect. |

Recipient sets in events are removal-only: removing is supported, adding throws
`UnsupportedOperationException`, so an audience can be narrowed but never widened.

## 4. Failure isolation

- A listener or contributor that throws never breaks the match. Zentrix logs the failure and
  continues as if the listener had not changed anything (for intercept events whose dispatch failed,
  the proposal as built is used).
- Gameplay hook handlers that throw count as passing. Scenario hooks keep their per-match failure
  limit, after which the scenario is disabled for that match.
- Validation runs after your changes: setters on event payloads reject invalid values (negative or
  non-finite magnitudes, empty items, unknown celebration IDs) with `IllegalArgumentException`, and
  identity masks still require a free alias.

## 5. Privacy and authority

- Service methods are trusted plugin calls; they do not check a player's permissions. When you act
  on a player's request, check their permissions yourself before calling.
- `CustomGameService#join` still enforces game passwords; the password itself is never exposed.
- Hidden identities stay hidden: events about masks expose real identities to plugins only. Show
  players display names (`IdentityService#getDisplayName`), never real names of masked players.
- Spectator isolation is not configurable through events: chat and broadcast audiences can only be
  narrowed, spectator cameras only view living players of the watched match.
- `SettingsService` exposes only the settings menu's allow-list, without credentials or licensing.
- Same-process plugins are not sandboxed. These rules prevent accidents; they are not a security
  boundary against a malicious plugin.
