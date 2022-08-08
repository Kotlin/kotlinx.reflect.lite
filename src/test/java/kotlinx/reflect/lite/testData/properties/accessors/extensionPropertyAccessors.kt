package tests.properties.accessors.extensionPropertyAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

var state: String = ""

var String.prop: String
    get() = length.toString()
    set(value) { state = this + value }

fun box(): String {
    val clazz = Class.forName("tests.properties.accessors.extensionPropertyAccessors.ExtensionPropertyAccessorsKt").kotlinClass
    val prop = clazz.getMemberByName("prop") as KMutableProperty1<String, String>

    assertEquals("3", prop.getter.invoke("abc"))
    assertEquals("5", prop.getter("defgh"))

    prop.setter("O", "K")

    return state
}
