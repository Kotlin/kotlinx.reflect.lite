package tests.call.simpleTopLevelFunctions

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

fun String.foo(): Int = length

var state = "Fail"

fun bar(result: String) {
    state = result
}

fun box(): String {
    val clazz = Class.forName("tests.call.simpleTopLevelFunctions.SimpleTopLevelFunctionsKt").kotlinPackage
    val foo = clazz.getMemberByName("foo")
    val f = foo.call("abc")
    if (f != 3) return "Fail: $f"

    try {
        foo.call()
        return "Fail: IllegalArgumentException should have been thrown"
    }
    catch (e: IllegalArgumentException) {}

    try {
        foo.call(42)
        return "Fail: IllegalArgumentException should have been thrown"
    }
    catch (e: IllegalArgumentException) {}

    val bar = clazz.getMemberByName("bar")
    bar.call("OK")
    return state
}
