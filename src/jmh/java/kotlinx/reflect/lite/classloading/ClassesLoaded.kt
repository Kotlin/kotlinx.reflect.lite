@file:Suppress("UNCHECKED_CAST")

package kotlinx.reflect.lite.classloading

import kotlinx.reflect.lite.*
import java.lang.reflect.*
import java.util.*
import kotlin.collections.HashSet
import kotlin.time.*

@Suppress("unused")
private val triggerClassloading = getNumberOfLoadedClasses()

fun getNumberOfLoadedClasses(): Int {
    val f: Field = ClassLoader::class.java.getDeclaredField("classes")
    f.setAccessible(true)
    val classLoader = Thread.currentThread().contextClassLoader
    val classes: Vector<Class<*>> = f.get(classLoader) as Vector<Class<*>>
    return classes.size
}

fun getLoadedClasses(): Set<Class<*>> {
    val f: Field = ClassLoader::class.java.getDeclaredField("classes")
    f.setAccessible(true)
    val classLoader = Thread.currentThread().contextClassLoader

    @Suppress("UNCHECKED_CAST")
    val classes: Vector<Class<*>> = f.get(classLoader) as Vector<Class<*>>
    return HashSet(classes)
}

@OptIn(ExperimentalTime::class)
inline fun analyzeLoadedClasses(name: String, packageDepth: Int = 3, limit: Int = 7, block: () -> Unit) {
    val before = getLoadedClasses()
    val t = measureTime {
        block()
    }
    println(t)
    val after = getLoadedClasses()
    val diff = after - before
    analyze(name, diff, packageDepth, limit)
}

fun analyze(name: String, diff: Set<Class<*>>, packageDepth: Int, limit: Int) {
    val stat = diff.mapNotNull { it.`package`?.toString()?.substringAfter(" ") }
        .map {
            it.split(".").take(packageDepth).joinToString(".")
        }.groupBy { it }.mapValues { it.value.size }
        .toList().sortedBy { -it.second }.take(limit)

    println("For $name, loaded classes: ${diff.size}")
    for (pair in stat) {
        println(pair.first + ": " + pair.second)
    }
}


@Suppress("unused")
inline fun countClasses(name: String, block: () -> Unit) {
    val before = getNumberOfLoadedClasses()
    block()
    val after = getNumberOfLoadedClasses()
    println("Classes loaded for $name " + (after - before))
}
