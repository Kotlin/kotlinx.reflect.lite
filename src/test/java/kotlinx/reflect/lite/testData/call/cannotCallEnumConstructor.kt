/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.cannotCallEnumConstructor

import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*


enum class E

fun box(): String {
    try {
        val c = ((E::class.java).kotlin).constructors.single()
        c.isAccessible = true
        c.call()
        return "Fail: constructing an enum class should not be allowed"
    } catch (e: Throwable) {
        return "OK"
    }
}
