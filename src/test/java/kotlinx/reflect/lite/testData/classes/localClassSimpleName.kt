package tests.classes.localClassSimpleName

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlin.test.assertEquals

fun check(klass: KClass<*>, expectedName: String) {
    assertEquals(expectedName, klass.simpleName)
}

fun localInMethod() {
    fun localInMethod(unused: Any?) {
        class Local
        check((Local::class.java.kotlinClass as KClass<Local>), "Local")

        class `Local$With$Dollars`
        check((`Local$With$Dollars`::class.java.kotlinClass as KClass<`Local$With$Dollars`>), "Local\$With\$Dollars")
    }
    localInMethod(null)

    class Local
    check((Local::class.java.kotlinClass as KClass<Local>), "Local")

    class `Local$With$Dollars`
    check((`Local$With$Dollars`::class.java.kotlinClass as KClass<`Local$With$Dollars`>), "Local\$With\$Dollars")
}

class LocalInConstructor {
    init {
        class Local
        check((Local::class.java.kotlinClass as KClass<Local>), "Local")

        class `Local$With$Dollars`
        check((`Local$With$Dollars`::class.java.kotlinClass as KClass<`Local$With$Dollars`>), "Local\$With\$Dollars")
    }
}

fun box(): String {
    localInMethod()
    LocalInConstructor()
    return "OK"
}
