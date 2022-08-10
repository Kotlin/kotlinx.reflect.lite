package tests.mapping.privateProperty

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class K(private var value: Long)

fun box(): String {
    val p = (K::class.java.kDeclarationContainer).getMemberByName("value") as KMutableProperty1<K, Long>

    assertNotNull(p.javaField, "Fail p field")
    assertNull(p.javaGetter, "Fail p getter")
    assertNull(p.javaSetter, "Fail p setter")

    return "OK"
}
