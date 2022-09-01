/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.classModality

import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class FinalClass {
    companion object Companion
}
open class OpenClass
abstract class AbstractClass
interface Interface
enum class EnumClass
enum class EnumClassWithAbstractMember { ; abstract fun foo() }
annotation class AnnotationClass
object Object

fun box(): String {
    assertTrue(FinalClass::class.java.kotlin.isFinal)
    assertFalse(FinalClass::class.java.kotlin.isOpen)
    assertFalse(FinalClass::class.java.kotlin.isAbstract)

    assertTrue(FinalClass.Companion::class.java.kotlin.isFinal)
    assertFalse(FinalClass.Companion::class.java.kotlin.isOpen)
    assertFalse(FinalClass.Companion::class.java.kotlin.isAbstract)

    assertFalse(OpenClass::class.java.kotlin.isFinal)
    assertTrue(OpenClass::class.java.kotlin.isOpen)
    assertFalse(OpenClass::class.java.kotlin.isAbstract)

    assertFalse(AbstractClass::class.java.kotlin.isFinal)
    assertFalse(AbstractClass::class.java.kotlin.isOpen)
    assertTrue(AbstractClass::class.java.kotlin.isAbstract)

    assertFalse(Interface::class.java.kotlin.isFinal)
    assertFalse(Interface::class.java.kotlin.isOpen)
    assertTrue(Interface::class.java.kotlin.isAbstract)

    assertTrue(EnumClass::class.java.kotlin.isFinal)
    assertFalse(EnumClass::class.java.kotlin.isOpen)
    assertFalse(EnumClass::class.java.kotlin.isAbstract)

    assertTrue(EnumClassWithAbstractMember::class.java.kotlin.isFinal)
    assertFalse(EnumClassWithAbstractMember::class.java.kotlin.isOpen)
    assertFalse(EnumClassWithAbstractMember::class.java.kotlin.isAbstract)

    // Note that unlike in JVM, annotation classes are final in Kotlin
    assertTrue(AnnotationClass::class.java.kotlin.isFinal)
    assertFalse(AnnotationClass::class.java.kotlin.isOpen)
    assertFalse(AnnotationClass::class.java.kotlin.isAbstract)

    assertTrue(Object::class.java.kotlin.isFinal)
    assertFalse(Object::class.java.kotlin.isOpen)
    assertFalse(Object::class.java.kotlin.isAbstract)

    return "OK"
}
