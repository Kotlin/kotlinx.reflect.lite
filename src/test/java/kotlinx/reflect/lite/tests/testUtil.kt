package kotlinx.reflect.lite.tests

import kotlinx.reflect.lite.*

internal fun <T : Any> Class<T>.toLiteKClass() = ReflectionLite.loadClassMetadata(this) as KClass<T>

internal fun <T: Any> KClass<T>.getMemberByName(name: String) =
    members.single { it.name == name }

internal fun KClass<*>.getPropertyByName(name: String) =
    members.single { it.name == name }

// todo util function getFunctionByName<ReturnType> to cast

internal fun <T: Any> KClass<T>.getPrimaryConstructor() = constructors.first() as KFunction<T>

internal inline fun test(name: String, box: () -> String) {
    print("$name: ")
    try {
        val res = box()
        if (res != "OK") {
            error("INCORRECT RESULT: $res")
        } else {
            println(res)
        }
    } catch (e: Throwable) {
        println("EXCEPTION: ${e.message}")
        throw e
    }
}
