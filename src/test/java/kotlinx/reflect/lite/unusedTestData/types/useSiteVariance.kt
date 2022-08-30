/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.types.useSiteVariance

import kotlin.reflect.KVariance
import kotlin.test.assertEquals

class Fourple<A, B, C, D>
fun foo(): Fourple<String, in String, out String, *> = null!!

fun listOfStrings(): List<String> = null!!

fun box(): String {
    assertEquals(
            listOf(
                    KVariance.INVARIANT,
                    KVariance.IN,
                    KVariance.OUT,
                    null
            ),
            ::foo.returnType.arguments.map { it.variance }
    )

    // Declaration-site variance should have no effect on the variance of the type projection:
    assertEquals(KVariance.INVARIANT, ::listOfStrings.returnType.arguments.first().variance)

    return "OK"
}
