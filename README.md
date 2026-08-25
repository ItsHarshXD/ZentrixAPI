# ZentrixAPI

[![](https://jitpack.io/v/ItsHarshXD/ZentrixAPI.svg)](https://jitpack.io/#ItsHarshXD/ZentrixAPI)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://ItsHarshXD.github.io/ZentrixAPI/)

Developer API for creating Zentrix Battle Royale addons. Version 1.8.0 adds structured-list scenario settings whose fields a scenario declares itself, scenario capabilities that let scenarios rule each other out by the gameplay area they take charge of rather than by name, and world-lifecycle callbacks for scenarios that hold a world-level property in place — all while retaining the complete 1.7.0 binary surface.

## Resources

- [Developer API Wiki](https://zentrix.gitbook.io/docs/dev-api) - Full documentation
- [Javadocs](https://itsharshxd.github.io/ZentrixAPI/) - API reference
- [Example Addon](https://github.com/ItsHarshXD/ZentrixExampleAddon) - Working example


## Features

- Query games, players, teams, phases, and stats
- React to gameplay via events
- Register custom recipes with craft limits
- Store addon data under `plugins/Zentrix/addons/<addon-id>/`
- Resolve copied `game-*` instances and their waiting, Nether, End, and deathmatch worlds
- Query and control Nether/End access, revival, deathmatch startup, force-start, and dynamic game rules
- Manage Cornucopia and per-world game-loot pools, loot-table imports, validation, and recovery
- Control Cornucopia placement, corpse lifecycle, teammate compasses, dragon buses, and block mechanics
- Open and extend every built-in menu, and register or resolve runtime GUI layouts
- Use party and runtime-game-scoped chat services

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.ItsHarshXD:ZentrixAPI:1.8.0")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.ItsHarshXD:ZentrixAPI:1.8.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.ItsHarshXD</groupId>
    <artifactId>ZentrixAPI</artifactId>
    <version>1.8.0</version>
    <scope>provided</scope>
</dependency>
```

## Quick Start

### 1. Create Your Addon

```java
public final class MyAddon extends ZentrixAddon {
    @Override
    protected void onAddonEnable() {
        // Access the API
        ZentrixAPI api = ZentrixAPI.get();
        
        // Use services
        api.getGameService().getActiveGames();
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new MyListener(), this);
    }
}
```

### 2. Add plugin.yml

```yaml
name: MyAddon
version: 1.0.0
main: com.example.myaddon.MyAddon
depend: [Zentrix]
api-version: '1.21'
```

### 3. Use the API Anywhere

```java
// Single, simple way to access the API
ZentrixAPI api = ZentrixAPI.get();
api.getGameService().getActiveGames();
api.getCurrencyService().getBalance(player);
```

| API | Compatible plugin | Notes |
|---|---|---|
| 1.1.x | Zentrix 1.2.x+ | Legacy services |
| 1.3.0 | 1.3.0-capable development builds | Runtime worlds and typed controls |
| 1.4.0 | 1.4.0+ | Dynamic sources and atomic matchmaking |
| 1.5.0 | 1.5.0+ | LocaleService for addon text formatting |
| 1.6.0 | 1.6.0+ | End worlds plus revival, loot, Cornucopia, corpse, GUI, compass, block, and dragon services |
| 1.7.0 | 1.7.0+ | Scenario system: registration, selection, voting, settings, and gameplay hooks |
| 1.8.0 | 1.8.0+ | Structured-list scenario settings with scenario-declared entry fields; scenario capabilities and capability conflicts; scenario world-prepared and world-change callbacks; LocaleService inline-flag delivery |

## Requirements

- Java 21+
- Paper 1.21.9-.1.21.10
- Zentrix plugin

## License

All Rights Reserved
