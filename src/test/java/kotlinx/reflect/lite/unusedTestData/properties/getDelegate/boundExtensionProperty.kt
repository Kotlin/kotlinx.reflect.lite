/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.getDelegate.boundExtensionProperty

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.test.*

object Delegate {
    var storage = ""
    operator fun getValue(instance: Any?, property: KProperty<*>) = storage
    operator fun setValue(instance: Any?, property: KProperty<*>, value: String) { storage = value }
}

class Foo

var Foo.result: String by Delegate

fun box(): String {
    val foo = Foo()
    foo.result = "Fail"
    val d = (foo::result).apply { isAccessible = true }.getDelegate() as Delegate
    foo.result = "OK"
    assertEquals(d, (foo::result).apply { isAccessible = true }.getDelegate())
    assertEquals(d, (Foo()::result).apply { isAccessible = true }.getDelegate())
    return d.getValue(foo, Foo::result)
}
