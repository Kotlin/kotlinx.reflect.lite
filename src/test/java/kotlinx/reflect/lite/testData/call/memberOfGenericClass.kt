package tests.call.memberOfGenericClass

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*

var result = "Fail"

class A<T> {
    fun foo(t: T) {
        result = t as String
    }
}

fun box(): String {
    val a = (A::class.java).toLiteKClass()
    val foo = a.getMemberByName("foo")
    foo.call(a.getPrimaryConstructor().call(), "OK")
    return result
}
