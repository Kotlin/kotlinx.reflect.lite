package tests.call.cannotCallEnumConstructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*


enum class E

fun box(): String {
    try {
        val c = ((E::class.java).kotlin).constructors.single()
        c.isAccessible = true
        c.call()
        return "Fail: constructing an enum class should not be allowed"
    } catch (e: Throwable) {
        return "OK"
    }
}
