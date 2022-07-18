package tests.call.cannotCallEnumConstructor

import kotlinx.reflect.lite.tests.*

enum class E

fun box(): String {
    try {
        val c = (E::class.java).toLiteKClass().constructors.single()
        //c.isAccessible = true // todo KCallable.isAccessible
        c.call()
        return "Fail: constructing an enum class should not be allowed"
    } catch (e: Throwable) {
        return "OK"
    }
}
