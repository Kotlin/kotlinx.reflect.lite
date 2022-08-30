/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.functions.simpleNames

import kotlin.test.assertEquals

fun foo() {}

class A {
    fun bar() = ""
}

fun Int.baz() = this

fun box(): String {
    assertEquals("foo", ::foo.name)
    assertEquals("bar", A::bar.name)
    assertEquals("baz", Int::baz.name)
    return "OK"
}
