package tests.properties.accessors.extensionPropertyAccessors

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.reflect.jvm.*
import kotlin.test.*

var state: String = ""

// TODO: implement top-level extension proeprties
var String.prop: String
    get() = length.toString()
    set(value) { state = this + value }

fun box(): String {
    val prop = String::prop

    val a = ::state.javaField!!.kotlinLiteProperty

    assertEquals("3", prop.getter.invoke("abc"))
    assertEquals("5", prop.getter("defgh"))

    prop.setter("O", "K")

    return state
}
