/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.jvmStaticInCompanionObject

// KT-12915 IAE on callBy of JvmStatic function with default arguments

import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

class C {
    companion object {
        @JvmStatic
        fun foo(a: String, b: String = "b") = a + b
    }
}

fun box(): String {
    val f = (C.Companion::class.java).kDeclarationContainer.members.single { it.name == "foo" }

    // Any object method currently requires the object instance passed
    try {
        f.callBy(mapOf(
                f.parameters.single { it.name == "a" } to "a"
        ))
        return "Fail: IllegalArgumentException should have been thrown"
    }
    catch (e: IllegalArgumentException) {
        // OK
    }

    assertEquals("ab", f.callBy(mapOf(
            f.parameters.first() to C,
            f.parameters.single { it.name == "a" } to "a"
    )))

    return "OK"
}
