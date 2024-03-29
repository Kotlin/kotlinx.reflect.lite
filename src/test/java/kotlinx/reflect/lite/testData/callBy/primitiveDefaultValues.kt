/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.primitiveDefaultValues

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun primitives(
        boolean: Boolean = true,
        character: Char = 'z',
        byte: Byte = 5.toByte(),
        short: Short = (-5).toShort(),
        int: Int = 2000000000,
        float: Float = -2.72f,
        long: Long = 1000000000000000000L,
        double: Double = 3.14159265359
) {
    assertEquals(true, boolean)
    assertEquals('z', character)
    assertEquals(5.toByte(), byte)
    assertEquals((-5).toShort(), short)
    assertEquals(2000000000, int)
    assertEquals(-2.72f, float)
    assertEquals(1000000000000000000L, long)
    assertEquals(3.14159265359, double)
}

fun box(): String {
    val clazz = Class.forName("tests.callBy.primitiveDefaultValues.PrimitiveDefaultValuesKt").kotlinPackage
    val primitives = clazz.getMemberByName("primitives")
    primitives.callBy(mapOf())
    return "OK"
}
