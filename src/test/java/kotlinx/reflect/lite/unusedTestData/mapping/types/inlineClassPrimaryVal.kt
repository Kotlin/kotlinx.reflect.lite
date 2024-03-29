/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.inlineClassPrimaryVal

import kotlin.reflect.KCallable
import kotlin.reflect.jvm.*
import kotlin.test.assertEquals

inline class Z1(val publicX: Int) {
    companion object {
        val publicXRef = Z1::publicX
        val publicXBoundRef = Z1(42)::publicX
    }
}

inline class Z2(internal val internalX: Int) {
    companion object {
        val internalXRef = Z2::internalX
        val internalXBoundRef = Z2(42)::internalX
    }
}

inline class Z3(private val privateX: Int) {
    companion object {
        val privateXRef = Z3::privateX
        val privateXBoundRef = Z3(42)::privateX
    }
}

inline class ZZ(val x: Z1)

fun KCallable<*>.getJavaTypesOfParams() = parameters.map { it.type.javaType }.toString()
fun KCallable<*>.getJavaTypeOfResult() = returnType.javaType.toString()

fun box(): String {
    assertEquals("[class tests.mapping.types.inlineClassPrimaryVal.Z1]",  Z1.publicXRef.getJavaTypesOfParams())
    assertEquals("int",                             Z1.publicXRef.getJavaTypeOfResult())

    assertEquals("[]",          Z1.publicXBoundRef.getJavaTypesOfParams())
    assertEquals("int",         Z1.publicXBoundRef.getJavaTypeOfResult())

    assertEquals("[class tests.mapping.types.inlineClassPrimaryVal.Z2]",  Z2.internalXRef.getJavaTypesOfParams())
    assertEquals("int",                             Z2.internalXRef.getJavaTypeOfResult())

    assertEquals("[]",          Z2.internalXBoundRef.getJavaTypesOfParams())
    assertEquals("int",         Z2.internalXBoundRef.getJavaTypeOfResult())

    assertEquals("[class tests.mapping.types.inlineClassPrimaryVal.Z3]",  Z3.privateXRef.getJavaTypesOfParams())
    assertEquals("int",                             Z3.privateXRef.getJavaTypeOfResult())

    assertEquals("[]",          Z3.privateXBoundRef.getJavaTypesOfParams())
    assertEquals("int",         Z3.privateXBoundRef.getJavaTypeOfResult())


    assertEquals("[class tests.mapping.types.inlineClassPrimaryVal.ZZ]",  ZZ::x.getJavaTypesOfParams())

    // KT-28170
    assertEquals("int",         ZZ::x.getJavaTypeOfResult())

    return "OK"
}
