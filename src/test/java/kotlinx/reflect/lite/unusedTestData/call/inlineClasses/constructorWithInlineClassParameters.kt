/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.inlineClasses.constructorWithInlineClassParameters

import kotlin.test.assertEquals

inline class Z(val x: Int)

class Outer(val z1: Z) {
    inner class Inner(val z2: Z) {
        val test = "$z1 $z2"
    }
}

inline class InlineOuter(val z1: Z) {
    //@Suppress("INNER_CLASS_INSIDE_INLINE_CLASS")
//    inner class Inner(val z2: Z) {
//        val test = "$z1 $z2"
//    }
}

fun box(): String {
    assertEquals(Z(1), ::Outer.call(Z(1)).z1)
    assertEquals("Z(x=1) Z(x=2)", Outer::Inner.call(Outer(Z(1)), Z(2)).test)
    assertEquals("Z(x=1) Z(x=3)", Outer(Z(1))::Inner.call(Z(3)).test)
//    assertEquals("Z(x=1) Z(x=2)", InlineOuter::Inner.call(InlineOuter(Z(1)), Z(2)).test)
//    assertEquals("Z(x=1) Z(x=3)", InlineOuter(Z(1))::Inner.call(Z(3)).test)

    return "OK"
}
