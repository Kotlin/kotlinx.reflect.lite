/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.extensionPropertyAccessorsWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class C(val x: Int, var y: Int)

val C.xx: Int
    get() = x

var C.yy: Int
    get() = y
    set(value) { y = value }

val c = C(1, 2)

val c_xx = Class.forName("tests.call.bound.extensionPropertyAccessorsWA.ExtensionPropertyAccessorsWAKt").kotlinPackage.getMemberByName("xx") as KProperty1<C, Int>
val c_y = C::class.java.kotlin.getMemberByName("y") as KMutableProperty1<C, Int>
val c_yy = Class.forName("tests.call.bound.extensionPropertyAccessorsWA.ExtensionPropertyAccessorsWAKt").kotlinPackage.getMemberByName("yy") as KMutableProperty1<C, Int>

fun box(): String {
    assertEquals(1, c_xx.getter(c))
    assertEquals(1, c_xx.getter.call(c))
    assertEquals(2, c_yy.getter(c))
    assertEquals(2, c_yy.getter.call(c))

    c_y.setter(c, 10)
    assertEquals(10, c_yy(c))

    c_yy.setter(c, 20)
    assertEquals(20, c_y(c))
    assertEquals(20, c_yy(c))

    c_y.setter.call(c, 100)
    assertEquals(100, c_yy.call(c))

    c_yy.setter.call(c, 200)
    assertEquals(200, c_y.call(c))

    return "OK"
}
