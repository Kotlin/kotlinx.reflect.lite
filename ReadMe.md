[![Kotlin](https://img.shields.io/badge/kotlin-1.0.0-blue.svg)](https://kotlinlang.org) [![Kotlin Slack](https://img.shields.io/badge/chat-kotlin%20slack-orange.svg)](https://kotlinslackin.herokuapp.com)


# kotlinx.reflect.lite

This library provides an API to introspect Kotlin symbols at runtime. Its main feature is the small size which makes it possible to use it on memory-constrained devices.

[ ![Download](https://api.bintray.com/packages/kotlin/kotlinx.reflect.lite/kotlinx.reflect.lite/images/download.svg) ](https://bintray.com/kotlin/kotlinx.reflect.lite/kotlinx.reflect.lite/_latestVersion)

## Maven

Add jcenter repository (if you don't have it yet)

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>https://jcenter.bintray.com</url>
</repository>
```

Add a dependency:

```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx.reflect.lite</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Gradle

jcenter is configured by default in gradle however you may need to include it in some cases

```groovy
repositories {
    jcenter()
}
```

Add a dependency:

```groovy
compile 'org.jetbrains.kotlinx:kotlinx.reflect.lite:1.0.0'
```

## FAQ

#### Why not just use Java reflection instead?

Java reflection cannot figure out Kotlin-specific features of the introspected program. For example, nullability of Kotlin types is erased in JVM bytecode.

Also parameter names were not supported in Java reflection until Java 8.

#### Why not just use Kotlin reflection instead?

While Kotlin has its own reflection library, its size is currently above 2Mb which may be unacceptable in some circumstances, for example on Android.

The size of the minimized version of this library is about 100Kb plus the dependency on protobuf-java.

#### What is supported?

Currently the only supported features are names of parameters and nullability of their types.
