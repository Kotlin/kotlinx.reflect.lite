/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.fakeOverridesInSubclass

import kotlin.reflect.full.*
import kotlin.test.*

open class Super(val r: String)

class Sub(r: String) : Super(r)

fun box(): String {
    val props = Sub::class.declaredMemberProperties
    if (!props.isEmpty()) return "Fail $props"

    val allProps = Sub::class.memberProperties
    assertEquals(listOf("r"), allProps.map { it.name })
    return allProps.single().get(Sub("OK")) as String
}
