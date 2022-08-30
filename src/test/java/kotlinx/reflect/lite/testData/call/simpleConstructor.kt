/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.simpleConstructor

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

class A(val result: String)

fun box(): String {
    val aCons = ((A::class.java).kotlin).constructors.first()
    val a = aCons.call("OK")
    return a.result
}
