/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.callBy.boundExtensionFunction

import kotlin.test.assertEquals

fun String.extFun(k: String, s: String = "") = this + k + s

fun box(): String {
    val sExtFun = "O"::extFun
    return sExtFun.callBy(mapOf(sExtFun.parameters[0] to "K"))
}
