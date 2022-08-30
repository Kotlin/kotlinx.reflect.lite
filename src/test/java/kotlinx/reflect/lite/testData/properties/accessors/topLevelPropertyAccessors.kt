package tests.properties.accessors.topLevelPropertyAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

var state: String = ""

fun box(): String {
    val clazz = Class.forName("tests.properties.accessors.topLevelPropertyAccessors.TopLevelPropertyAccessorsKt").kotlinPackage
    val state = clazz.getMemberByName("state") as KMutableProperty0<String>

    assertEquals("", state.getter.invoke())
    assertEquals("", state.getter())

    state.setter("OK")

    return state.get()
}
