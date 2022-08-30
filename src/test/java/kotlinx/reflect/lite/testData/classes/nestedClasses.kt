package tests.classes.nestedClasses

import kotlin.test.assertEquals
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*


class A {
    companion object {}
    inner class Inner
    class Nested
    private class PrivateNested
}

fun nestedNames(c: KClass<*>) = c.nestedClasses.map { it.simpleName ?: throw AssertionError("Unnamed class: ${it}") }.sorted()

fun box(): String {
    // Kotlin class without nested classes
    assertEquals(emptyList<String>(), nestedNames(A.Inner::class.java.kotlin))
    // Kotlin class with nested classes
    assertEquals(listOf("Companion", "Inner", "Nested", "PrivateNested"),
        nestedNames((A::class.java.kotlin)))

    // Java class without nested classes
    assertEquals(emptyList<String>(), nestedNames(Error::class.java.kotlin))
    // Java interface with nested classes
    assertEquals(listOf("Entry"), nestedNames(java.util.Map::class.java.kotlin))
    // Java class with nested classes
    assertEquals(listOf("SimpleEntry", "SimpleImmutableEntry"), nestedNames(java.util.AbstractMap::class.java.kotlin))

    // Built-ins
    assertEquals(emptyList<String>(), nestedNames(Array<Any>::class.java.kotlin))
    assertEquals(emptyList<String>(), nestedNames(CharSequence::class.java.kotlin))
    assertEquals(listOf("Companion"), nestedNames(String::class.java.kotlin))

    assertEquals(emptyList<String>(), nestedNames(Collection::class.java.kotlin))
    assertEquals(emptyList<String>(), nestedNames(MutableCollection::class.java.kotlin))
    assertEquals(emptyList<String>(), nestedNames(List::class.java.kotlin))
    assertEquals(emptyList<String>(), nestedNames(MutableList::class.java.kotlin))
    assertEquals(listOf("Entry"), nestedNames(Map::class.java.kotlin))
    assertEquals(emptyList<String>(), nestedNames(Map.Entry::class.java.kotlin))
    assertEquals(emptyList<String>(), nestedNames(MutableMap.MutableEntry::class.java.kotlin))

    // TODO: should be MutableEntry. Currently we do not distinguish between Map and MutableMap.
    assertEquals(listOf("Entry"), nestedNames(MutableMap::class.java.kotlin))

    // Primitives
    for (primitive in listOf(Byte::class, Double::class, Float::class, Int::class, Long::class, Short::class, Char::class, Boolean::class)) {
        assertEquals(listOf("Companion"), nestedNames(primitive.java.kotlin))
    }

    // Primitive arrays
    for (primitiveArray in listOf(
        ByteArray::class, DoubleArray::class, FloatArray::class, IntArray::class,
        LongArray::class, ShortArray::class, CharArray::class, BooleanArray::class
    )) {
        assertEquals(emptyList<String>(), nestedNames(primitiveArray.java.kotlin))
    }

    return "OK"
}
