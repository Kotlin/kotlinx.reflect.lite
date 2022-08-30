/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.invokeKProperty

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*


class A(val foo: String)

fun box(): String {
    return ((A::class.java).kotlin.getMemberByName("foo") as KProperty1<A, String>).invoke(A("OK")) as String
}
