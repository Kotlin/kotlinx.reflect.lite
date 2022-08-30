/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classes.primitiveKClassEquality

import kotlin.test.assertEquals
import kotlin.test.assertFalse

fun box(): String {
    val x = Int::class.javaPrimitiveType!!.kotlin
    val y = Int::class.javaObjectType.kotlin

    assertEquals(x, y)
    assertEquals(x.hashCode(), y.hashCode())
    assertFalse(x === y)

    return "OK"
}
