package tests.properties.privateClassVar

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class A {
    private var value = 0

    fun ref() = (A::class.java).kotlinClass.getMemberByName("value") as KMutableProperty1<A, Int>
}

fun box(): String {
    val a = A()
    val p = a.ref()
    try {
        p.set(a, 1)
        return "Fail: private property is accessible by default"
    } catch(e: IllegalAccessException) {
        assertEquals("class kotlinx.reflect.lite.calls.CallerImpl\$FieldSetter cannot access a member of class tests.properties.privateClassVar.A with modifiers \"private\"", e.message)
    }

    p.isAccessible = true

    p.set(a, 2)
    p.get(a)

    p.isAccessible = false
    try {
        p.set(a, 3)
        return "Fail: setAccessible(false) had no effect"
    } catch(e: IllegalAccessException) {
        assertEquals("class kotlinx.reflect.lite.calls.CallerImpl\$FieldSetter cannot access a member of class tests.properties.privateClassVar.A with modifiers \"private\"", e.message)
    }

    return "OK"
}
