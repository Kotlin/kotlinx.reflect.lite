/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.isOptionalOverridesParameters

import kotlin.test.*

open class A {
    open fun foo(x: Int, y: Int = 1) {}
}

class B : A() {
    override fun foo(x: Int, y: Int) {}
}

class C : A()


fun Int.extFun() {}

fun box(): String {
    assertEquals(listOf(false, false, true), A::foo.parameters.map { it.isOptional })
    assertEquals(listOf(false, false, true), B::foo.parameters.map { it.isOptional })
    assertEquals(listOf(false, false, true), C::foo.parameters.map { it.isOptional })

    assertFalse(Int::extFun.parameters.single().isOptional)

    return "OK"
}
