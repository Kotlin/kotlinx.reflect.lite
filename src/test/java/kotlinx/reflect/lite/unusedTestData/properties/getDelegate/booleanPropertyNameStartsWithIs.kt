/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.getDelegate.booleanPropertyNameStartsWithIs

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.test.*

object Delegate {
    operator fun getValue(instance: Any?, property: KProperty<*>) = true
}

class Foo {
    val isOK: Boolean by Delegate
}

fun box(): String {
    val foo = Foo()
    assertEquals(Delegate, Foo::isOK.apply { isAccessible = true }.getDelegate(foo))
    assertEquals(Delegate, foo::isOK.apply { isAccessible = true }.getDelegate())
    return if (foo.isOK) "OK" else "Fail"
}
