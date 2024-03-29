/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.jvmStatic.objectFunction

import kotlin.reflect.KFunction
import kotlin.reflect.jvm.*
import kotlin.test.assertEquals

object O {
    @JvmStatic
    fun foo(s: String): Int = s.length
}

fun box(): String {
    val foo = O::class.members.single { it.name == "foo" } as KFunction<*>

    val j = foo.javaMethod ?: return "Fail: no Java method found for O::foo"
    assertEquals(3, j.invoke(null, "abc"))

    val k = j.kotlinFunction ?: return "Fail: no Kotlin function found for Java method O::foo"
    assertEquals(3, k.call(O, "def"))

    return "OK"
}
