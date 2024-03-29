/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.propertyAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

class A(private var foo: String)

object O {
    @JvmStatic
    private var bar: String = ""
}

fun box(): String {
    val foo = A::class.java.kDeclarationContainer.members.single { it.name == "foo" } as KMutableProperty<*>
    assertEquals(listOf(A::class.java), foo.parameters.map { it.type.javaType })
    assertEquals(listOf(A::class.java), foo.getter.parameters.map { it.type.javaType })
    assertEquals(listOf(A::class.java, String::class.java), foo.setter.parameters.map { it.type.javaType })

    val bar = O::class.java.kDeclarationContainer.members.single { it.name == "bar" } as KMutableProperty<*>
    assertEquals(listOf(O::class.java), bar.parameters.map { it.type.javaType })
    assertEquals(listOf(O::class.java), bar.getter.parameters.map { it.type.javaType })
    assertEquals(listOf(O::class.java, String::class.java), bar.setter.parameters.map { it.type.javaType })

    return "OK"
}
