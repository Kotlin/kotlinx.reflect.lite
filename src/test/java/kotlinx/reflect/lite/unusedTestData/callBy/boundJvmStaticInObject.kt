/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.boundJvmStaticInObject

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.impl.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*


object Host {
    @JvmStatic fun concat(s1: String, s2: String, s3: String = "K", s4: String = "x") =
        s1 + s2 + s3 + s4
}

fun box(): String {
    val concat = (Host::class.java).kotlin.getMemberByName("concat") as KFunction<String>
    val concatParams = concat.parameters
    return concat.callBy(mapOf(
        concatParams[0] to Host,
        concatParams[1] to "",
        concatParams[2] to "O",
        concatParams[4] to ""
    ))
}
