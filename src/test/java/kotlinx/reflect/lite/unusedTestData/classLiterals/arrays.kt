/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classLiterals.arrays

import kotlin.test.*
import kotlin.reflect.KClass

fun box(): String {
    val any = Array<Any>::class
    val string = Array<String>::class

    assertNotEquals<KClass<*>>(any, string)
    assertNotEquals<Class<*>>(any.java, string.java)

    return "OK"
}
