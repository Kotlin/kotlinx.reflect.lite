

package tests.functions.genericOverriddenFunction

import kotlin.test.assertEquals

interface H<T> {
    fun foo(): T?
}

interface A : H<A>

fun box(): String {
    assertEquals("tests.functions.genericOverriddenFunction.A?", A::foo.returnType.toString())
    assertEquals("T?", H<A>::foo.returnType.toString())

    return "OK"
}