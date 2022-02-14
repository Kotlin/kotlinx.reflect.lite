package tests.classes.nestedClasses

import kotlin.test.assertEquals
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*

class A {
    companion object {}
    inner class Inner
    class Nested
    private class PrivateNested
}

fun nestedNames(c: KClass<*>) = c.nestedClasses.map { it.simpleName ?: throw AssertionError("Unnamed class: ${it}") }.sorted()

fun box(): String {
    // Kotlin class without nested classes
    assertEquals(emptyList<String>(), nestedNames(A.Inner::class.java.toLiteKClass()))
    // Kotlin class with nested classes
    assertEquals(listOf("Companion", "Inner", "Nested", "PrivateNested"), nestedNames(A::class.java.toLiteKClass()))

    // Java class without nested classes
    //assertEquals(emptyList<String>(), nestedNames(Error::class.java.toLiteKClass()))
    // Java interface with nested classes
    //assertEquals(listOf("Entry"), nestedNames(java.util.Map::class.java.toLiteKClass()))
    // Java class with nested classes
    //assertEquals(listOf("SimpleEntry", "SimpleImmutableEntry"), nestedNames(java.util.AbstractMap::class.java.toLiteKClass()))

    // Built-ins
//    assertEquals(emptyList<String>(), nestedNames(Array<Any>::class.java.toLiteKClass()))
//    assertEquals(emptyList<String>(), nestedNames(CharSequence::class.java.toLiteKClass()))
//    assertEquals(listOf("Companion"), nestedNames(String::class.java.toLiteKClass()))
//
//    assertEquals(emptyList<String>(), nestedNames(Collection::class.java.toLiteKClass()))
//    assertEquals(emptyList<String>(), nestedNames(MutableCollection::class.java.toLiteKClass()))
//    assertEquals(emptyList<String>(), nestedNames(List::class.java.toLiteKClass()))
//    assertEquals(emptyList<String>(), nestedNames(MutableList::class.java.toLiteKClass()))
//    assertEquals(listOf("Entry"), nestedNames(Map::class.java.toLiteKClass()))
//    assertEquals(emptyList<String>(), nestedNames(Map.Entry::class.java.toLiteKClass()))
//    assertEquals(emptyList<String>(), nestedNames(MutableMap.MutableEntry::class.java.toLiteKClass()))

    // TODO: should be MutableEntry. Currently we do not distinguish between Map and MutableMap.
    //assertEquals(listOf("Entry"), nestedNames(MutableMap::class.java.toLiteKClass()))

    // Primitives
//    for (primitive in listOf(Byte::class, Double::class, Float::class, Int::class, Long::class, Short::class, Char::class, Boolean::class)) {
//        assertEquals(listOf("Companion"), nestedNames(primitive.java.toLiteKClass()))
//    }

    // Primitive arrays
//    for (primitiveArray in listOf(
//        ByteArray::class, DoubleArray::class, FloatArray::class, IntArray::class,
//        LongArray::class, ShortArray::class, CharArray::class, BooleanArray::class
//    )) {
//        assertEquals(emptyList<String>(), nestedNames(primitiveArray.java.toLiteKClass()))
//    }

    return "OK"
}