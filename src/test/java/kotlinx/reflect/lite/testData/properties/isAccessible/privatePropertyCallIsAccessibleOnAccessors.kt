package tests.properties.privatePropertyCallIsAccessibleOnAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class A(private var foo: String)

fun box(): String {
    val a = A("")
    val foo = (A::class.java).kDeclarationContainer.getMemberByName("foo") as KMutableProperty1<A, String>

    assertTrue(!foo.isAccessible)
    assertTrue(!foo.getter.isAccessible)
    assertTrue(!foo.setter.isAccessible)

    val setter = foo.setter
    setter.isAccessible = true
    assertTrue(setter.isAccessible)
    assertTrue(foo.setter.isAccessible)

    // After we invoked isAccessible on a setter, the underlying field and thus the getter are also accessible
    assertTrue(foo.isAccessible)
    assertTrue(foo.getter.isAccessible)
    setter.call(a, "A")
    assertEquals("A", foo.getter.call(a))

    setter.isAccessible = false
    assertFalse(setter.isAccessible)
    assertFalse(foo.setter.isAccessible)
    assertFalse(foo.getter.isAccessible)
    assertFalse(foo.isAccessible)

    val getter = foo.getter
    getter.isAccessible = true
    assertTrue(setter.isAccessible)
    assertTrue(foo.setter.isAccessible)
    assertTrue(foo.isAccessible)
    assertTrue(foo.getter.isAccessible)
    assertTrue(getter.isAccessible)

    return "OK"
}
