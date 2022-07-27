package tests.call.fakeOverrideSubstituted

import kotlinx.reflect.lite.tests.*

open class A<T>(val t: T) {
    fun foo() = t
}

class B(s: String) : A<String>(s)

fun box(): String {
    val foo = (B::class.java).toLiteKClass().getMemberByName("foo")
    return foo.call(B("OK")) as String
}
