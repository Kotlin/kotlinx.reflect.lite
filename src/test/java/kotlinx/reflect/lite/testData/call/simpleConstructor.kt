package tests.call.simpleConstructor

import kotlinx.reflect.lite.tests.*

class A(val result: String)

fun box(): String {
    val aCons = (A::class.java).toLiteKClass().constructors.first()
    val a = aCons.call("OK")
    return a.result
}
