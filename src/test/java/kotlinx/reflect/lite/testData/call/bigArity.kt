/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bigArity

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

class A {
    fun foo(
        p00: A, p01: A, p02: A, p03: A, p04: A, p05: A, p06: A, p07: A, p08: A, p09: A,
        p10: A, p11: A, p12: A, p13: A, p14: A, p15: A, p16: A, p17: A, p18: A, p19: A,
        p20: A, p21: A, p22: A, p23: A, p24: A, p25: A, p26: A, p27: A, p28: A, p29: String
    ): String {
        return p29
    }
}

fun box(): String {
    val a = A()
    val foo = ((A::class.java).kotlin).getMemberByName("foo")
    val o = foo.call(
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        "O"
    ) as String
    val k = foo.call(
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        a,
        "K"
    ) as String

    return o + k
}
