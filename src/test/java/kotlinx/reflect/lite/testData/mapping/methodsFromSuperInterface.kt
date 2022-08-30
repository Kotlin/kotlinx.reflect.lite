package tests.mapping.methodsFromSuperInterface

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

interface A1 {
    fun a1()
}

interface A2 {
    fun a2()
}

interface B1 : A1
interface B2 : A1, A2

interface C : B2

abstract class D : B1, C

fun box(): String {
    assertEquals("public abstract void tests.mapping.methodsFromSuperInterface.A1.a1()", (D::class.java.kDeclarationContainer.getMemberByName("a1") as KFunction<*>).javaMethod.toString())
    assertEquals("public abstract void tests.mapping.methodsFromSuperInterface.A2.a2()", (D::class.java.kDeclarationContainer.getMemberByName("a2") as KFunction<*>).javaMethod!!.toString())

    return "OK"
}
