package tests.classes.qualifiedNameOfStandardClasses

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlin.test.assertEquals

fun box(): String {
    assertEquals("kotlin.Any", (Any::class.java.kotlinClass as KClass<Any>).qualifiedName)
    assertEquals("kotlin.String", (String::class.java.kotlinClass as KClass<String>).qualifiedName)
    assertEquals("kotlin.CharSequence", (CharSequence::class.java.kotlinClass as KClass<CharSequence>).qualifiedName)
    assertEquals("kotlin.Number", (Number::class.java.kotlinClass as KClass<Number>).qualifiedName)
    assertEquals("kotlin.Int", (Int::class.java.kotlinClass as KClass<Int>).qualifiedName)
    assertEquals("kotlin.Long", (Long::class.java.kotlinClass as KClass<Long>).qualifiedName)

    assertEquals("kotlin.Array", (Array<Any>::class.java.kotlinClass as KClass<Array<Any>>).qualifiedName)
    assertEquals("kotlin.Array", (Array<IntArray>::class.java.kotlinClass as KClass<Array<IntArray>>).qualifiedName)
    assertEquals(
        "kotlin.Array",
        (Array<Array<String>>::class.java.kotlinClass as KClass<Array<Array<String>>>).qualifiedName
    )

    assertEquals("kotlin.IntArray", (IntArray::class.java.kotlinClass as KClass<IntArray>).qualifiedName)
    assertEquals("kotlin.DoubleArray", (DoubleArray::class.java.kotlinClass as KClass<DoubleArray>).qualifiedName)

    assertEquals("kotlin.Int.Companion", (Int.Companion::class.java.kotlinClass as KClass<Int.Companion>).qualifiedName)
    assertEquals(
        "kotlin.Double.Companion",
        (Double.Companion::class.java.kotlinClass as KClass<Double.Companion>).qualifiedName
    )
    assertEquals(
        "kotlin.Char.Companion",
        (Char.Companion::class.java.kotlinClass as KClass<Char.Companion>).qualifiedName
    )

    assertEquals("kotlin.ranges.IntRange", (IntRange::class.java.kotlinClass as KClass<IntRange>).qualifiedName)

    assertEquals("kotlin.collections.List", (List::class.java.kotlinClass as KClass<List<*>>).qualifiedName)
    assertEquals(
        "kotlin.collections.Map.Entry",
        (Map.Entry::class.java.kotlinClass as KClass<Map.Entry<*, *>>).qualifiedName
    )

    // TODO: KT-11754
    assertEquals(
        "kotlin.collections.List",
        (MutableList::class.java.kotlinClass as KClass<MutableList<*>>).qualifiedName
    )
    assertEquals(
        "kotlin.collections.Map.Entry",
        (MutableMap.MutableEntry::class.java.kotlinClass as KClass<MutableMap.MutableEntry<*, *>>).qualifiedName
    )

    assertEquals("kotlin.Function0", (Function0::class.java.kotlinClass as KClass<Function0<*>>).qualifiedName)
    assertEquals("kotlin.Function1", (Function1::class.java.kotlinClass as KClass<Function1<*, *>>).qualifiedName)
    assertEquals(
        "kotlin.Function5",
        (Function5::class.java.kotlinClass as KClass<Function5<*, *, *, *, *, *>>).qualifiedName
    )
    assertEquals(
        "kotlin.jvm.functions.FunctionN",
        (Function42::class.java.kotlinClass as KClass<Function42<*, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *, *>>).qualifiedName
    )

    return "OK"
}
