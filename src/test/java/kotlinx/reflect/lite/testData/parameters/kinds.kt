/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.kinds

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class A {
    fun Int.foo(x: String) {}

    inner class Inner(s: String) {}
}

fun box(): String {
    val foo = A::class.java.kotlin.getMemberByName("foo")
    val inner = (A::class.java.kotlin).nestedClasses.single { it.simpleName == "Inner" }
    assertEquals(listOf(KParameter.Kind.INSTANCE, KParameter.Kind.EXTENSION_RECEIVER, KParameter.Kind.VALUE), foo.parameters.map { it.kind })
    assertEquals(listOf(KParameter.Kind.INSTANCE, KParameter.Kind.VALUE), inner.constructors.first().parameters.map { it.kind })

    return "OK"
}
