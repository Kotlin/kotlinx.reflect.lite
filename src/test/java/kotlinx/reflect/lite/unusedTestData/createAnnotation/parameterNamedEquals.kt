/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.createAnnotation.parameterNamedEquals

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

annotation class Anno(val equals: Boolean)

fun box(): String {
    val t = Anno::class.constructors.single().call(true)
    val f = Anno::class.constructors.single().call(false)
    assertEquals(true, t.equals)
    assertEquals(false, f.equals)
    assertNotEquals(t, f)
    return "OK"
}
