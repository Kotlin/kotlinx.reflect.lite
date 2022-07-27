package kotlinx.reflect.lite.tests

import kotlinx.reflect.lite.*

internal fun <T: Any> Class<T>.toLiteKDeclarationContainer() = ReflectionLite.loadClassMetadata(this)
internal fun <T : Any> Class<T>.toLiteKClass() = toLiteKDeclarationContainer() as KClass<T>

internal fun KDeclarationContainer.getMemberByName(name: String) =
    members.single { it.name == name }

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
