/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.localDelegated.defaultImpls

import kotlin.reflect.KProperty
import kotlin.test.assertEquals

object Delegate {
    operator fun getValue(z: Any?, p: KProperty<*>): String? {
        assertEquals("val x: kotlin.String?", p.toString())
        return "OK"
    }
}

interface Foo {
    fun bar(): String {
        val x by Delegate
        return x!!
    }
}

object O : Foo

fun box(): String = O.bar()
