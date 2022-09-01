/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.call.bound.objectFunctionWA

import kotlinx.reflect.lite.*
import kotlinx.reflect.lite.jvm.*
import kotlinx.reflect.lite.tests.*

object Host {
    fun foo(x: String) = x
}

class CompanionOwner {
    companion object {
        fun bar(x: String) = x
    }
}

fun box(): String {
    val foo = Host::class.java.kotlin.getMemberByName("foo") as KFunction<String>
    val bar = CompanionOwner.Companion::class.java.kotlin.getMemberByName("bar") as KFunction<String>
    return foo.call(Host, "O") + bar.call(CompanionOwner.Companion, "K")
}

