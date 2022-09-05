/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.memberFunctionWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

class C(val k: String) {
    fun foo(s: String) = s + k
}

fun box(): String {
    val foo = C::class.java.kotlin.getMemberByName("foo") as KFunction<String>
    return foo.call(C("K"), "O")
}

