/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.accessors.extensionPropertyAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.*

var state: String = ""

var String.prop: String
    get() = length.toString()
    set(value) { state = this + value }

fun box(): String {
    val clazz = Class.forName("tests.properties.accessors.extensionPropertyAccessors.ExtensionPropertyAccessorsKt").kotlinPackage
    val prop = clazz.getMemberByName("prop") as KMutableProperty1<String, String>

    assertEquals("3", prop.getter.invoke("abc"))
    assertEquals("5", prop.getter("defgh"))

    prop.setter("O", "K")

    return state
}
