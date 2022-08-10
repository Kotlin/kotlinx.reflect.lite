package tests.parameters.instanceParameterOfFakeOverride

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

open class A {
    val property = "OK"

    fun function() {}
}

class B : A()

fun box(): String {
    val bProperty = (B::class.java).kDeclarationContainer.getMemberByName("property")
    assertEquals(KParameter.Kind.INSTANCE, bProperty.parameters[0].kind)
    assertEquals(B::class.java, bProperty.parameters[0].type.javaType)

    val bFunction = (B::class.java).kDeclarationContainer.getMemberByName("function")
    assertEquals(KParameter.Kind.INSTANCE, bFunction.parameters[0].kind)
    assertEquals(B::class.java, bFunction.parameters[0].type.javaType)

    return "OK"
}
