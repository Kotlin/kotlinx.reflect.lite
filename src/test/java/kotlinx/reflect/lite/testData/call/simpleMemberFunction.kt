package tests.call.simpleMemberFunction

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*

class A {
    fun foo(x: Int, y: Int) = x + y
}

fun box(): String {
    val foo = ((A::class.java).kotlinClass as KClass<A>).getMemberByName("foo")

    val x = foo.call(A(), 42, 239)
    if (x != 281) return "Fail: $x"

    try {
        (A::foo).call()
        return "Fail: no exception"
    } catch (e: Exception) {
    }

    return "OK"
}
