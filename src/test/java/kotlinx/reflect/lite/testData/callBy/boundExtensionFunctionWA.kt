/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.boundExtensionFunctionWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

// WA for the use-case from this test: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/callBy/boundExtensionFunction.kt
/**
 * val sExtFun = "O"::extFun
 * sExtFun.callBy(mapOf(sExtFun.parameters[0] to "K"))
 */

fun String.extFun(k: String, s: String = "") = this + k + s

fun box(): String {
    val sExtFun = Class.forName("tests.callBy.boundExtensionFunctionWA.BoundExtensionFunctionWAKt").kotlinPackage.
        getMemberByName("extFun") as KFunction<String>
    return sExtFun.callBy(mapOf(
        sExtFun.parameters[0] to "O",
        sExtFun.parameters[1] to "K"
    ))
}
