/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.objectMemberReferences

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

object Host {
    fun foo(i: Int, s: String) {}
}

fun box(): String {
    val fooParams = (Host::class.java).kotlin.getMemberByName("foo").parameters

    assertEquals(3, fooParams.size)

    assertEquals(null, fooParams[0].name)
    assertEquals("i", fooParams[1].name)
    assertEquals(Int::class.java, fooParams[1].type.javaType)

    assertEquals("s", fooParams[2].name)
    assertEquals(String::class.java, fooParams[2].type.javaType)

    return "OK"
}
