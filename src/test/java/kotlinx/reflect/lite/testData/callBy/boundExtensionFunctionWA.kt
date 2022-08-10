package tests.callBy.boundExtensionFunctionWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*

// WA for the use-case from this test: https://github.com/Kotlin/kotlinx.reflect.lite/blob/mvicsokolova/dev/src/test/java/kotlinx/reflect/lite/unusedTestData/callBy/boundExtensionFunction.kt
/**
 * val sExtFun = "O"::extFun
 * sExtFun.callBy(mapOf(sExtFun.parameters[0] to "K"))
 */

fun String.extFun(k: String, s: String = "") = this + k + s

fun box(): String {
    val sExtFun = Class.forName("tests.callBy.boundExtensionFunctionWA.BoundExtensionFunctionWAKt").kDeclarationContainer.
        getMemberByName("extFun") as KFunction<String>
    return sExtFun.callBy(mapOf(
        sExtFun.parameters[0] to "O",
        sExtFun.parameters[1] to "K"
    ))
}
