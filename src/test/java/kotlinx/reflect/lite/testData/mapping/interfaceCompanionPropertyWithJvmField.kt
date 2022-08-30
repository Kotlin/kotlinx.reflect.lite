/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.interfaceCompanionPropertyWithJvmField

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

interface Foo {
    companion object {
        @JvmField
        val value = "OK"
    }
}

fun box(): String {
    val value = Foo.Companion::class.java.kDeclarationContainer.getMemberByName("value") as KProperty1<Foo.Companion, String>
    val field = value.javaField
    assertNotNull(field, "javaField not found for property declared in the interface's companion object")
    return field.get(null) as String
}
