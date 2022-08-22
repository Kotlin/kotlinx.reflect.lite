package tests.callBy.nonDefaultParameterOmitted

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

fun foo(x: Int, y: Int = 2) = x + y

fun box(): String {
    val foo = Class.forName("tests.callBy.nonDefaultParameterOmitted.NonDefaultParameterOmittedKt").kotlinPackage.getMemberByName("foo")
    try {
        foo.callBy(mapOf())
        return "Fail: IllegalArgumentException must have been thrown"
    }
    catch (e: IllegalArgumentException) {
        // OK
    }

    try {
        foo.callBy(mapOf(foo.parameters.last() to 1))
        return "Fail: IllegalArgumentException must have been thrown"
    }
    catch (e: IllegalArgumentException) {
        // OK
    }
    assertEquals(25, foo.callBy(mapOf(foo.parameters[0] to 10, foo.parameters[1] to 15)))
    return "OK"
}
