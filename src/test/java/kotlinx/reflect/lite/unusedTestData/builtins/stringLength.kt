/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.builtins.stringLength

import kotlin.test.assertEquals

fun box(): String {
    String::class.members
    assertEquals(2, String::length.call("OK"))
    return "OK"
}
