/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.properties

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertTrue
import kotlin.test.assertFalse

const val const = "const"
val nonConst = "nonConst"

class A {
    lateinit var lateinit: Unit
    var nonLateinit = Unit
}

fun box(): String {
    val clazz = Class.forName("tests.modifiers.properties.PropertiesKt").kotlin
    assertTrue((clazz.getMemberByName("const") as KProperty0<String>).isConst)
    assertFalse((clazz.getMemberByName("nonConst") as KProperty0<String>).isConst)

    assertTrue((A::class.java.kotlin.getMemberByName("lateinit") as KProperty1<A, Unit>).isLateInit)
    assertFalse((A::class.java.kotlin.getMemberByName("nonLateinit") as KProperty1<A, Unit>).isLateInit)

    return "OK"
}
