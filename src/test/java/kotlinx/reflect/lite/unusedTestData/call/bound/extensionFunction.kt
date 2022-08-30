/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.extensionFunction

import kotlin.reflect.*

fun String.foo(x: String) = this + x
fun String?.bar(x: String) = x

fun box() =
        (""::foo).call("O") + (null::bar).call("K")
