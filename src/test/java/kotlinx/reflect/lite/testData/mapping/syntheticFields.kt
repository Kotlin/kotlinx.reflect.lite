/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.syntheticFields

import kotlinx.reflect.lite.jvm.kotlinProperty

enum class A {
    // There's a synthetic field "$VALUES" here
}

fun box(): String {
    for (field in A::class.java.getDeclaredFields()) {
        val prop = field.kotlinProperty
        if (prop != null) return "Fail, property found: $prop"
    }

    return "OK"
}
