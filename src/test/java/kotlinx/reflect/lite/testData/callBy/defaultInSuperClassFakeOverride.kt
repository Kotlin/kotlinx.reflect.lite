/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.defaultInSuperClassFakeOverride

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

// Part of this test: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/callBy/defaultInSuperClass.kt
// TODO: support descriptors for overrides (for now descriptors for fake overrides only are supported)
open class A {
    open fun foo(a: String, b: String = "b") = b + a
}

class B : A()

fun box(): String {
    val f = (B::class.java).kotlin.getMemberByName("foo")

    assertEquals("ba", f.callBy(mapOf(
        f.parameters.first() to B(),
        f.parameters.single { it.name == "a" } to "a"
    )))

    return "OK"
}
