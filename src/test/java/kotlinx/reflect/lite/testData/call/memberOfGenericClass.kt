/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.memberOfGenericClass

import kotlinx.reflect.lite.tests.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

var result = "Fail"

class A<T> {
    fun foo(t: T) {
        result = t as String
    }
}

fun box(): String {
    val a = ((A::class.java).kotlin)
    val foo = a.getMemberByName("foo")
    foo.call(a.getPrimaryConstructor().call(), "OK")
    return result
}
