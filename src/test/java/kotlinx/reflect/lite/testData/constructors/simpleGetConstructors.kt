/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.constructors.simpleGetConstructors

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.*
import java.util.Collections
import kotlin.test.assertEquals
import kotlin.test.assertTrue

open class A private constructor(x: Int) {
    public constructor(s: String): this(s.length)
    constructor(): this("")
}

class B : A("")

class C {
    class Nested
    inner class Inner
}

fun box(): String {
    assertEquals(3, (A::class.java.kotlin).constructors.size)
    assertEquals(1, (B::class.java.kotlin).constructors.size)

    assertTrue(Collections.disjoint(
        (A::class.java.kotlin).members,
        (A::class.java.kotlin).constructors
    ))
    assertTrue(Collections.disjoint(
        (B::class.java.kotlin).members,
        (B::class.java.kotlin).constructors
    ))

    assertEquals(1, (C.Nested::class.java.kDeclarationContainer as KClass<C.Nested>).constructors.size)
    assertEquals(1, (C.Inner::class.java.kDeclarationContainer as KClass<C.Inner>).constructors.size)

    return "OK"
}
