/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.getDelegate.fakeOverride

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.test.*

object Delegate {
    operator fun getValue(instance: Any?, property: KProperty<*>) = "OK"
}

open class Base {
    val x: String by Delegate
}

class Derived : Base()

fun box(): String {
    val d = Derived()
    assertEquals(
            (Base::x).apply { isAccessible = true }.getDelegate(d),
            (Derived::x).apply { isAccessible = true }.getDelegate(d)
    )
    return d.x
}
