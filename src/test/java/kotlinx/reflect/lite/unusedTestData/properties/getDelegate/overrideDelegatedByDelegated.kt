/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.getDelegate.overrideDelegatedByDelegated

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.test.*

class Delegate(val value: String) {
    operator fun getValue(instance: Any?, property: KProperty<*>) = value
}

open class Base {
    open val x: String by Delegate("Base")
}

class Derived : Base() {
    override val x: String by Delegate("Derived")
}

fun check(expected: String, delegate: Any?) {
    if (delegate == null) throw AssertionError("getDelegate returned null")
    assertEquals(expected, (delegate as Delegate).value)
}

fun box(): String {
    val base = Base()
    val derived = Derived()

    check("Base", (Base::x).apply { isAccessible = true }.getDelegate(base))
    check("Base", (base::x).apply { isAccessible = true }.getDelegate())
    check("Derived", (Derived::x).apply { isAccessible = true }.getDelegate(derived))
    check("Derived", (derived::x).apply { isAccessible = true }.getDelegate())

    check("Base", (Base::x).apply { isAccessible = true }.getDelegate(derived))

    return "OK"
}
