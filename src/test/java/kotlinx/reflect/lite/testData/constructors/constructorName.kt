package tests.constructors.constructorName

import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class A

fun box(): String {
    assertEquals("<init>", A::class.java.toLiteKClass().constructors.first().name)
    return "OK"
}