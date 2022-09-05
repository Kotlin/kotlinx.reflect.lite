/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.extensionFunctionWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*


fun String.foo(x: String) = this + x
fun String?.bar(x: String) = x

fun box(): String {
        val clazz = Class.forName("tests.call.bound.extensionFunctionWA.ExtensionFunctionWAKt").kotlinPackage
        val foo = clazz.getMemberByName("foo") as KFunction<String>
        val bar = clazz.getMemberByName("bar") as KFunction<String>
        return foo.call("", "O") + bar.call(null, "K")
}

