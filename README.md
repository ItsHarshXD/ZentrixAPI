# ZentrixAPI

[![](https://jitpack.io/v/ItsHarshXD/ZentrixAPI.svg)](https://jitpack.io/#ItsHarshXD/ZentrixAPI)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://ItsHarshXD.github.io/ZentrixAPI/)

Developer API for creating Zentrix Battle Royale addons.

## Resources

- [Developer API Wiki](https://zentrix.gitbook.io/docs/dev-api) - Full documentation
- [Javadocs](https://itsharshxd.github.io/ZentrixAPI/) - API reference
- [Example Addon](https://github.com/ItsHarshXD/ZentrixExampleAddon) - Working example


## Features

- Query games, players, teams, phases, and stats
- React to gameplay via events
- Register custom recipes with craft limits
- Store addon data under `plugins/Zentrix/addons/<addon-id>/`

## Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.ItsHarshXD:ZentrixAPI:1.0.0")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.ItsHarshXD:ZentrixAPI:1.0.0'
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
    <version>1.0.0</version>
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

## Requirements

- Java 21+
- Paper 1.21.9-.1.21.10
- Zentrix plugin

## License

All Rights Reserved
