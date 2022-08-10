package tests.callBy.nullValue

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertNull

fun foo(x: String? = "Fail") {
    assertNull(x)
}

fun box(): String {
    val foo = Class.forName("tests.callBy.nullValue.NullValueKt").kotlinPackage.getMemberByName("foo")
    foo.callBy(mapOf(foo.parameters.single() to null))
    return "OK"
}
