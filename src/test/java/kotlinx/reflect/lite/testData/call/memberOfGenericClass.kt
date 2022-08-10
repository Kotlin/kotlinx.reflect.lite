package tests.call.memberOfGenericClass

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*

var result = "Fail"

class A<T> {
    fun foo(t: T) {
        result = t as String
    }
}

fun box(): String {
    val a = ((A::class.java).kotlin)
    val foo = a.getMemberByName("foo")
    foo.call(a.getPrimaryConstructor().call(), "OK")
    return result
}
