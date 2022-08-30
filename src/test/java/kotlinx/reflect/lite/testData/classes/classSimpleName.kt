/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classes.classSimpleName

import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

class Klass

fun box(): String {
    assertEquals("Klass", (Klass::class.java).kotlin.simpleName)
    assertEquals("Date", (java.util.Date::class.java).kotlin.simpleName)
    assertEquals("ObjectRef", (kotlin.jvm.internal.Ref.ObjectRef::class.java).kotlin.simpleName)
    return "OK"
}
