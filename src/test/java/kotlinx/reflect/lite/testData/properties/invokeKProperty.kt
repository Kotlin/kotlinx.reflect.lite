package tests.properties.invokeKProperty

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

class A(val foo: String)

fun box(): String {
    return ((A::class.java).kotlin.getMemberByName("foo") as KProperty1<A, String>).invoke(A("OK")) as String
}
