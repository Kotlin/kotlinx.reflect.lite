/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.privateFakeOverrideFromSuperclass

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*

open class A(private val p: Int)
class B : A(42)

fun box() =
        if (B::class.java.kotlin.members.filterIsInstance<KProperty<*>>().isEmpty()) "OK"
        else "Fail: invisible fake overrides should not appear in KClass.memberProperties"
