package tests.call.accessPrivateProperties

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.reflect.jvm.*
import kotlin.test.*

private class A(private var bar: String = "") {
    fun getBar() = ((A::class.java).kotlin).getMemberByName("bar")
    fun getKotlinReflectBar() = A::bar
}

// Test case referring the tests.call.incorrectNumberOfArguments.testAccessPrivateBarProperty()
fun box(): String {
    // kotlinx.reflect.lite
    val getBar = A().getBar() as KMutableProperty1<A, String>
    val bar = (A::class.java.kotlin).getMemberByName(
            "bar"
        )
    bar.isAccessible = true
    try {
        getBar.call(A())
    } catch (e: Throwable) {
        assertEquals(
            "class kotlinx.reflect.lite.calls.CallerImpl\$FieldGetter cannot access a member of class tests.call.accessPrivateProperties.A with modifiers \"private\"",
            e.message
        )
    }

    // via kotlin.reflect
    val kotlinReflectGetBar = A().getKotlinReflectBar()
    val kotlinReflectBar = A::class.members.first { it.name == "bar" }
    kotlinReflectBar.isAccessible = true
    assertEquals(null, kotlinReflectGetBar.javaGetter)
    assertEquals(null, kotlinReflectGetBar.javaSetter)
    try {
        kotlinReflectGetBar.call(A())
    } catch (e: Throwable) {
        assertEquals(
            "java.lang.IllegalAccessException: class kotlin.reflect.jvm.internal.calls.CallerImpl\$FieldGetter cannot access a member of class tests.call.accessPrivateProperties.A with modifiers \"private\"",
            e.message
        )
    }
    return "OK"
}
