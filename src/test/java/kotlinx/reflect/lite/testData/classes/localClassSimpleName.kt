/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.classes.localClassSimpleName

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlin.test.assertEquals

fun check(klass: KClass<*>, expectedName: String) {
    assertEquals(expectedName, klass.simpleName)
}

fun localInMethod() {
    fun localInMethod(unused: Any?) {
        class Local
        check(Local::class.java.kotlin, "Local")

        class `Local$With$Dollars`
        check(`Local$With$Dollars`::class.java.kotlin, "Local\$With\$Dollars")
    }
    localInMethod(null)

    class Local
    check(Local::class.java.kotlin, "Local")

    class `Local$With$Dollars`
    check(`Local$With$Dollars`::class.java.kotlin, "Local\$With\$Dollars")
}

class LocalInConstructor {
    init {
        class Local
        check(Local::class.java.kotlin, "Local")

        class `Local$With$Dollars`
        check(`Local$With$Dollars`::class.java.kotlin, "Local\$With\$Dollars")
    }
}

fun box(): String {
    localInMethod()
    LocalInConstructor()
    return "OK"
}
