/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.fakeOverrideToString2

import kotlin.test.assertEquals

interface I1 {
    fun f()
    val x: Int
}

interface I2 {
    fun f()
    val x: Int
}

interface I3 {
    fun f()
    val x: Int
}

interface I : I2, I1, I3

fun box(): String {
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString2.I.f(): kotlin.Unit", I::f.toString())
    assertEquals("val tests.methodsFromAny.fakeOverrideToString2.I.x: kotlin.Int", I::x.toString())

    val f = I::class.members.single { it.name == "f" }
    assertEquals("fun tests.methodsFromAny.fakeOverrideToString2.I.f(): kotlin.Unit", f.toString())
    val x = I::class.members.single { it.name == "x" }
    assertEquals("val tests.methodsFromAny.fakeOverrideToString2.I.x: kotlin.Int", x.toString())

    return "OK"
}
