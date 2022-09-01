/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.memberPropertyAccessorsWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class C(val x: Int, var y: Int) {
    val xx: Int
        get() = x

    var yy: Int
        get() = y
        set(value) { y = value }
}

val c = C(1, 2)

val c_x = c::class.java.kotlin.getMemberByName("x") as KProperty1<C, Int>
val c_xx = c::class.java.kotlin.getMemberByName("xx") as KProperty1<C, Int>
val c_y = c::class.java.kotlin.getMemberByName("y") as KMutableProperty1<C, Int>
val c_yy = c::class.java.kotlin.getMemberByName("yy") as KMutableProperty1<C, Int>

fun box(): String {
    assertEquals(1, c_x.getter(c))
    assertEquals(1, c_x.getter.call(c))
    assertEquals(1, c_xx(c))
    assertEquals(1, c_xx.getter.call(c))
    assertEquals(2, c_y(c))
    assertEquals(2, c_y.getter.call(c))
    assertEquals(2, c_yy(c))
    assertEquals(2, c_yy.getter.call(c))

    c_y.setter(c, 10)
    assertEquals(10, c_y.getter(c))
    assertEquals(10, c_yy.getter(c))

    c_yy.setter(c, 20)
    assertEquals(20, c_y.getter(c))
    assertEquals(20, c_yy.getter(c))

    c_y.setter.call(c, 100)
    assertEquals(100, c_yy.getter.call(c))

    c_yy.setter.call(c, 200)
    assertEquals(200, c_y.getter.call(c))

    return "OK"
}
