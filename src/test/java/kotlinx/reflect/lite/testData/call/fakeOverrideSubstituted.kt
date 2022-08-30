/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.fakeOverrideSubstituted

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*

open class A<T>(val t: T) {
    fun foo() = t
}

class B(s: String) : A<String>(s)

fun box(): String {
    val foo = ((B::class.java).kotlin).getMemberByName("foo")
    return foo.call(B("OK")) as String
}
