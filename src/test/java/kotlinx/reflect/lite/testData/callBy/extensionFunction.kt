package tests.callBy.extensionFunction

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun String.sum(other: String = "b") = this + other

fun box(): String {
    val f = Class.forName("tests.callBy.extensionFunction.ExtensionFunctionKt").kDeclarationContainer.getMemberByName("sum")
    assertEquals("ab", f.callBy(mapOf(f.parameters.first() to "a")))
    return "OK"
}
