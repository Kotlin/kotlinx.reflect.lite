package tests.callBy.simpleConstructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.impl.*

class A(val result: String = "OK")

fun box(): String {
    val aCons = (A::class.java.kotlin).constructors.first()
    return aCons.callBy(mapOf()).result
}
