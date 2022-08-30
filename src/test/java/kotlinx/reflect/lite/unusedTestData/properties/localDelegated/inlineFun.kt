/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.localDelegated.inlineFun

import kotlin.reflect.*
import kotlin.test.assertEquals

object Delegate {
    lateinit var property: KProperty<*>

    operator fun getValue(instance: Any?, kProperty: KProperty<*>) {
        property = kProperty
    }
}

class Foo {
    inline fun foo() {
        val x by Delegate
        x
    }
}

fun box(): String {
    Foo().foo()
    assertEquals("val x: kotlin.Unit", Delegate.property.toString())
    return "OK"
}
