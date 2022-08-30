/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.objectFunction

import kotlin.reflect.*

object Host {
    fun foo(x: String) = x
}

class CompanionOwner {
    companion object {
        fun bar(x: String) = x
    }
}

fun box(): String =
        (Host::foo).call("O") + (CompanionOwner.Companion::bar).call("K")
