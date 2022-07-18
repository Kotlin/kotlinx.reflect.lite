package tests.call.fakeOverride

import kotlinx.reflect.lite.tests.*

open class A {
    fun foo() = "OK"
}

class B : A()

fun box(): String {
    val foo = (B::class.java).toLiteKClass().getMemberByName("foo")
    return foo.call(B()) as String
}
