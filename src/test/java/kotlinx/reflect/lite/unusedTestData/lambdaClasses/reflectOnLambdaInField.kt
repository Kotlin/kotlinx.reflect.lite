/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.lambdaClasses.reflectOnLambdaInField

import kotlin.reflect.jvm.reflect

class C {
    val x = { OK: String -> }
}

fun box(): String {

    return C().x.reflect()?.parameters?.singleOrNull()?.name ?: "null"
}
