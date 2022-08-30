package tests.classes.qualifiedNameOfStandardClasses

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

import kotlin.test.assertEquals

fun box(): String {
    assertEquals("kotlin.Any", Any::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.String", String::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.CharSequence", CharSequence::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.Number", Number::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.Int", Int::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.Long", Long::class.java.kotlin.qualifiedName)

    assertEquals("kotlin.Array", Array<Any>::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.Array", Array<IntArray>::class.java.kotlin.qualifiedName)
    assertEquals(
        "kotlin.Array",
        Array<Array<String>>::class.java.kotlin.qualifiedName
    )

    assertEquals("kotlin.IntArray", IntArray::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.DoubleArray", DoubleArray::class.java.kotlin.qualifiedName)

    assertEquals("kotlin.Int.Companion", Int.Companion::class.java.kotlin.qualifiedName)
    assertEquals(
        "kotlin.Double.Companion",
        Double.Companion::class.java.kotlin.qualifiedName
    )
    assertEquals(
        "kotlin.Char.Companion",
        Char.Companion::class.java.kotlin.qualifiedName
    )

    assertEquals("kotlin.ranges.IntRange", IntRange::class.java.kotlin.qualifiedName)

    assertEquals("kotlin.collections.List", List::class.java.kotlin.qualifiedName)
    assertEquals(
        "kotlin.collections.Map.Entry",
        Map.Entry::class.java.kotlin.qualifiedName
    )

////    // TODO: KT-11754
    assertEquals(
        "kotlin.collections.List",
        MutableList::class.java.kotlin.qualifiedName
    )
    assertEquals(
        "kotlin.collections.Map.Entry",
        MutableMap.MutableEntry::class.java.kotlin.qualifiedName
    )

    assertEquals("kotlin.Function0", Function0::class.java.kotlin.qualifiedName)
    assertEquals("kotlin.Function1", Function1::class.java.kotlin.qualifiedName)
    assertEquals(
        "kotlin.Function5",
        Function5::class.java.kotlin.qualifiedName
    )
    assertEquals(
        "kotlin.jvm.functions.FunctionN",
        Function42::class.java.kotlin.qualifiedName
    )

    return "OK"
}
