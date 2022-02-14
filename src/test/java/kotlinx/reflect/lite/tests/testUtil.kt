package kotlinx.reflect.lite.tests

import kotlinx.reflect.lite.*

internal fun <T : Any> Class<T>.toLiteKClass() = ReflectionLite.loadClassMetadata(this)

internal inline fun test(name: String, box: () -> String) {
    print("$name: ")
    try {
        val res = box()
        println(res)
    } catch (e: Throwable) {
        println("EXCEPTION: ${e.message}")
        throw e
    }
}
