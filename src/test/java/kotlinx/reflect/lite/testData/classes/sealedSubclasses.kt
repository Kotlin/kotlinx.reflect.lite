package tests.classes.sealedSubclasses

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

// --

sealed class SealedClassWithTopLevelSubclasses {
    class NotASealedSubclass : TL2()
}
object TL1 : SealedClassWithTopLevelSubclasses()
open class TL2 : SealedClassWithTopLevelSubclasses()

// --

sealed class SealedClassWithNestedSubclasses {
    data class N1(val x: Unit) : SealedClassWithNestedSubclasses()
    object N2 : SealedClassWithNestedSubclasses()
}

// --

sealed class SealedClassWithNoSubclasses

// --

fun sealedSubclassNames(c: KClass<*>) = c.sealedSubclasses.map { it.simpleName ?: throw AssertionError("Unnamed class: ${it}") }.sorted()

fun box(): String {
    assertEquals(listOf("TL1", "TL2"), sealedSubclassNames(SealedClassWithTopLevelSubclasses::class.java.toLiteKClass()))
    assertEquals(listOf("N1", "N2"), sealedSubclassNames(SealedClassWithNestedSubclasses::class.java.toLiteKClass()))
    assertEquals(emptyList(), sealedSubclassNames(SealedClassWithNoSubclasses::class.java.toLiteKClass()))

//    assertEquals(emptyList(), sealedSubclassNames(String::class.java.toLiteKClass()))
//    assertEquals(emptyList(), sealedSubclassNames(Thread::class.java.toLiteKClass()))
//    assertEquals(emptyList(), sealedSubclassNames(FloatArray::class.java.toLiteKClass()))

    return "OK"
}