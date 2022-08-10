package tests.parameters.isMarkedNullable

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class A {
    fun <T, U : Any> foo(p1: String, p2: String?, p3: T, p4: U, p5: U?) { }
}

fun Any?.ext() {}

fun box(): String {
    val ps = (A::class.java).kDeclarationContainer.getMemberByName("foo").parameters.map { it.type.isMarkedNullable }
    assertEquals(listOf(false, false, true, false, false, true), ps)

    val ext = Class.forName("tests.parameters.isMarkedNullable.IsMarkedNullableKt").kDeclarationContainer.getMemberByName("ext")
    assertTrue(ext.parameters.single().type.isMarkedNullable)

    return "OK"
}
