/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.lambdaClasses.reflectOnLambdaInConstructor

import kotlin.reflect.jvm.reflect

class C {
    val o = { O: String -> }
    val k = { K: String -> }

    constructor(y: Int)
    constructor(y: String)
}

fun box(): String =
    (C(0).o.reflect()?.parameters?.singleOrNull()?.name ?: "null") +
            (C("").k.reflect()?.parameters?.singleOrNull()?.name ?: "null")
