
// KT-13700 Exception obtaining descriptor for property reference

package tests.properties.genericOverriddenProperty

import kotlin.test.assertEquals

interface H<T> {
    val parent : T?
}

interface A : H<A>

fun box(): String {
    assertEquals("tests.properties.genericOverriddenProperty.A?", A::parent.returnType.toString())
    assertEquals("T?", H<A>::parent.returnType.toString())

    return "OK"
}