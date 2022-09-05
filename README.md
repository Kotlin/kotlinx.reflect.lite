[![Kotlin Experimental](https://kotl.in/badges/experimental.svg)](https://kotlinlang.org/docs/components-stability.html)
[![JetBrains incubator project](https://jb.gg/badges/incubator.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.0-blue.svg)](https://kotlinlang.org)

# kotlinx.reflect.lite

`kotlinx-reflect-lite` is an experimental attempt to replace existing `kotlin-reflect` implementation 
with a new lightweight and fast library, while preserving the very same API shape.


## Status of the library

The libary is in purely **experimental pre-alpha state**.

It is being prototyped, evaluated and constantly changing.
There is no binary or source compatibilities, and no guarantees that we won't 
abandon this library (including fixing any issues, answering any questions, or providing any releases) 
whatsoever.


## How to use

Currently, the API of `kotlinx-reflect-lite` is a replicated subset of `kotlin-reflect` with a different package.
All core interfaces can be drop-in replaced by redacting a package, for example, 
`kotlin.reflect.KCallable` has a matching `kotlinx.reflect.lite.KCallable` interface, as well as any 
other top-level entity from `kotlin.reflect` package.

The API surface of these replicated types is incomplete and allowed to throw `UnsupportedOperationException`.

### Available API

In addition to core interfaces, subset of reflective extensions is supported as well.

The full list is:
```kotlin
// Kotlin reflection to Java
ReflectJvmMapping.javaField
ReflectJvmMapping.javaGetter
ReflectJvmMapping.javaSetter
ReflectJvmMapping.javaMethod
ReflectJvmMapping.javaConstructor
ReflectJvmMapping.companionObject
ReflectJvmMapping.javaType
ReflectJvmMapping.kotlinProperty
ReflectJvmMapping.kotlinFunction

// Java reflection to Kotlin
JvmClassMapping.kotlin
JvmClassMapping.kotlinPackage
JvmClassMapping.java

// Callables support
KCallablesJvm.setAccessible
KFunction.callBy
KFunction.isSuspend
KFunction.getParameters
KCallables.callSuspend
```

### Gradle
```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx.reflect.lite:1.1.0")
}
```

### Maven

```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx.reflect.lite</artifactId>
    <version>1.1.0</version>
</dependency>
```
