/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.boundExtensionPropertyAcessorWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

// WA for the use-case from this test: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/callBy/boundExtensionPropertyAcessor.kt
/**
 * val String.plusK: String
 *   get() = this + "K"
 *
 * ("O"::plusK).getter.callBy(mapOf())
 */

val String.plusK: String
    get() = this + "K"

fun box(): String {
    val plusK = Class.forName("tests.callBy.boundExtensionPropertyAcessorWA.BoundExtensionPropertyAcessorWAKt").kotlinPackage.
        getMemberByName("plusK") as KProperty1<String, String>
    return plusK.getter.callBy(mapOf(
            plusK.parameters[0] to "O"
        )
    )
}
