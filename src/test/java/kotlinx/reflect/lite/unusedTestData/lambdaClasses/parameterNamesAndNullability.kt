/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.lambdaClasses.parameterNamesAndNullability

import kotlin.reflect.KParameter
import kotlin.reflect.jvm.reflect
import kotlin.test.assertEquals
import kotlin.test.assertNull

fun lambda() {
    val f = { x: Int, y: String? -> }

    val g = f.reflect()!!

    // TODO: maybe change this name
    assertEquals("<anonymous>", g.name)
    assertEquals(listOf("x", "y"), g.parameters.map { it.name })
    assertEquals(listOf(false, true), g.parameters.map { it.type.isMarkedNullable })
}

fun funExpr() {
    val f = fun(x: Int, y: String?) {}

    val g = f.reflect()!!

    // TODO: maybe change this name
    assertEquals("<no name provided>", g.name)
    assertEquals(listOf("x", "y"), g.parameters.map { it.name })
    assertEquals(listOf(false, true), g.parameters.map { it.type.isMarkedNullable })
}

fun extensionFunExpr() {
    val f = fun String.(): String = this

    val g = f.reflect()!!

    assertEquals(KParameter.Kind.EXTENSION_RECEIVER, g.parameters.single().kind)
    assertEquals(null, g.parameters.single().name)
}

fun box(): String {
    lambda()
    funExpr()
    extensionFunExpr()

    return "OK"
}
