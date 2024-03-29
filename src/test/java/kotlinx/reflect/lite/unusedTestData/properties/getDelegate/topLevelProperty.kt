/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.getDelegate.topLevelProperty

import kotlin.reflect.KProperty
import kotlin.reflect.jvm.isAccessible
import kotlin.test.*

object Delegate {
    var storage = ""
    operator fun getValue(instance: Any?, property: KProperty<*>) = storage
    operator fun setValue(instance: Any?, property: KProperty<*>, value: String) { storage = value }
}

var result: String by Delegate

fun box(): String {
    result = "Fail"
    val p = (::result).apply { isAccessible = true }
    val d = p.getDelegate() as Delegate
    result = "OK"
    assertEquals(d, (::result).apply { isAccessible = true }.getDelegate())
    return d.getValue(null, p)
}
