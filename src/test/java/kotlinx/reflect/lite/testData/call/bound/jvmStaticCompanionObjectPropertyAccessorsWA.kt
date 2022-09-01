/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.jvmStaticCompanionObjectPropertyAccessorsWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

class Host {
    companion object {
        @JvmStatic val x = 1
        @JvmStatic var y = 2

        @JvmStatic val xx: Int
            get() = x

        @JvmStatic var yy: Int
            get() = y
            set(value) { y = value }
    }
}

val c_x = Host.Companion::class.java.kotlin.getMemberByName("x") as KProperty1<Host.Companion, Int>
val c_xx = Host.Companion::class.java.kotlin.getMemberByName("xx") as KProperty1<Host.Companion, Int>
val c_y = Host.Companion::class.java.kotlin.getMemberByName("y") as KMutableProperty1<Host.Companion, Int>
val c_yy = Host.Companion::class.java.kotlin.getMemberByName("yy") as KMutableProperty1<Host.Companion, Int>

fun box(): String {
    assertEquals(1, c_x(Host.Companion))
    assertEquals(1, c_x(Host.Companion))
    assertEquals(1, c_xx(Host.Companion))
    assertEquals(1, c_xx(Host.Companion))
    assertEquals(2, c_y(Host.Companion))
    assertEquals(2, c_y(Host.Companion))
    assertEquals(2, c_yy(Host.Companion))
    assertEquals(2, c_yy(Host.Companion))

    c_y.setter(Host.Companion, 10)
    assertEquals(10, c_y.getter(Host.Companion))
    assertEquals(10, c_yy.getter(Host.Companion))

    c_yy.setter(Host.Companion, 20)
    assertEquals(20, c_y.getter(Host.Companion))
    assertEquals(20, c_yy.getter(Host.Companion))

    c_y.setter.call(Host.Companion, 100)
    assertEquals(100, c_yy.getter.call(Host.Companion))

    c_yy.setter.call(Host.Companion, 200)
    assertEquals(200, c_y.getter.call(Host.Companion))

    return "OK"
}
