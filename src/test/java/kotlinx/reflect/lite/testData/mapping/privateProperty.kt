/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.privateProperty

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

class K(private var value: Long)

fun box(): String {
    val p = (K::class.java.kDeclarationContainer).getMemberByName("value") as KMutableProperty1<K, Long>

    assertNotNull(p.javaField, "Fail p field")
    assertNull(p.javaGetter, "Fail p getter")
    assertNull(p.javaSetter, "Fail p setter")

    return "OK"
}
