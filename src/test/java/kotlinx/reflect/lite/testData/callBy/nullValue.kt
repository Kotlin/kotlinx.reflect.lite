/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.nullValue

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertNull

fun foo(x: String? = "Fail") {
    assertNull(x)
}

fun box(): String {
    val foo = Class.forName("tests.callBy.nullValue.NullValueKt").kotlinPackage.getMemberByName("foo")
    foo.callBy(mapOf(foo.parameters.single() to null))
    return "OK"
}
