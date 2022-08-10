package tests.parameters.isOptional

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

// Part of the test: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/parameters/isOptionalOverridesParameters.kt
// Only fake overrides are supported for now
open class A {
    open fun foo(x: Int, y: Int = 1) {}
}

class B : A() {
    override fun foo(x: Int, y: Int) {}
}

class C : A()


fun Int.extFun() {}

fun box(): String {
    assertEquals(listOf(false, false, true), (A::class.java).kDeclarationContainer.getMemberByName("foo").parameters.map { it.isOptional })

    assertFalse(Class.forName("tests.parameters.isOptional.IsOptionalKt").kDeclarationContainer.getMemberByName("extFun").parameters.single().isOptional)

    return "OK"
}
