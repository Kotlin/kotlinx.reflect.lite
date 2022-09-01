/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.companionObjectPropertyAccessorsWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

class Host {
    companion object {
        val x = 1
        var y = 2

        val xx: Int
            get() = x

        var yy: Int
            get() = y
            set(value) { y = value }
    }
}

val c_x = Host.Companion::class.java.kotlin.members.single { it.name == "x" } as KProperty1<Host.Companion, Int>
val c_xx = Host.Companion::class.java.kotlin.members.single { it.name == "xx" } as KProperty1<Host.Companion, Int>
val c_y = Host.Companion::class.java.kotlin.members.single { it.name == "y" } as KMutableProperty1<Host.Companion, Int>
val c_yy = Host.Companion::class.java.kotlin.members.single { it.name == "yy" } as KMutableProperty1<Host.Companion, Int>

fun box(): String {
    assertEquals(1, c_x.call(Host.Companion))
    assertEquals(1, c_x.call(Host.Companion))
    assertEquals(1, c_xx.call(Host.Companion))
    assertEquals(1, c_xx.call(Host.Companion))
    assertEquals(2, c_y.call(Host.Companion))
    assertEquals(2, c_y.call(Host.Companion))
    assertEquals(2, c_yy.call(Host.Companion))
    assertEquals(2, c_yy.call(Host.Companion))

    c_y.setter(Host.Companion, 10)
    assertEquals(10, c_y.call(Host.Companion))
    assertEquals(10, c_yy.call(Host.Companion))

    c_yy.setter(Host.Companion, 20)
    assertEquals(20, c_y.call(Host.Companion))
    assertEquals(20, c_yy.call(Host.Companion))

    c_y.setter.call(Host.Companion, 100)
    assertEquals(100, c_yy.call(Host.Companion))

    c_yy.setter.call(Host.Companion, 200)
    assertEquals(200, c_y.call(Host.Companion))

    return "OK"
}
