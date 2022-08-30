/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.simpleTopLevelFunction

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

fun foo(result: String = "OK") = result

fun box(): String {
    val clazz = Class.forName("tests.callBy.simpleTopLevelFunction.SimpleTopLevelFunctionKt").kotlinPackage
    val foo = clazz.getMemberByName("foo") as KFunction<String>
    return foo.callBy(mapOf())
}
