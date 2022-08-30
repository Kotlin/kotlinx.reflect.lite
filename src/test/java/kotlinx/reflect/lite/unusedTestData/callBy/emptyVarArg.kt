/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.emptyVarArg

import kotlin.test.assertEquals

fun join(vararg strings: String) = strings.toList().joinToString("")

fun sum(vararg bytes: Byte) = bytes.toList().fold(0) { acc, el -> acc + el }

fun box(): String {
    val j = ::join
    val s = ::sum
    assertEquals("", j.callBy(emptyMap()))
    assertEquals(0, s.callBy(emptyMap()))
    return "OK"
}
