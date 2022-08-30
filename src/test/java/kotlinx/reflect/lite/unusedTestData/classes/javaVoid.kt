/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classes.javaVoid

import kotlin.test.assertEquals

fun box(): String {
    assertEquals(Void::class, Void.TYPE.kotlin)
    assertEquals(Void.TYPE.kotlin, Void::class)

    assertEquals(Void.TYPE, Void::class.javaPrimitiveType)
    assertEquals(Void::class.java, Void::class.javaObjectType)
    assertEquals(Void.TYPE, Void.TYPE.kotlin.javaPrimitiveType)
    assertEquals(Void::class.java, Void.TYPE.kotlin.javaObjectType)

    assertEquals("Void", Void::class.simpleName)
    assertEquals("java.lang.Void", Void::class.qualifiedName)

    return "OK"
}
