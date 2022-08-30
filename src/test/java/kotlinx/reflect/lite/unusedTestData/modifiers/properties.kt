/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.properties

import kotlin.test.assertTrue
import kotlin.test.assertFalse

const val const = "const"
val nonConst = "nonConst"

class A {
    lateinit var lateinit: Unit
    var nonLateinit = Unit
}

fun box(): String {
    assertTrue(::const.isConst)
    assertFalse(::nonConst.isConst)

    assertTrue(A::lateinit.isLateinit)
    assertFalse(A::nonLateinit.isLateinit)

    return "OK"
}
