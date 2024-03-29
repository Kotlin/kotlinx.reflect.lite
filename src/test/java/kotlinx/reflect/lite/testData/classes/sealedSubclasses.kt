/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classes.sealedSubclasses

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

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
    assertEquals(listOf("TL1", "TL2"),
        sealedSubclassNames((SealedClassWithTopLevelSubclasses::class.java.kDeclarationContainer as KClass<SealedClassWithTopLevelSubclasses>)))
    assertEquals(listOf("N1", "N2"),
        sealedSubclassNames((SealedClassWithNestedSubclasses::class.java.kDeclarationContainer as KClass<SealedClassWithNestedSubclasses>)))
    assertEquals(emptyList(),
        sealedSubclassNames((SealedClassWithNoSubclasses::class.java.kDeclarationContainer as KClass<SealedClassWithNoSubclasses>)))

    assertEquals(emptyList(), sealedSubclassNames(String::class.java.kotlin))
    assertEquals(emptyList(), sealedSubclassNames(Thread::class.java.kotlin))
    assertEquals(emptyList(), sealedSubclassNames(FloatArray::class.java.kotlin))

    return "OK"
}
