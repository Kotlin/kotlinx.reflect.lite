package tests.callBy.boundExtensionPropertyAcessorWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
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
    val plusK = Class.forName("tests.callBy.boundExtensionPropertyAcessorWA.BoundExtensionPropertyAcessorWAKt").kDeclarationContainer.
        getMemberByName("plusK") as KProperty1<String, String>
    return plusK.getter.callBy(mapOf(
            plusK.parameters[0] to "O"
        )
    )
}
