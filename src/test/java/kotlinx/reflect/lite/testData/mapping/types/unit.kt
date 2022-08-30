/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.unit

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo(unitParam: Unit, nullableUnitParam: Unit?): Unit {}

var bar: Unit = Unit

fun box(): String {
    assert(Unit::class.java != java.lang.Void.TYPE)

    val foo = Class.forName("tests.mapping.types.unit.UnitKt").kDeclarationContainer.getMemberByName("foo") as KFunction<Unit>
    assertEquals(Unit::class.java, foo.parameters[0].type.javaType)
    assertEquals(Unit::class.java, foo.parameters[1].type.javaType)
    assertEquals(java.lang.Void.TYPE, foo.returnType.javaType)

    val bar = Class.forName("tests.mapping.types.unit.UnitKt").kDeclarationContainer.getMemberByName("bar") as KMutableProperty0<Unit>
    assertEquals(Unit::class.java, bar.returnType.javaType)
    assertEquals(Unit::class.java, bar.getter.returnType.javaType)
    assertEquals(Unit::class.java, bar.setter.parameters.single().type.javaType)
    assertEquals(java.lang.Void.TYPE, bar.setter.returnType.javaType)

    return "OK"
}
