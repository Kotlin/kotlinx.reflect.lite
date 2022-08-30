/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.kClassInAnnotation.checkcast

import kotlin.reflect.KClass

fun box(): String {
    try {
        String::class.java as KClass<String>
    } catch (e: Exception) {
        return "OK"
    }
    return "fail"
}
