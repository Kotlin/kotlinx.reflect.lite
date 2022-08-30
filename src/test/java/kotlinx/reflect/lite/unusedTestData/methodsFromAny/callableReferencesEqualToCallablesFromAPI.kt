/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.callableReferencesEqualToCallablesFromAPI

import kotlin.reflect.*
import kotlin.test.assertEquals

class A {
    fun foo() = "foo"
    val bar = "bar"
}

fun checkEqual(x: Any, y: Any) {
    assertEquals(x, y)
    assertEquals(y, x)
    assertEquals(x.hashCode(), y.hashCode())
}

fun box(): String {
    checkEqual(A::foo, A::class.members.single { it.name == "foo" })
    checkEqual(A::bar, A::class.members.single { it.name == "bar" })

    return "OK"
}
