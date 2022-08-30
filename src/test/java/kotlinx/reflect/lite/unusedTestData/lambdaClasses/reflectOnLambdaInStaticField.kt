/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.lambdaClasses.reflectOnLambdaInStaticField

import kotlin.reflect.jvm.reflect

val x = { OK: String -> }

fun box(): String {
    return x.reflect()?.parameters?.singleOrNull()?.name ?: "null"
}
