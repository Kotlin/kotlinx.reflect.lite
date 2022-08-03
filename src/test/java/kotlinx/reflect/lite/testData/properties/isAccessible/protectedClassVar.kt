package tests.properties.protectedClassVar

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class A(param: String) {
    protected var v: String = param

    fun ref() = (A::class.java).kotlinClass.getMemberByName("v") as KMutableProperty1<A, String>
}

fun box(): String {
    val a = A(":(")
    val f = a.ref()

    try {
        f.get(a)
        return "Fail: protected property getter is accessible by default"
    } catch (e: IllegalAccessException) {
        assertEquals("class kotlinx.reflect.lite.calls.CallerImpl\$Method cannot access a member of class tests.properties.protectedClassVar.A with modifiers \"protected final\"", e.message)
    }

    try {
        f.set(a, ":D")
        return "Fail: protected property setter is accessible by default"
    } catch (e: IllegalAccessException) {
        assertEquals("class kotlinx.reflect.lite.calls.CallerImpl\$Method cannot access a member of class tests.properties.protectedClassVar.A with modifiers \"protected final\"", e.message)
    }

    f.isAccessible = true

    f.set(a, ":)")

    return if (f.get(a) != ":)") "Fail: ${f.get(a)}" else "OK"
}
