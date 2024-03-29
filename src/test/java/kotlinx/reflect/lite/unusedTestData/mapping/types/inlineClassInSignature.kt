/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.mapping.types.inlineClassInSignature

import kotlin.reflect.jvm.*
import kotlin.test.assertEquals

inline class S(val value: String)

fun S.foo(x: Int, s: S): S = this

/* TODO: Support calling members of inline classes in reflection (KT-26748)
inline class T(val s: S) {
    fun bar(u: S): T = this
}
*/

fun box(): String {
    assertEquals(listOf(String::class.java, Int::class.java, String::class.java), S::foo.parameters.map { it.type.javaType })
    assertEquals(String::class.java, S::foo.returnType.javaType)

/*
    assertEquals(listOf(), T::bar.parameters.map { it.type.javaType })
    assertEquals(String::class.java, T::bar.returnType.javaType)
*/

    return "OK"
}
