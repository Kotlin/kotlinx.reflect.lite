<a href="http://slack.kotlinlang.org/"><img src="http://slack.kotlinlang.org/badge.svg" height="20"></a>
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/kotlin/kotlinx.reflect.lite/kotlinx.reflect.lite/images/download.svg)](https://bintray.com/kotlin/kotlinx.reflect.lite/kotlinx.reflect.lite/_latestVersion)

# kotlinx.reflect.lite

This library provides an API to introspect [Kotlin](https://kotlinlang.org) classes. Its main feature is the small size which makes it possible to use it on memory-constrained devices.

## Example

Here's an example that outputs names of declarations in a Kotlin class, along with the names of their parameters:
```
import kotlinx.reflect.lite.ReflectionLite

class Example(val p: String) {
    fun sum(x: Int, y: Int) = x + y
    fun product(x: Double, y: Double) = x + y
    fun <T> first(list: List<T>): T = list[0]
}

fun main(args: Array<String>) {
    val metadata = ReflectionLite.loadClassMetadata(Example::class.java)!!
    for (function in metadata.functions) {
        println("function ${function.name} (${function.parameters.joinToString { p -> "${p.name}" }})")
    }
    for (constructor in metadata.constructors) {
        println("constructor (${constructor.parameters.joinToString { p -> "${p.name}" }})")
    }
    for (property in metadata.properties) {
        println("property ${property.name}")
    }
}
```

Output:
```
function first (list)
function product (x, y)
function sum (x, y)
constructor (p)
property p
```

## How to use

To introspect a Kotlin class, use one of the methods in [`ReflectionLite.loadClassMetadata](https://github.com/Kotlin/kotlinx.reflect.lite/blob/master/src/main/java/kotlinx/reflect/lite/ReflectionLite.kt) to load metadata from a Java reflection Class object.

[ClassMetadata](https://github.com/Kotlin/kotlinx.reflect.lite/blob/master/src/main/java/kotlinx/reflect/lite/ClassMetadata.kt) allows to introspect Kotlin-specific features of the class, as well as its members. Explore [the available API](https://github.com/Kotlin/kotlinx.reflect.lite/tree/master/src/main/java/kotlinx/reflect/lite) for more details.

## Maven

Add the jcenter repository:

```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```

Add the dependency:

```xml
<dependency>
    <groupId>org.jetbrains.kotlinx</groupId>
    <artifactId>kotlinx.reflect.lite</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Gradle

jcenter is configured by default in Gradle, however you may need to include it manually in some cases:

```groovy
repositories {
    jcenter()
}
```

Add the dependency:

```groovy
compile 'org.jetbrains.kotlinx:kotlinx.reflect.lite:1.1.0'
```

## FAQ

#### Why not just use Java reflection instead?

Java reflection cannot figure out Kotlin-specific features of the introspected program. There's a lot of information that cannot be easily represented in the JVM bytecode format: for example, nullability of Kotlin types, or different Kotlin modifiers (`data`, `lateinit`, `suspend`, ...), etc.

#### Why not just use Kotlin reflection instead?

While Kotlin has its own reflection library, its size is currently above 2Mb which may be unacceptable in some circumstances, for example on Android.

The size of the minimized version of this library is about 216Kb.
