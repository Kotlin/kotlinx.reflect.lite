/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.functions.functionFromStdlib

import kotlin.reflect.KFunction1
import kotlin.reflect.jvm.isAccessible

fun doStuff(fn: KFunction1<String, String>) = fn.call("oK")

fun box(): String {
    return doStuff(String::capitalize.apply { isAccessible = true })
}
