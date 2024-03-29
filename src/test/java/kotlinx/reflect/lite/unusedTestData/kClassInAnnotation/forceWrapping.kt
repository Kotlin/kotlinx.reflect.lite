/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.kClassInAnnotation.forceWrapping

import kotlin.reflect.KClass
import kotlin.test.assertEquals

annotation class Anno(
        val klass: KClass<*>,
        val kClasses: Array<KClass<*>>,
        vararg val kClassesVararg: KClass<*>
)

@Anno(String::class, arrayOf(Int::class), Double::class)
fun foo() {}

fun box(): String {
    val k = ::foo.annotations.single() as Anno
    assertEquals(String::class, k.klass)
    assertEquals(Int::class, k.kClasses[0])
    assertEquals(Double::class, k.kClassesVararg[0])
    return "OK"
}
