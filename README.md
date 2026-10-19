# ZentrixAPI

[![](https://jitpack.io/v/ItsHarshXD/ZentrixAPI.svg)](https://jitpack.io/#ItsHarshXD/ZentrixAPI)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://ItsHarshXD.github.io/ZentrixAPI/)

Developer API for creating Zentrix Battle Royale addons.

## Resources

- [Developer API Wiki](https://zentrix.gitbook.io/docs/dev-api) - Full documentation
- [Javadocs](https://itsharshxd.github.io/ZentrixAPI/) - API reference
- [Example Addon](https://github.com/ItsHarshXD/ZentrixExampleAddon) - Working example
- [Extending Zentrix](docs/EXTENDING.md) - Ownership, ordering and coexistence of addon contributions
- [Threads, results and safety](docs/THREADING.md) - Legal calls, result meanings and data lifetime
- [Configuration, persistence and frozen state](docs/CONFIGURATION.md) - What is saved, and what a match keeps

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.ItsHarshXD:ZentrixAPI:1.9.0")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.ItsHarshXD:ZentrixAPI:1.9.0'
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
    <version>1.9.0</version>
    <scope>provided</scope>
</dependency>
```

## Quick Start

### 1. Create Your Addon

```java
public final class MyAddon extends ZentrixAddon {
    @Override
    protected void onAddonEnable() {
        // Get the API
        ZentrixAPI api = ZentrixAPI.get();
        
        // Use a service
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
// Get the API from one place
ZentrixAPI api = ZentrixAPI.get();
api.getGameService().getActiveGames();
api.getCurrencyService().getBalance(player);
```

## Compatibility

| | Supported |
|---|---|
| API contract | 1.9.0, reported at runtime by `ZentrixAPI.get().getAPIVersion()` and `ApiVersion.CURRENT` |
| Server software | Paper or Purpur |
| Minecraft | 1.21.9 – 26.3; 26.3 support is in beta and flagged by the startup report and `ServerPlatform#beta()` |
| Java | 21 for 1.21.x servers; 26.x servers need Java 25. The API itself is compiled for Java 21 |
| Editions | Premium, Unlicensed and Free. Free restricts some features; check `PlatformService#getCapability` |
| Required plugin | PlaceholderAPI 2.12.3 or newer (Zentrix does not start without it) |
| Optional plugins | TAB 6.1.2+, Simple Voice Chat bukkit-2.6.21+, WorldGuard 7.0.14+, FastAsyncWorldEdit 2.15.4+ (Cornucopia), ItemsAdder and Nexo (Premium) |

These are the combinations the Zentrix source declares support for; the published artifact and a
given server are not verified by this table. Ask the running server instead of assuming:

```java
PlatformService platform = ZentrixAPI.get().getPlatformService();
platform.getEdition();                       // PREMIUM, UNLICENSED or FREE
platform.getServer();                        // Minecraft version, software, Java, beta flag
platform.getIntegration("WorldGuard");       // installed, version, hooked or why not
platform.getCapability(ZentrixCapabilities.CORNUCOPIA); // AVAILABLE, RESTRICTED, DEPENDENCY_MISSING, ...
```

### Dependency and load order

- Depend on the API as `compileOnly` / `provided` and never shade it: Zentrix ships the API
  classes itself, so there is exactly one copy of them at runtime in every edition, obfuscated
  releases included.
- Declare `depend: [Zentrix]` so Zentrix is enabled first. A plugin that only `softdepend`s can
  listen for `ZentrixReadyEvent`; `ZentrixShutdownEvent` fires just before the API goes away.
- `ZentrixAddon` checks `getRequiredAPIVersion()` with semantic versioning: same major version,
  and at least that minor and patch version. A different major version or a malformed version
  string keeps the addon from starting, as do any `getRequiredCapabilities()` that this server
  cannot provide.
- `ZentrixProvider.register`/`unregister` are core bootstrap internals, not for addons.

## Requirements

- Java 21+ to compile against the API
- A server from the compatibility table above
- Zentrix plugin

## License

All Rights Reserved
