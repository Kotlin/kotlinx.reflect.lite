/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.typeParameters.innerGenericParameter

import kotlin.reflect.KVariance
import kotlin.test.assertEquals

class A<out T> {
    inner class B<in U> {
        fun test(u: U): T? = null
    }
}

fun box(): String {
    val fn = A.B::class.members.single { it.name == "test" }

    val t = A::class.typeParameters.single()
    val u = A.B::class.typeParameters.single()

    assertEquals("T", t.name)
    assertEquals(KVariance.OUT, t.variance)
    assertEquals("U", u.name)
    assertEquals(KVariance.IN, u.variance)

    assertEquals(t, fn.returnType.classifier)
    assertEquals(u, fn.parameters[1].type.classifier)

    return "OK"
}
