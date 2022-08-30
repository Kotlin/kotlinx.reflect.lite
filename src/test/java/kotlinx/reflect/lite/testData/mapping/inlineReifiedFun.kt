/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.inlineReifiedFun

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

inline fun <reified T> f() = 1

fun g() {}

class Foo {
    inline fun <reified T> h(t: T) = 1
}

fun box(): String {
    val g = Class.forName("tests.mapping.inlineReifiedFun.InlineReifiedFunKt").kDeclarationContainer.getMemberByName("g") as KFunction<*>
    assertEquals(g as Any?, g.javaMethod!!.kotlinFunction)

    val h = Foo::class.java.kDeclarationContainer.members.single { it.name == "h" } as KFunction<*>
    assertEquals(h, h.javaMethod!!.kotlinFunction as Any?)

    return "OK"
}
