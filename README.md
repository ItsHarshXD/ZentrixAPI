# ZentrixAPI

[![](https://jitpack.io/v/ItsHarshXD/ZentrixAPI.svg)](https://jitpack.io/#ItsHarshXD/ZentrixAPI)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://ItsHarshXD.github.io/ZentrixAPI/)

Developer API for creating Zentrix Battle Royale addons.

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

```java
public final class MyAddon extends ZentrixAddon {
    @Override
    protected void onAddonEnable() {
        ZentrixAPI api = getAPI();
        getServer().getPluginManager().registerEvents(new MyListener(), this);
    }
}
```

## Documentation

See the [Developer API Wiki](https://zentrix.gitbook.io/docs/dev-api) for full documentation.

Browse the [Javadocs](https://itsharshxd.github.io/ZentrixAPI/) for API reference.

## Requirements

- Java 21+
- Paper 1.21.9-1.21.10
- Zentrix plugin

## License

All Rights Reserved
