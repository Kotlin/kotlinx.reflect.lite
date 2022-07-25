package tests.properties.accessors.topLevelPropertyAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlin.reflect.jvm.*
import kotlin.test.assertEquals

var state: String = ""

fun box(): String {
    val prop = ::state.javaField!!.kotlinLiteProperty as KMutableProperty0<String>

    assertEquals("", prop.getter.invoke())
    assertEquals("", prop.getter())

    prop.setter("OK")

    return prop.get()
}
