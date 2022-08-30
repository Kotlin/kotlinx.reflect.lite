/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.ordinaryMethodIsInvokedWhenNoDefaultValuesAreUsed

import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*
import kotlin.test.assertEquals

fun foo(result: String = "foo") {
    assertEquals("box", result)

    // Check that this function was invoked directly and not through the "foo$default", i.e. there's no "foo$default" in the stack trace
    val st = Thread.currentThread().stackTrace
    for (i in 0..5) {
        if ("foo\$default" in st[i].methodName) {
            throw AssertionError("KCallable.call should invoke the method directly if all arguments are provided")
        }
    }
}

fun box(): String {
    val clazz = Class.forName("tests.callBy.ordinaryMethodIsInvokedWhenNoDefaultValuesAreUsed.OrdinaryMethodIsInvokedWhenNoDefaultValuesAreUsedKt").kotlinPackage
    val foo = clazz.getMemberByName("foo")
    foo.callBy(mapOf(foo.parameters.single() to "box"))
    return "OK"
}
