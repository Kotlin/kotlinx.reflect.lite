/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.methodsFromAny.typeParametersToString

import kotlin.test.assertEquals

interface Variance<A, in B, out C, D>
class OneBound<T : Enum<T>>
class SeveralBounds<T : Cloneable> where T : Enum<T>, T : Variance<String, Int?, Double?, Number>

fun box(): String {
    assertEquals("[A, in B, out C, D]", Variance::class.typeParameters.toString())
    assertEquals("[T]", OneBound::class.typeParameters.toString())
    assertEquals("[T]", SeveralBounds::class.typeParameters.toString())

    return "OK"
}
