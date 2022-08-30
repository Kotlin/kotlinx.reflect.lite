/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */



package tests.methodsFromAny.typeToStringInnerGeneric

import kotlin.test.assertEquals

class A<T1> {
    inner class B<T2, T3> {
        inner class C<T4>
    }
}

fun foo(): A<Int>.B<Double, Float>.C<Long> = null!!

fun box(): String {
    assertEquals("tests.methodsFromAny.typeToStringInnerGeneric.A<kotlin.Int>.B<kotlin.Double, kotlin.Float>.C<kotlin.Long>", ::foo.returnType.toString())
    return "OK"
}
