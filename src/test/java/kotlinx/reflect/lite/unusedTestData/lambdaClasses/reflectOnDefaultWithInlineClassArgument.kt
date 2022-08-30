/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.lambdaClasses.reflectOnDefaultWithInlineClassArgument

import kotlin.reflect.jvm.reflect

inline class C(val x: Int)

fun C.f(x: (String) -> Unit = { OK: String -> }) = x.reflect()?.parameters?.singleOrNull()?.name

fun box(): String = C(0).f() ?: "null"
