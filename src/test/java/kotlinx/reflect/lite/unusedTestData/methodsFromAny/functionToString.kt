/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */



package tests.methodsFromAny.functionToString

import kotlin.test.assertEquals

fun top() = 42

fun String.ext(): Int = 0
fun IntRange?.ext2(): Array<Int?> = arrayOfNulls(0)

class A {
    fun mem(): String = ""
}

fun assertToString(s: String, x: Any) {
    assertEquals(s, x.toString())
}

fun box(): String {
    assertToString("fun top(): kotlin.Int", ::top)
    assertToString("fun kotlin.String.ext(): kotlin.Int", String::ext)
    assertToString("fun kotlin.ranges.IntRange?.ext2(): kotlin.Array<kotlin.Int?>", IntRange::ext2)
    assertToString("fun tests.methodsFromAny.functionToString.A.mem(): kotlin.String", A::mem)
    return "OK"
}
