/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.functionFromStdlibSingleFileFacade

// KT-12630 KotlinReflectionInternalError on referencing some functions from stdlib


import kotlin.test.*

fun box(): String {
    val hashCode = Any?::hashCode
    assertEquals("fun kotlin.Any?.hashCode(): kotlin.Int", hashCode.toString())

    return "OK"
}
