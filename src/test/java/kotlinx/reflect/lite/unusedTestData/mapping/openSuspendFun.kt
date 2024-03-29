/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.openSuspendFun

import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.javaMethod
import kotlin.test.assertEquals

open class Aaa {
    suspend open fun aaa() {}
}

class Bbb {
    suspend fun bbb() {}
}

fun box(): String {
    val bbb = Bbb::class.declaredMemberFunctions.first { it.name == "bbb" }.javaMethod
    assertEquals("bbb", bbb!!.name)
    val aaa = Aaa::class.declaredMemberFunctions.first { it.name == "aaa" }.javaMethod
    assertEquals("aaa", aaa!!.name)
    return "OK"
}
