/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.functions

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertTrue
import kotlin.test.assertFalse

inline fun inline() {}
class External { external fun external() }
operator fun Unit.invoke() {}
infix fun Unit.infix(unit: Unit) {}
class Suspend { suspend fun suspend() {} }

val externalGetter = Unit
    external get

inline var inlineProperty: Unit
    get() = Unit
    set(value) {}

fun box(): String {
    val clazz = Class.forName("tests.modifiers.functions.FunctionsKt").kotlinPackage

    val inline = clazz.getMemberByName("inline") as KFunction<Unit>
    assertTrue(inline.isInline)
    assertFalse(inline.isExternal)
    assertFalse(inline.isOperator)
    assertFalse(inline.isInfix)
    assertFalse(inline.isSuspend)

    val external = External::class.java.kotlin.getMemberByName("external") as KFunction<Unit>
    assertFalse(external.isInline)
    assertTrue(external.isExternal)
    assertFalse(external.isOperator)
    assertFalse(external.isInfix)
    assertFalse(external.isSuspend)

    val invoke = clazz.getMemberByName("invoke") as KFunction<Unit>
    assertFalse(invoke.isInline)
    assertFalse(invoke.isExternal)
    assertTrue(invoke.isOperator)
    assertFalse(invoke.isInfix)
    assertFalse(invoke.isSuspend)

    val infix = clazz.getMemberByName("infix") as KFunction<Unit>
    assertFalse(infix.isInline)
    assertFalse(infix.isExternal)
    assertFalse(infix.isOperator)
    assertTrue(infix.isInfix)
    assertFalse(infix.isSuspend)

    val suspend = Suspend::class.java.kotlin.getMemberByName("suspend") as KFunction<Unit>
    assertFalse(suspend.isInline)
    assertFalse(suspend.isExternal)
    assertFalse(suspend.isOperator)
    assertFalse(suspend.isInfix)
    assertTrue(suspend.isSuspend)

    val externalGetter = clazz.getMemberByName("externalGetter") as KProperty0<Unit>
    // TODO should be true, see KT-53863
    // assertTrue(externalGetter.getter.isExternal)
    assertFalse(externalGetter.getter.isInline)

    val inlineProperty = clazz.getMemberByName("inlineProperty") as KMutableProperty0<Unit>
    assertFalse(inlineProperty.getter.isExternal)
    // TODO should be true, see KT-53862
    //assertTrue(inlineProperty.getter.isInline)
    // TODO should be true, see KT-53862
    //assertTrue(inlineProperty.setter.isInline)
    assertFalse(inlineProperty.isSuspend)
    return "OK"
}
