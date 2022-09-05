/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.modifiers.classVisibility

import kotlin.test.assertEquals
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.*

class DefaultVisibilityClass
public class PublicClass {
    protected class ProtectedClass
    fun getProtectedClass(): KClass<*> = ProtectedClass::class.java.kotlin
}
internal class InternalClass
private class PrivateClass

fun box(): String {
    assertEquals(KVisibility.PUBLIC, DefaultVisibilityClass::class.java.kotlin.visibility)
    assertEquals(KVisibility.PUBLIC, PublicClass::class.java.kotlin.visibility)
    assertEquals(KVisibility.PROTECTED, PublicClass().getProtectedClass().visibility)
    assertEquals(KVisibility.INTERNAL, InternalClass::class.java.kotlin.visibility)
    assertEquals(KVisibility.PRIVATE, PrivateClass::class.java.kotlin.visibility)

    class Local
    assertEquals(null, Local::class.java.kotlin.visibility)

    val anonymous = object {}
    assertEquals(null, anonymous::class.java.kotlin.visibility)

    return "OK"
}
