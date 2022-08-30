/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.overrideAnyWithPrimitive

import kotlin.reflect.jvm.*
import kotlin.test.*

interface I {
    fun foo(): Any
}

class A : I {
    override fun foo(): Int = 0
    fun bar(x: Long): Int = x.toInt()
}

fun box(): String {
    assertEquals(Integer::class.java, A::foo.returnType.javaType)
    assertNotEquals(Integer.TYPE, A::foo.returnType.javaType)

    assertNotEquals(Integer::class.java, A::bar.returnType.javaType)
    assertEquals(Integer.TYPE, A::bar.returnType.javaType)

    assertEquals(java.lang.Long.TYPE, A::bar.parameters.last().type.javaType)

    return "OK"
}
