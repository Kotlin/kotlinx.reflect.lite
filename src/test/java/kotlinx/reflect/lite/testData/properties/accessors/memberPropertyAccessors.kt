/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.accessors.memberPropertyAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class C(var state: String)

fun box(): String {
    val prop = (C::class.java.kotlin).getMemberByName("state") as KMutableProperty1<C, String>

    val c = C("1")
    assertEquals("1", prop.getter.invoke(c))
    assertEquals("1", prop.getter(c))

    prop.setter(c, "OK")

    return prop.get(c)
}
