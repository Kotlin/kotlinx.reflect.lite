package tests.callBy.simpleConstructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*

class A(val result: String = "OK")

fun box(): String {
    val aCons = (A::class.java.kotlinClass as KClass<A>).constructors.first()
    return aCons.callBy(mapOf()).result
}
