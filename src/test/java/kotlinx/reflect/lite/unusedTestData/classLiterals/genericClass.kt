/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classLiterals.genericClass

import kotlin.test.assertEquals

class Generic<K, V>

fun box(): String {
    val g = Generic::class
    assertEquals("Generic", g.simpleName)
    return "OK"
}
