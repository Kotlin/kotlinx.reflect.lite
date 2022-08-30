
/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// KT-13700 Exception obtaining descriptor for property reference

package tests.properties.genericOverriddenProperty

import kotlin.test.assertEquals

interface H<T> {
    val parent : T?
}

interface A : H<A>

fun box(): String {
    assertEquals("tests.properties.genericOverriddenProperty.A?", A::parent.returnType.toString())
    assertEquals("T?", H<A>::parent.returnType.toString())

    return "OK"
}
