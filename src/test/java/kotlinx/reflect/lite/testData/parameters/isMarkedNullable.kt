/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.isMarkedNullable

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class A {
    fun <T, U : Any> foo(p1: String, p2: String?, p3: T, p4: U, p5: U?) { }
}

fun Any?.ext() {}

fun box(): String {
    val ps = (A::class.java).kotlin.getMemberByName("foo").parameters.map { it.type.isMarkedNullable }
    assertEquals(listOf(false, false, true, false, false, true), ps)

    val ext = Class.forName("tests.parameters.isMarkedNullable.IsMarkedNullableKt").kotlinPackage.getMemberByName("ext")
    assertTrue(ext.parameters.single().type.isMarkedNullable)

    return "OK"
}
