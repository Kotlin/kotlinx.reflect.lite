package tests.callBy.simpleMemberFunciton

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

class A {
    fun foo(x: Int): String {
        assert(x == 42) { return "42" }
        return "OK"
    }
}

fun box(): String {
    val foo = (A::class.java).kotlinClass.getMemberByName("foo") as KFunction<String>
    return foo.callBy(mapOf(foo.parameters[0] to A(), foo.parameters[1] to 42))
}
