/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.propertyEqualsHashCode

import kotlin.test.*

val top = 42
var top2 = -23

val Int.intExt: Int get() = this
val Char.charExt: Int get() = this.toInt()

class A(var mem: String)
class B(var mem: String)


fun checkEqual(x: Any, y: Any) {
    assertEquals(x, y)
    assertEquals(x.hashCode(), y.hashCode(), "Elements are equal but their hash codes are not: ${x.hashCode()} != ${y.hashCode()}")
}

fun box(): String {
    checkEqual(::top, ::top)
    checkEqual(::top2, ::top2)
    checkEqual(Int::intExt, Int::intExt)
    checkEqual(A::mem, A::mem)

    assertFalse(::top == ::top2)
    assertFalse(Int::intExt == Char::charExt)
    assertFalse(A::mem == B::mem)

    return "OK"
}
