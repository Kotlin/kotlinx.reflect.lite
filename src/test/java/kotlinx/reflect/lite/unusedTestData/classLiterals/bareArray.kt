/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classLiterals.bareArray

import kotlin.test.*
import kotlin.reflect.KClass

fun box(): String {
    val any = Array<Any>::class
    val bare = Array::class

    assertEquals<KClass<*>>(any, bare)

    return "OK"
}
