/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.allVsDeclared

import kotlin.reflect.full.*
import kotlin.test.*

open class Super {
    val a: Int = 1
    val String.b: String get() = this
}

class Sub : Super() {
    val c: Double = 1.0
    val Char.d: Char get() = this
}

fun box(): String {
    val sub = Sub::class

    assertEquals(listOf("a", "c"), sub.memberProperties.map { it.name }.sorted())
    assertEquals(listOf("b", "d"), sub.memberExtensionProperties.map { it.name }.sorted())
    assertEquals(listOf("c"), sub.declaredMemberProperties.map { it.name })
    assertEquals(listOf("d"), sub.declaredMemberExtensionProperties.map { it.name })

    return "OK"
}
