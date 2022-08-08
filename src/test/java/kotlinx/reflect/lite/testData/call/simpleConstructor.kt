package tests.call.simpleConstructor

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*

class A(val result: String)

fun box(): String {
    val aCons = ((A::class.java).kotlinClass as KClass<A>).constructors.first()
    val a = aCons.call("OK")
    return a.result
}
