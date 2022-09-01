/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.objectPropertyAccessorsWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

object Host {
    val x = 1
    var y = 2

    val xx: Int
        get() = x

    var yy: Int
        get() = y
        set(value) { y = value }
}

val c_x = Host::class.java.kotlin.getMemberByName("x") as KProperty1<Host, Int>
val c_xx = Host::class.java.kotlin.getMemberByName("xx") as KProperty1<Host, Int>
val c_y = Host::class.java.kotlin.getMemberByName("y") as KMutableProperty1<Host, Int>
val c_yy = Host::class.java.kotlin.getMemberByName("yy") as KMutableProperty1<Host, Int>

fun box(): String {
    assertEquals(1, c_x.getter(Host))
    assertEquals(1, c_x.getter.call(Host))
    assertEquals(1, c_xx(Host))
    assertEquals(1, c_xx.getter.call(Host))
    assertEquals(2, c_y.getter(Host))
    assertEquals(2, c_y.getter.call(Host))
    assertEquals(2, c_yy(Host))
    assertEquals(2, c_yy.getter.call(Host))

    c_y.setter(Host, 10)
    assertEquals(10, c_y(Host))
    assertEquals(10, c_yy(Host))

    c_yy.setter(Host, 20)
    assertEquals(20, c_y.getter(Host))
    assertEquals(20, c_yy.getter(Host))

    c_y.setter.call(Host, 100)
    assertEquals(100, c_yy.getter.call(Host))

    c_yy.setter.call(Host, 200)
    assertEquals(200, c_y.getter.call(Host))

    return "OK"
}
