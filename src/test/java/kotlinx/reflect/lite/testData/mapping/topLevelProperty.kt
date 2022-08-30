/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.topLevelProperty

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

var topLevel = "123"

val fileFacadeClass = object {}::class.java.enclosingClass

fun box(): String {
    val p = Class.forName("tests.mapping.topLevelProperty.TopLevelPropertyKt").kDeclarationContainer.getMemberByName("topLevel") as KMutableProperty0<String>

    assertNotNull(p.javaField, "Fail p field")
    assertEquals(p.javaField!!.getDeclaringClass(), fileFacadeClass)

    val pp = p.javaField!!.kotlinProperty
    assert(p == pp) { "Fail p != pp" }

    val getter = p.javaGetter!!
    val setter = p.javaSetter!!

    assertEquals(fileFacadeClass.getMethod("getTopLevel"), getter)
    assertEquals(fileFacadeClass.getMethod("setTopLevel", String::class.java), setter)

    assertNull(p.getter.javaConstructor)
    assertNull(p.setter.javaConstructor)

    assertEquals("123", getter.invoke(null), "Fail k getter")
    setter.invoke(null, "456")
    assertEquals("456", getter.invoke(null), "Fail k setter")

    return "OK"
}
