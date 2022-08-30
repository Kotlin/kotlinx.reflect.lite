/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.suspendFun

import kotlin.reflect.jvm.javaType
import kotlin.test.assertEquals

interface A {
    suspend fun f(param: String): MutableList<in String>
}

fun box(): String {
    val type = A::class.members.single { it.name == "f" }.returnType.javaType
    assertEquals("java.util.List<? super java.lang.String>", type.toString())

    return "OK"
}
