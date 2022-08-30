/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.properties.localDelegated.inLambda

import kotlin.reflect.*

fun <T> eval(fn: () -> T) = fn()

inline operator fun String.getValue(t:Any?, p: KProperty<*>): String =
    if (p.returnType.classifier == String::class) this else "fail"

fun box() = eval {
    val x by "OK"
    x
}
