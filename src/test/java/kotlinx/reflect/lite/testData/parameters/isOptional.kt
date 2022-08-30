/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.isOptional

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
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
    assertEquals(listOf(false, false, true), (A::class.java).kotlin.getMemberByName("foo").parameters.map { it.isOptional })

    assertFalse(Class.forName("tests.parameters.isOptional.IsOptionalKt").kotlinPackage.getMemberByName("extFun").parameters.single().isOptional)

    return "OK"
}
