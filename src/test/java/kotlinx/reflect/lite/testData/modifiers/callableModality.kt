/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.callableModality

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertTrue
import kotlin.test.assertFalse

interface Interface {
    open fun openFun() {}
    abstract fun abstractFun()
}

abstract class AbstractClass {
    final val finalVal = Unit
    open val openVal = Unit
    abstract var abstractVar: Unit
}

fun box(): String {
    val openFun = Interface::class.java.kotlin.getMemberByName("openFun")
    assertFalse(openFun.isFinal)
    assertTrue(openFun.isOpen)
    assertFalse(openFun.isAbstract)

    val abstractFun = Interface::class.java.kotlin.getMemberByName("abstractFun")
    assertFalse(abstractFun.isFinal)
    assertFalse(abstractFun.isOpen)
    assertTrue(abstractFun.isAbstract)

    val finalVal = AbstractClass::class.java.kotlin.getMemberByName("finalVal") as KProperty1<AbstractClass, Unit>
    assertTrue(finalVal.isFinal)
    assertFalse(finalVal.isOpen)
    assertFalse(finalVal.isAbstract)
    assertTrue(finalVal.getter.isFinal)
    assertFalse(finalVal.getter.isOpen)
    assertFalse(finalVal.getter.isAbstract)

    val openVal = AbstractClass::class.java.kotlin.getMemberByName("openVal") as KProperty1<AbstractClass, Unit>
    assertFalse(openVal.isFinal)
    assertTrue(openVal.isOpen)
    assertFalse(openVal.isAbstract)
    assertFalse(openVal.getter.isFinal)
    assertTrue(openVal.getter.isOpen)
    assertFalse(openVal.getter.isAbstract)

    val abstractVar = AbstractClass::class.java.kotlin.getMemberByName("abstractVar") as KMutableProperty1<AbstractClass, Unit>
    assertFalse(abstractVar.isFinal)
    assertFalse(abstractVar.isOpen)
    assertTrue(abstractVar.isAbstract)
    assertFalse(abstractVar.getter.isFinal)
    assertFalse(abstractVar.getter.isOpen)
    assertTrue(abstractVar.getter.isAbstract)
    assertFalse(abstractVar.setter.isFinal)
    assertFalse(abstractVar.setter.isOpen)
    assertTrue(abstractVar.setter.isAbstract)

    return "OK"
}
