/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.privateToThisAccessors

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.full.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*


class K<in T : String> {
    private var t: T
        get() = "OK" as T
        set(value) {}

    fun run(): String {
        val p = (K::class.java).kotlin.getMemberByName("t") as KMutableProperty1<K<String>, String>
        p.isAccessible = true
        p.set(this as K<String>, "")
        return p.get(this) as String
    }
}

fun box() = K<String>().run()
