package tests.callBy.defaultAndNonDefaultIntertwined

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo(a: String, b: String = "b", c: String, d: String = "d", e: String) =
        a + b + c + d + e

fun box(): String {
    val foo = Class.forName("tests.callBy.defaultAndNonDefaultIntertwined.DefaultAndNonDefaultIntertwinedKt").kDeclarationContainer
        .getMemberByName("foo") as KFunction<String>
    val p = foo.parameters
    assertEquals("abcde", foo.callBy(mapOf(
            p[0] to "a",
            p[2] to "c",
            p[4] to "e"
    )))

    return "OK"
}
