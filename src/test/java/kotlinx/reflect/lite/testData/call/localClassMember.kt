/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.localClassMember

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

fun box(): String {
    class Local {
        fun result(s: String) = s
    }

    val localCons = (Local::class.java).kotlin.getPrimaryConstructor()
    val result = (Local::class.java).kotlin.getMemberByName("result") as KFunction<String>
    return result.call(localCons.call(), "OK")
}
