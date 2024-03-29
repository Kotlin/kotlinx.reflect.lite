/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.mappedClassIsEqualToClassLiteral

class A

fun box(): String {
    val a1 = A::class.java.kotlin
    val a2 = A::class

    if (a1 != a2) return "Fail equals"
    if (a1.hashCode() != a2.hashCode()) return "Fail hashCode"
    if (a1.toString() != a2.toString()) return "Fail toString"

    return "OK"
}
