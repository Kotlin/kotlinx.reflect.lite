/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.supertypes

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.TypeVariable
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

open class Klass
interface Interface<T, U>
interface Interface2

class A<Z> : Interface<String, Z>, Klass(), Interface2

fun box(): String {
    val (i, k, i2) = (A::class.java.kDeclarationContainer as KClass<A<*>>).supertypes.map { it.javaType }

    i as? ParameterizedType ?: fail("Not a parameterized type: $i")
    assertEquals(Interface::class.java, i.rawType)
    val args = i.actualTypeArguments
    assertEquals(String::class.java, args[0], "Not String: ${args[0]}")
    assertTrue(args[1].let { it is TypeVariable<*> && it.name == "Z" && it.genericDeclaration == A::class.java }, "Not Z: ${args[1]}")

    assertEquals(Klass::class.java, k)

    assertEquals(Interface2::class.java, i2)

    return "OK"
}
