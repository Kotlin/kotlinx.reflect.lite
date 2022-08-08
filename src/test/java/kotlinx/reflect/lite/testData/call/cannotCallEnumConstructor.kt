package tests.call.cannotCallEnumConstructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

enum class E

fun box(): String {
    try {
        val c = ((E::class.java).kotlinClass as KClass<E>).constructors.single()
        c.isAccessible = true
        c.call()
        return "Fail: constructing an enum class should not be allowed"
    } catch (e: Throwable) {
        return "OK"
    }
}
