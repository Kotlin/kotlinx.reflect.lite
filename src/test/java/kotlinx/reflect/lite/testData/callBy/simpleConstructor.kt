/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.simpleConstructor

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.impl.*

class A(val result: String = "OK")

fun box(): String {
    val aCons = (A::class.java.kotlin).constructors.first()
    return aCons.callBy(mapOf()).result
}
