package tests.classes.qualifiedNameOfStandardClasses

import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun box(): String {
    assertEquals("kotlin.Any", Any::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.String", String::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.CharSequence", CharSequence::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Number", Number::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Int", Int::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Long", Long::class.java.toLiteKClass().qualifiedName)

    assertEquals("kotlin.Array", Array<Any>::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Array", Array<IntArray>::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Array", Array<Array<String>>::class.java.toLiteKClass().qualifiedName)

    assertEquals("kotlin.IntArray", IntArray::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.DoubleArray", DoubleArray::class.java.toLiteKClass().qualifiedName)

    assertEquals("kotlin.Int.Companion", Int.Companion::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Double.Companion", Double.Companion::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Char.Companion", Char.Companion::class.java.toLiteKClass().qualifiedName)

    assertEquals("kotlin.ranges.IntRange", IntRange::class.java.toLiteKClass().qualifiedName)

    assertEquals("kotlin.collections.List", List::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.collections.Map.Entry", Map.Entry::class.java.toLiteKClass().qualifiedName)

    // TODO: KT-11754
    assertEquals("kotlin.collections.List", MutableList::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.collections.Map.Entry", MutableMap.MutableEntry::class.java.toLiteKClass().qualifiedName)

    assertEquals("kotlin.Function0", Function0::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Function1", Function1::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.Function5", Function5::class.java.toLiteKClass().qualifiedName)
    assertEquals("kotlin.jvm.functions.FunctionN", Function42::class.java.toLiteKClass().qualifiedName)

    return "OK"
}