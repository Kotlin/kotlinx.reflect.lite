/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.parametersEqualsHashCode

import kotlin.test.*

class A {
    fun foo(s: String, x: Int) {}
    fun bar(x: Int) {}
    val baz = 42
}

fun box(): String {
    // Dispatch receiver parameters of different callables are not equal
    assertNotEquals(A::foo.parameters[0], A::bar.parameters[0])
    assertNotEquals(A::foo.parameters[0], A::baz.parameters[0])

    assertNotEquals(A::foo.parameters[1], A::bar.parameters[1])
    assertNotEquals(A::foo.parameters[1], A::foo.parameters[2])
    assertNotEquals(A::bar.parameters[1], A::foo.parameters[2])

    assertEquals(A::foo.parameters[0], A::foo.parameters[0])
    assertEquals(A::foo.parameters[0].hashCode(), A::foo.parameters[0].hashCode())
    assertEquals(A::foo.parameters[1], A::foo.parameters[1])
    assertEquals(A::foo.parameters[1].hashCode(), A::foo.parameters[1].hashCode())
    assertEquals(A::bar.parameters[0], A::bar.parameters[0])
    assertEquals(A::bar.parameters[0].hashCode(), A::bar.parameters[0].hashCode())

    return "OK"
}
