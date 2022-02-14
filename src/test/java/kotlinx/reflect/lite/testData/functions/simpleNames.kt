package tests.functions.simpleNames

import kotlin.test.assertEquals

fun foo() {}

class A {
    fun bar() = ""
}

fun Int.baz() = this

fun box(): String {
    assertEquals("foo", ::foo.name)
    assertEquals("bar", A::bar.name)
    assertEquals("baz", Int::baz.name)
    return "OK"
}