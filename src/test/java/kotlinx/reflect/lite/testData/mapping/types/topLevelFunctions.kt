package tests.mapping.types.topLevelFunctions

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun free(s: String): Int = s.length

fun Any.extension() {}

fun box(): String {
    val clazz = Class.forName("tests.mapping.types.topLevelFunctions.TopLevelFunctionsKt").kotlinClass
    val free = clazz.getMemberByName("free") as KFunction<Int>
    val extension = clazz.getMemberByName("extension") as KFunction<Unit>
    assertEquals(java.lang.Integer.TYPE, free.returnType.javaType)
    assertEquals(String::class.java, free.parameters.single().type.javaType)

    assertEquals(Any::class.java, extension.parameters.single().type.javaType)

    return "OK"
}