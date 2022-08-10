package tests.mapping.extensionProperty

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class K(var value: Long)

var K.ext: Double
    get() = value.toDouble()
    set(value) {
        this.value = value.toLong()
    }

val fileFacadeClass = object {}::class.java.enclosingClass

fun box(): String {
    val clazz = Class.forName("tests.mapping.extensionProperty.ExtensionPropertyKt").kDeclarationContainer
    val p = clazz.getMemberByName("ext") as KMutableProperty1<K, Double>

    val getter = p.javaGetter!!
    val setter = p.javaSetter!!

    assertEquals(getter, fileFacadeClass.getMethod("getExt", K::class.java))
    assertEquals(setter, fileFacadeClass.getMethod("setExt", K::class.java, Double::class.java))

    val k = K(42L)
    assert(getter.invoke(null, k) == 42.0) { "Fail k getter" }
    setter.invoke(null, k, -239.0)
    assert(getter.invoke(null, k) == -239.0) { "Fail k setter" }

    return "OK"
}
