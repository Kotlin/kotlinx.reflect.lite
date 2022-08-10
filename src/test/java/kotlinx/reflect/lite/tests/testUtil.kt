package kotlinx.reflect.lite.tests

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*

@Deprecated(
    message = "Better use the <T : Any> Class<T>.kotlinClass: KDeclarationContainer extension",
    replaceWith = ReplaceWith("(this.kotlinClass as KClass<T>)", imports = arrayOf("kotlinx.reflect.lite.*, kotlinx.reflect.lite.impl.*")))
internal fun <T : Any> Class<T>.toLiteKClass() = kotlin

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
