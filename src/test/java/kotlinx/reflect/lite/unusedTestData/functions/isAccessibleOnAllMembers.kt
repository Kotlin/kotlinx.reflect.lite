/*
 * Copyright 2016-2022 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package tests.functions.isAccessibleOnAllMembers

import kotlin.reflect.jvm.isAccessible

fun box(): String {
    val members = Observer::class.members
    for (member in members) {
        member.isAccessible = true
    }
    return members.single { it.name == "result" }.call(Observer()) as String
}

class Observer : AutoCloseable {
    override fun close() {
    }

    private fun result() = "OK"
}
