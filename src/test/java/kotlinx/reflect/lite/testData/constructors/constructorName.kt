package tests.constructors.constructorName

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlin.test.assertEquals

class A

fun box(): String {
    assertEquals("<init>", (A::class.java.kotlin).constructors.first().name)
    return "OK"
}
