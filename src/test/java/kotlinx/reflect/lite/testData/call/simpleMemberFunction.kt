/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.simpleMemberFunction

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

class A {
    fun foo(x: Int, y: Int) = x + y
}

fun box(): String {
    val foo = ((A::class.java).kotlin).getMemberByName("foo")

    val x = foo.call(A(), 42, 239)
    if (x != 281) return "Fail: $x"

    try {
        (A::foo).call()
        return "Fail: no exception"
    } catch (e: Exception) {
    }

    return "OK"
}
