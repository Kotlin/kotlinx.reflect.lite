/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.constructors.constructorName

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.*
import kotlin.test.assertEquals

class A

fun box(): String {
    assertEquals("<init>", (A::class.java.kotlin).constructors.first().name)
    return "OK"
}
