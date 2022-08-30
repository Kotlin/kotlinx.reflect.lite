/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.exceptionHappened

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import java.lang.reflect.InvocationTargetException

fun fail(message: String) {
    throw AssertionError(message)
}

fun box(): String {
    val clazz = Class.forName("tests.call.exceptionHappened.ExceptionHappenedKt").kotlinPackage
    val fail = clazz.getMemberByName("fail")
    try {
        fail.call("OK")
    } catch (e: InvocationTargetException) {
        return e.getTargetException().message.toString()
    }

    return "Fail: no exception was thrown"
}
