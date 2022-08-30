/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.returnUnit

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo() {}

class A {
    fun bar() {}
}

object O {
    @JvmStatic fun baz() {}
}

fun nullableUnit(unit: Boolean): Unit? = if (unit) Unit else null

fun box(): String {
    val clazz = Class.forName("tests.call.returnUnit.ReturnUnitKt").kDeclarationContainer
    val foo = clazz.getMemberByName("foo")
    assertEquals(Unit, foo.call())
    val bar = (A::class.java.kotlin).getMemberByName("bar")
    assertEquals(Unit, bar.call(A()))
    assertEquals(Unit, ((O::class.java.kotlin)).members.single { it.name == "baz" }.call(O))

    val nullableUnit = clazz.getMemberByName("nullableUnit")
    assertEquals(Unit, nullableUnit.call(true))
    assertEquals(null, nullableUnit.call(false))

    return "OK"
}
