/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.jvmStaticObjectFunction

import kotlin.reflect.*

object Host {
    @JvmStatic fun foo(x: String) = x
}

class CompanionOwner {
    companion object {
        @JvmStatic fun bar(x: String) = x
    }
}

fun box(): String =
        (Host::foo).call("O") + (CompanionOwner.Companion::bar).call("K")
