package tests.callBy.simpleTopLevelFunction

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

fun foo(result: String = "OK") = result

fun box(): String {
    val clazz = Class.forName("tests.callBy.simpleTopLevelFunction.SimpleTopLevelFunctionKt").kDeclarationContainer
    val foo = clazz.getMemberByName("foo") as KFunction<String>
    return foo.callBy(mapOf())
}
