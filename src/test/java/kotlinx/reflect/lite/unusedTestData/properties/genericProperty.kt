/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */


package tests.properties.genericProperty

import kotlin.test.assertEquals

data class Box<T>(val element: T)

fun box(): String {
    val p = Box<String>::element
    assertEquals("val tests.properties.genericProperty.Box<T>.element: T", p.toString())
    return p.call(Box("OK"))
}
