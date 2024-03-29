/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.simpleMemberFunciton

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

class A {
    fun foo(x: Int): String {
        assert(x == 42) { return "42" }
        return "OK"
    }
}

fun box(): String {
    val foo = (A::class.java).kotlin.getMemberByName("foo") as KFunction<String>
    return foo.callBy(mapOf(foo.parameters[0] to A(), foo.parameters[1] to 42))
}
