package tests.call.fakeOverride

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

open class A {
    fun foo() = "OK"
}

class B : A()

fun box(): String {
    val foo = ((B::class.java).kotlinClass as KClass<B>).getMemberByName("foo")
    return foo.call(B()) as String
}
