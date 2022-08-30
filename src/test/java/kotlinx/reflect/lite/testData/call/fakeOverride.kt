/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.fakeOverride

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

open class A {
    fun foo() = "OK"
}

class B : A()

fun box(): String {
    val foo = (B::class.java).kotlin.getMemberByName("foo")
    return foo.call(B()) as String
}
