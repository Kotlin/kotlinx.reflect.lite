/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.parameters.instanceParameterOfFakeOverride

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

open class A {
    val property = "OK"

    fun function() {}
}

class B : A()

fun box(): String {
    val bProperty = (B::class.java).kotlin.getMemberByName("property")
    assertEquals(KParameter.Kind.INSTANCE, bProperty.parameters[0].kind)
    assertEquals(B::class.java, bProperty.parameters[0].type.javaType)

    val bFunction = (B::class.java).kotlin.getMemberByName("function")
    assertEquals(KParameter.Kind.INSTANCE, bFunction.parameters[0].kind)
    assertEquals(B::class.java, bFunction.parameters[0].type.javaType)

    return "OK"
}
