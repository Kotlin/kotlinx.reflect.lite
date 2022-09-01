/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.typeParameters

import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class A {
    fun <T> nonReified(): T = null!!
    inline fun <reified U> reified(): U = null!!
}

fun box(): String {
    assertFalse(A::class.java.kotlin.members.single { it.name == "nonReified" }.typeParameters.single().isReified)
    assertTrue(A::class.java.kotlin.members.single { it.name == "reified" }.typeParameters.single().isReified)
    return "OK"
}
