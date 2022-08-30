/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classLiterals.annotationClassLiteral

import kotlin.test.assertEquals

fun box(): String {
    assertEquals("Deprecated", Deprecated::class.simpleName)

    return "OK"
}
