/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.typeParameters.declarationSiteVariance

import kotlin.reflect.KVariance
import kotlin.test.assertEquals

class Triple<in A, B, out C> {
    fun <T> foo(): T = null!!
}

fun box(): String {
    assertEquals(
            listOf(
                    KVariance.IN,
                    KVariance.INVARIANT,
                    KVariance.OUT
            ),
            Triple::class.typeParameters.map { it.variance }
    )

    assertEquals(KVariance.INVARIANT, Triple::class.members.single { it.name == "foo" }.typeParameters.single().variance)

    return "OK"
}
